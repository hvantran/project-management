# http-request-monitor poller reference

## Runtime

- UI: `http://localhost:6084` (gateway `/api/action-manager`)
- Backend in Docker: `0.0.0.0:6085->8082`, context `/action-manager-backend`
- Mongo: `mongodb://localhost:27017`
  - `action-manager.actions`, `action-manager.jobs`
  - `template-manager.template-collection`
- Dry run: `POST /v1/jobs/validations?actionId={actionId}` with `JobDefinitionDTO` JSON. No persistence. Always HTTP 204 if the controller returns; check container logs.
- Freemarker runs on `jobContent` **before** Graal (`JobManagerServiceImpl.process`). Placeholders in factory scripts are real Freemarker.

Action id (local): `d4685841-91e3-4228-9072-f6ac16219df5` (`http-request-monitor`).

Known factories (local):

| Job `_id` | `jobName` |
|-----------|-----------|
| `6b4e6b36-64e6-404f-8329-a4adc21493e6` | `hasaki-price-monitor-request-template-IO` |
| `6308747a-5984-4ca7-b1cd-a4c2548223f2` | `lazada-price-monitor-request-template-IO` |

Templates: `action-manager-hasaki-price-monitor-with-slack-alerts`, `action-manager-lazada-price-monitor-with-slack-alerts`.

Poller job fields: `category` `IO`, `outputTargets` `["CONSOLE","METRIC"]`, `isScheduled` true, `scheduleInterval` from factory config, `configurations` JSON object string (often `{}` on pollers; factory holds `productUrl` / `productId`, `priceThreshold`, `actionId`, `slackWebhookURL`, `outOfStockValue`, `scheduleInterval`).

Job document keys: `_id`, `jobName`, `jobContent`, `actionId`, `outputTargets`, `isScheduled`, `scheduleInterval`, `configurations`, `jobStatus`.

## HTTP from GraalJS

```javascript
let RequestParams = Java.type('com.hoatv.fwk.common.services.HttpClientService.RequestParams');
let HttpMethod = Java.type('com.hoatv.fwk.common.services.HttpClientService.HttpMethod');
let HttpClientService = Java.type('com.hoatv.fwk.common.services.HttpClientService');
let HttpClient = Java.type('java.net.http.HttpClient');
let JobResult = Java.type('com.hoatv.action.manager.services.JobResult');
```

Use injected `httpClientService.sendHTTPRequest().apply(requestParams)`. Helpers: `asString`, `is2xx`, `asScriptHttpResult`. Internal POSTs (create job, create notifier) get Bearer via `OutboundRequestAuth` when URL host is internal. Public GETs must stay unauthenticated.

Wrap `sendHTTPRequest` in `try/catch`; return `{ statusCode, html, error }` rather than throwing. Docker has no working IPv6; `www.lazada.vn` AAAA → `ConnectException`. Prefer IPv4 / mobile host that has A records. `requestTimeoutInMs` + `retryTimes` on connect failures is slow; keep retries small on retailer HTML.

Factory job-create URL pattern: `${actionManagerBaseUrl}/v1/actions/${actionId}/jobs`. Process() default `actionManagerBaseUrl` is `http://localhost:8082/action-manager-backend` (wrong inside Docker unless job/action config overrides). Notifier: `http://spring-kafka-notifier:8080/spring-kafka-notifier/api/v1/notifier-configurations`.

## Lazada parse

1. Parse `-i{itemId}-s{skuId}` from the PDP URL.
2. GET `https://m.lazada.vn/products/i{itemId}-s{skuId}.html` with a mobile UA.
3. Extract JSON after `__moduleData__ = `, also try `__moduleData__=` and `window.__moduleData__`.
4. SKU: `skuInfos[skuId]` or nested `data.root.fields.skuInfos`.
5. **Use `sku.price.salePrice.value` only if present.** If missing, do not treat desktop HTML as success.
6. Captcha/punish: body has `"action":"captcha"` or `/_____tmd_____/punish` — `JobResult` message, no throw.
7. Title/color for job names: read `getFields(moduleData)` (`skuInfos`/`product` at root **or** `data.root.fields`). Missing title produced `lazada-lazada-product-item-{skuId}-price-monitor`.

`stock: 0` with `quantity.type == "default"` and `limit.max > 0` is **not** OOS. OOS is `max == 0` or `type == "warning"`.

## Hasaki parse (existing)

Factory GET `https://hasaki.vn/mobile/v3/detail/product?product_id=${productId}&...` then JSON `document.data.blocks[0].common_data`. Keep that shape for Hasaki; do not force Lazada HTML onto Hasaki.

## JobResult vs metrics

`new JobResult(price)` → METRIC tag value is that number (Kafka topic `job-manager-for-{jobName}`).

`new JobResult(null, message)` sets `exception`; persistence jobs mark FAILURE **after** `processOutputTargets`. Dry-run still 204. Prefer exception field for poller parse/HTTP failure so metrics are not `"null"` strings. Factory may return `new JobResult("human string")` on CONSOLE-only.

## Sync checklist

When changing Lazada poller behavior:

- [ ] Existing poller `jobContent` in `action-manager.jobs`
- [ ] `buildPollerContent` inside factory `jobContent`
- [ ] Same text in `template-manager` `templateText`
- [ ] Dry-run poller: log `data=` numeric or `-1`
- [ ] Dry-run factory: creates job+notifier **or** explicit `JobResult` string/exception — no Graal throw
