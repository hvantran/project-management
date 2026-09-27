---
name: creating-http-request-monitor-poller
description: >-
  Use when creating, updating, or dry-running Action Manager http-request-monitor
  jobs; Lazada or Hasaki price pollers; factory templates that embed poller
  jobContent; Mongo GraalJS JobResult scripts; Slack Kafka notifier $gt/$lte
  rules; or UI Dry run that returns 204 while backend logs PolyglotException.
---

# Creating http-request-monitor poller jobs

Poller scripts live in **Mongo**, not git. Java/image rebuild is only for `HttpClientService` / `OutboundRequestAuth` changes.

## Kinds

| Kind | Role | `outputTargets` | Scheduled |
|------|------|-----------------|-----------|
| Factory | One-shot: parse product, POST poller job + notifier | `CONSOLE` | no |
| Poller | Recurring numeric metric for Slack | `CONSOLE`, `METRIC` | yes |

Both hang off action `http-request-monitor`. Hasaki factory uses JSON API. Lazada factory/poller parse mobile HTML `__moduleData__` (new kind). Copy Hasaki HTTP+JWT pattern for **internal** POSTs only.

## Workflow

1. Confirm action `http-request-monitor` and whether the user wants a **factory** (new SKU later) or a **single poller**.
2. Probe the product from **host and** `action-manager-backend` (Docker IPv6 to Lazada often `ConnectException`). Record status, body length, captcha/`__moduleData__`, and the **sale** field — not list/`pdt_price`.
3. Write GraalJS `execute()` that returns `new JobResult(number)` or OOS `-1`. Parse/HTTP/SKU misses: `new JobResult(null, "clear message")`. **Never `throw`.**
4. If a factory exists, keep `buildPollerContent()` identical to the poller. Update factory job **and** Template Manager template.
5. Write Mongo (`jobContent` / `templateText`). Do not commit secrets. Do not put Bearer on `lazada.vn` / `hasaki.vn`.
6. Dry-run in the UI. Success toast is HTTP 204 only. Proof is backend `Job result: JobResult(data=..., exception=...)` — not `PolyglotException`.

Details: [reference.md](reference.md)

## Contract

- **Sale price** from mobile `sku.price.salePrice.value`. Desktop `skuInfos` often has **no** `price`.
- **OOS** `-1` when `quantity.limit.max == 0` or `quantity.type == "warning"` (not `stock`).
- Slack `$and`: `$gt 0` and `$lte` threshold so `-1` never alerts.
- Factory Freemarker: `${productUrl}` etc. in configs; `<#noparse>${value}</#noparse>` in Slack message.
- `OutboundRequestAuth` already skips public hosts. Do not add `Authorization` on retailer GETs.
- Catch HTTP connect/timeout in script. `retryTimes` wraps `ConnectException` as `AppException` and retries.

## Do not

| Excuse | Reality |
|--------|---------|
| Throw so failures are loud | Graal `throw` is `PolyglotException`; looks like a platform bug |
| Desktop fallback always | Desktop can parse SKU and still miss `salePrice` |
| Use `tracking.pdt_price` | List price, not sale |
| JWT on retailer GET | Token is for Action Manager / notifier only |
| Trust Dry-run toast | `processNonePersistenceJob` swallows exceptions; always 204 |
| Edit poller, skip factory embed | New SKUs get the old `buildPollerContent` |

## Red flags

- Job name `lazada-lazada-product-item-*` (title read from wrong JSON shape)
- Log line `An exception occurred while processing` plus `PolyglotException`
- Metric string that is not a number or `-1`
- Slack webhook or JWT in script logs (job `configurations` are logged on run)
