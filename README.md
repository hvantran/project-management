

<!-- TOC end -->

<!-- TOC --><a name="project-management"></a>
# Project-management

<!-- TOC --><a name="ci-quality-gateway"></a>
## CI Quality gateway
<!-- TOC --><a name="project-ci"></a>
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
<!-- TOC --><a name="code-quality-ci"></a>
### Code quality CI
[![code quality with codeql](https://github.com/hvantran/project-management/actions/workflows/code-security-and-analysis-with-codeql.yml/badge.svg)](https://github.com/hvantran/project-management/actions/workflows/code-security-and-analysis-with-codeql.yml)

# Table of content
- [Project-management](#project-management)
  * [CI Quality gateway](#ci-quality-gateway)
    + [Project CI](#project-ci)
    + [Code quality CI](#code-quality-ci)
- [Architecture](#architecture)
- [Projects Overview](#projects-overview)
  * [1. Action Manager](#1-action-manager)
    + [1.1 Technical architechture](#11-technical-architechture)
    + [1.2 Introduction](#12-introduction)
    + [1.3 User cases](#13-user-cases)
      - [1.3.1 Actions](#131-actions)
    + [1.3.2 Jobs](#132-jobs)
  * [2. Template Manager](#2-template-manager)
    + [2.1 Technical architechture](#21-technical-architechture)
    + [2.2 Introduction](#22-introduction)
    + [2.3 User cases](#23-user-cases)
      - [2.3.1 Templates](#231-templates)
    + [2.3.2 Reports](#232-reports)
- [Technical implementation notes](#technical-implementation-notes)
  * [1. Smart logging with LoggingMonitor](#1-smart-logging-with-loggingmonitor)
    + [1.1 Situation](#11-situation)
    + [1.2 Resolution](#12-resolution)
    + [1.3 Usage](#13-usage)
  * [2. Metric m](#2-metric-m)

<!-- TOC --><a name="architecture"></a>
# Architecture
<TBD>

<!-- TOC --><a name="projects-overview"></a>
# Projects Overview

<!-- TOC --><a name="1-action-manager"></a>
## 1. Action Manager

<!-- TOC --><a name="11-technical-architechture"></a>
### 1.1 Technical architechture

![action-manager-architechture (2)](https://github.com/hvantran/project-management/assets/17746024/092bbd23-82e7-4cb7-8fe4-354100fe111c)

<!-- TOC --><a name="12-introduction"></a>
### 1.2 Introduction

I had the idea for an "Action Manager Service" to fulfill my own needs. Specifically, I wanted the ability to complete tasks using Java programming language and receive notifications or emails with the results. The service allows for tasks to be run immediately without the need for compilation and also supports the scheduling of both one-time and recurring jobs.


**Github repo URL**: https://github.com/hvantran/action-manager-app

<!-- TOC --><a name="13-user-cases"></a>
### 1.3 User cases

<!-- TOC --><a name="131-actions"></a>
#### 1.3.1 Actions

![image](https://github.com/hvantran/project-management/assets/17746024/f09ab4ff-6acf-4055-8643-302545607b8d)

<!-- TOC --><a name="132-jobs"></a>
### 1.3.2 Jobs

![image](https://github.com/hvantran/project-management/assets/17746024/e8963478-1da0-4eda-bf40-f635ecb6fd93)


<!-- TOC --><a name="2-template-manager"></a>
## 2. Template Manager

<!-- TOC --><a name="21-technical-architechture"></a>
### 2.1 Technical architechture

![template-manager-architechture](https://github.com/hvantran/project-management/assets/17746024/a992af47-db1b-4a32-80a0-3019cee083aa)

<!-- TOC --><a name="22-introduction"></a>
### 2.2 Introduction

I work on tasks day by day, but there are some tasks repeatedly, and I don't want to spend too much time working on these tasks, it will reduce development time

The first idea is **Template + Data = Output**

**Github repo URL**: https://github.com/hvantran/template-management-app

<!-- TOC --><a name="23-user-cases"></a>
### 2.3 User cases

<!-- TOC --><a name="231-templates"></a>
#### 2.3.1 Templates

![image](https://github.com/hvantran/project-management/assets/17746024/009b342a-c748-49b2-bdb0-0f1b1cc736a9)


<!-- TOC --><a name="232-reports"></a>
### 2.3.2 Reports

![image](https://github.com/hvantran/project-management/assets/17746024/79d9674d-2db8-4653-ab30-bdc92c29d6a8)




<!-- TOC --><a name="technical-implementation-notes"></a>
# Technical implementation notes
  
<!-- TOC --><a name="1-smart-logging-with-loggingmonitor"></a>
## 1. Smart logging with LoggingMonitor

<!-- TOC --><a name="11-situation"></a>
### 1.1 Situation

For user actions from the front end, the backend will call a chain of functions, and to monitor these functions the developers must add more logs to track the execution flow. It is the common case for all user actions, we need to figure out a way to do this without specific in the loggers. 

<!-- TOC --><a name="12-resolution"></a>
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

<!-- TOC --><a name="13-usage"></a>
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

<!-- TOC --><a name="2-metric-m"></a>
## 2. Metric m

