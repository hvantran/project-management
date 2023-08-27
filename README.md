# Project-management

My project collection

[![project man](https://github.com/hvantran/project-management/actions/workflows/workflow.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/workflow.yaml)

[![action-manager-backend](https://github.com/hvantran/project-management/actions/workflows/action-manager-backend-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/action-manager-backend-ci.yaml)
[![action-manager-ui](https://github.com/hvantran/project-management/actions/workflows/action-manager-ui-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/action-manager-ui-ci.yaml)

[![ecommerce-stats-app](https://github.com/hvantran/project-management/actions/workflows/ecommerce-stats-app-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/ecommerce-stats-app-ci.yaml)

[![endpoint-collector-backend](https://github.com/hvantran/project-management/actions/workflows/endpoint-collector-backend-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/endpoint-collector-backend-ci.yaml)
[![endpoint-collector-ui](https://github.com/hvantran/project-management/actions/workflows/endpoint-collector-ui-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/endpoint-collector-ui-ci.yaml)

[![template-manager-backend](https://github.com/hvantran/project-management/actions/workflows/template-manager-backend-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/template-manager-backend-ci.yaml)
[![template-manager-ui](https://github.com/hvantran/project-management/actions/workflows/template-manager-ui-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/template-manager-ui-ci.yaml)

# Notify changes to parent tasks

[![[ecommerce-stats-app notify](https://github.com/hvantran/ecommerce-stats-app/actions/workflows/notify-parent-ci.yaml/badge.svg)](https://github.com/hvantran/ecommerce-stats-app/actions/workflows/notify-parent-ci.yaml)

[![action-manager-app notify](https://github.com/hvantran/template-management-app/actions/workflows/notify-parent-ci.yaml/badge.svg)](https://github.com/hvantran/template-management-app/actions/workflows/notify-parent-ci.yaml)

# Architecture
<TBD>

# Technical implementation notes
  
## 1. Smart logging with LoggingMonitor

### 1.1 Situation

For user actions from the front end, the backend will call a chain of functions, and to monitor these functions the developers must add more logs to track the execution flow. It is the common case for all user actions, we need to figure out a way to do this without specific in the loggers. 

### 1.2 Resolution

The **LoggerMonitor** is created to adapt these common cases.
```
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LoggingMonitor {

    /**
     * The summary of the method. It supports the display parameters of the execution method with ${argument<index>} syntax
     * Example:
     * "Hello ${argument0}" on method with the first argument is Nick. The result is "Hello Nick"
     * @return
     */
    String description() default "";
}
```

### 1.3 Usage
```

    @Override
    @LoggingMonitor(description = "Archive action by id: ${argument0}")
    public void archive(String actionId) {
        <code here>
    }
```



