# Overview

This is a sample webapp that verifies heavy concurrent batch job executions.
This app uses a Singleton EJB annotated with `@Startup`, which means its
post-construct method will be invoked when the server is starting up.
During its post-construct methods, multiple batch job executions are
started concurrently to verify concurrent batch job executions.

## How to Build

    mvn clean install

## How to Run Tests

1. Start JBoss EAP 7 or WildFly server:
    
    ```
    $JBOSS_HOME/bin/standalone.sh
    ```
  
2. Deploy the app to start the batch job executions:

   ```
   mvn wildfly:deploy
   ```

   During deployment, the start-up singleton bean's post-construct method
   will be invoked, which in turn starts concurrent executions of batch job.
   When all batch job executions complete and the app is deployed, you will
   see the following server output.

   If WildFly fails to run these concurrent batch job executions due to thread
   deadlock, the above mvn deploy command will hang till maven timeout, while
   WildFly suffers from thead deadlock.

```
12:26:00,483 INFO  [stdout] (ServerService Thread Pool -- 84) JobExecutions 10 [2, 3, 4, 5, 6, 1, 7, 10, 8, 9]
12:26:00,483 INFO  [stdout] (ServerService Thread Pool -- 84) JobExecution 2 : COMPLETED
12:26:00,484 INFO  [stdout] (ServerService Thread Pool -- 84) JobExecution 3 : COMPLETED
12:26:00,484 INFO  [stdout] (ServerService Thread Pool -- 84) JobExecution 4 : COMPLETED
12:26:00,484 INFO  [stdout] (ServerService Thread Pool -- 84) JobExecution 5 : COMPLETED
12:26:00,484 INFO  [stdout] (ServerService Thread Pool -- 84) JobExecution 6 : COMPLETED
12:26:00,484 INFO  [stdout] (ServerService Thread Pool -- 84) JobExecution 1 : COMPLETED
12:26:00,484 INFO  [stdout] (ServerService Thread Pool -- 84) JobExecution 7 : COMPLETED
12:26:00,485 INFO  [stdout] (ServerService Thread Pool -- 84) JobExecution 10 : COMPLETED
12:26:00,485 INFO  [stdout] (ServerService Thread Pool -- 84) JobExecution 8 : COMPLETED
12:26:00,485 INFO  [stdout] (ServerService Thread Pool -- 84) JobExecution 9 : COMPLETED
12:26:00,587 INFO  [org.wildfly.extension.undertow] (ServerService Thread Pool -- 84) WFLYUT0021: Registered web context: '/throttle' for server 'default-server'
12:26:00,657 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0010: Deployed "throttle.war" (runtime-name : "throttle.war")
```

3. Undeploy application:

    ```
    mvn wildfly:undeploy
    ```
