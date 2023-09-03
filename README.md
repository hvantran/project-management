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

# Projects Overview

## 1. Action Manager

### 1.1 Technical architechture

![image](https://github.com/hvantran/project-management/assets/17746024/eb5e4b1f-c936-40aa-920b-b281dd2c03d5)

CI/CD

![image](https://github.com/hvantran/project-management/assets/17746024/f7b687b4-aa69-4aec-bafd-c038f01b7482)


### 1.2 Introduction

The first idea of **action manager service** comes from my requirements. I want able to do jobs inside actions with Java programming language.

The jobs can able to run immediately without any compilation process, and it also supports scheduled and one-time jobs.

That is the reason why the action manager was born

**Github repo URL**: https://github.com/hvantran/action-manager-app

### 1.3 User cases

#### 1.3.1 Actions

![image](https://github.com/hvantran/project-management/assets/17746024/f09ab4ff-6acf-4055-8643-302545607b8d)

### 1.3.2 Jobs

![image](https://github.com/hvantran/project-management/assets/17746024/e8963478-1da0-4eda-bf40-f635ecb6fd93)


## 2. Template Manager

### 2.1 Technical architechture

![image](https://github.com/hvantran/project-management/assets/17746024/9c910f52-0dfa-4105-95d9-e234fd851e44)


CI/CD

![image](https://github.com/hvantran/project-management/assets/17746024/7d189b11-1bd6-422f-8ffb-5b46474e485c)


### 2.2 Introduction

I work on tasks day by day, but there are some tasks repeatedly, and I don't want to spend too much time working on these tasks, it will reduce development time

The first idea is **Template + Data = Output**

**Github repo URL**: https://github.com/hvantran/template-management-app

### 2.3 User cases

#### 2.3.1 Templates

![image](https://github.com/hvantran/project-management/assets/17746024/009b342a-c748-49b2-bdb0-0f1b1cc736a9)


### 2.3.2 Reports

![image](https://github.com/hvantran/project-management/assets/17746024/79d9674d-2db8-4653-ab30-bdc92c29d6a8)




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


