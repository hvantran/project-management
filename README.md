# Project-management

## CI Quality gateway
### Project CI
[![project man](https://github.com/hvantran/project-management/actions/workflows/workflow.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/workflow.yaml)
[![action-manager-backend](https://github.com/hvantran/project-management/actions/workflows/action-manager-backend-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/action-manager-backend-ci.yaml)
[![action-manager-ui](https://github.com/hvantran/project-management/actions/workflows/action-manager-ui-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/action-manager-ui-ci.yaml)
[![ecommerce-stats-app](https://github.com/hvantran/project-management/actions/workflows/ecommerce-stats-app-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/ecommerce-stats-app-ci.yaml)
[![endpoint-collector-backend](https://github.com/hvantran/project-management/actions/workflows/endpoint-collector-backend-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/endpoint-collector-backend-ci.yaml)
[![endpoint-collector-ui](https://github.com/hvantran/project-management/actions/workflows/endpoint-collector-ui-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/endpoint-collector-ui-ci.yaml)
[![template-manager-backend](https://github.com/hvantran/project-management/actions/workflows/template-manager-backend-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/template-manager-backend-ci.yaml)
[![template-manager-ui](https://github.com/hvantran/project-management/actions/workflows/template-manager-ui-ci.yaml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/template-manager-ui-ci.yaml)
[![[ecommerce-stats-app notify](https://github.com/hvantran/ecommerce-stats-app/actions/workflows/notify-parent-ci.yaml/badge.svg)](https://github.com/hvantran/ecommerce-stats-app/actions/workflows/notify-parent-ci.yaml)
[![action-manager-app notify](https://github.com/hvantran/template-management-app/actions/workflows/notify-parent-ci.yaml/badge.svg)](https://github.com/hvantran/template-management-app/actions/workflows/notify-parent-ci.yaml)
### Code quality CI
[![code quality with codeql](https://github.com/hvantran/project-management/actions/workflows/code-security-and-analysis-with-codeql.yml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/code-security-and-analysis-with-codeql.yml)
# Architecture
<TBD>

# User guide

## 1. Action Manager

### 1.1 Technical architechture

### 1.2 Overview

### 1.3 User cases

#### 1.3.1 Actions

##### a. Create actions

##### b. Archive actions

##### c. Restore actions

##### d. Export actions

##### e. Import actions

##### f. Delete actions

##### g. View actions

##### h. Set farvorite actions

##### i. View action details

##### j. Add new jobs to an existing action

### 1.3.2 Jobs

##### a. Create schedule/one-time jobs

##### b. Update jobs

##### c. Delete jobs

##### d. Dry run jobs

##### e. Pause jobs

##### f. Resume jobs

##### g. View jobs

##### h. View job details



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
     * The summary of the method.
     * It supports the display parameters of the execution method and also supports getting the property of the object by using getter methods.
     * Syntax: {argument<index>[.<getter method name>()]}
     * Example:
     * "Hello {argument0}" on method with the first argument is Nick. The result is "Hello Nick"
     * "Hello {argument0.getMessage()}" on method with the first argument is User("Nick"). The result is "Hello Nick"
     * @return
     */
    String description() default "";
}
```

### 1.3 Usage
```

    @Override
    @LoggingMonitor(description = "Archive action by id: {argument0}")
    public void archive(String actionId) {
        <code here>
    }

    @Override
    @LoggingMonitor(description = "Update action: {argument0.getActionName()}")
    public void update(ActionDefinition actionDefinitionVO) {
        <code here>
    }
```

## 2. Metric m


