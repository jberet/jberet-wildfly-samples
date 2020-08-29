# Overview

This is a sample webapp that demonstrate batch processing with batchlet, ejb singleton bean, timer and conditional 
step transition. The batch job consists of 3 steps:

1. step1: `Batchlet1`
    * during its processing, check an external condition to see if this batchelt should be stopped, without stopping
    the step and job execution.
    * if the above condition indicates `stop`, then continue to run `step3`
    * else continue to `step2`
2. step2: `Batchlet23`
    * normal execution from `step`
3. step3: `Batchlet23`
    * either from `step1` due to `stop` condition, or from `step2` as normal execution.

## How to Build

    mvn clean install

## How to Run Tests

1. Start JBoss EAP or WildFly server:
    
    ```
    $JBOSS_HOME/bin/standalone.sh
    ```
  
2. Deploy the app to start the batch job executions:

   ```
   mvn wildfly:deploy
   ```
After the application is deployed and initialized, the timer in `BatchletSingletonBean` will periodically start the
batch job `batchlets.xml`.

```
12:21:20,779 INFO  [io.smallrye.metrics] (MSC service thread 1-4) MicroProfile: Metrics activated (SmallRye Metrics version: 2.4.2)
12:21:20,936 INFO  [org.wildfly.extension.undertow] (ServerService Thread Pool -- 94) WFLYUT0021: Registered web context: '/batchlet-singleton' for server 'default-server'
12:21:20,953 INFO  [org.jboss.as.server] (management-handler-thread - 1) WFLYSRV0010: Deployed "batchlet-singleton.war" (runtime-name : "batchlet-singleton.war")
12:21:30,008 INFO  [org.jberet.samples.wildfly.batchletsingleton] (EJB default - 1) Starting job execution: 12
12:21:30,019 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 2) Batchlet process() of batchlets.step1, exit status: COMPLETE
12:21:30,020 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 2) Batchlet process() of batchlets.step2
12:21:30,021 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 2) Batchlet process() of batchlets.step3
12:22:00,006 INFO  [org.jberet.samples.wildfly.batchletsingleton] (EJB default - 1) Starting job execution: 13
12:22:00,008 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 3) Batchlet process() of batchlets.step1, exit status: COMPLETE
12:22:00,008 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 3) Batchlet process() of batchlets.step2
12:22:00,009 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 3) Batchlet process() of batchlets.step3
12:22:30,008 INFO  [org.jberet.samples.wildfly.batchletsingleton] (EJB default - 1) Starting job execution: 14
12:22:30,012 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 4) Batchlet process() of batchlets.step1, exit status: COMPLETE
12:22:30,012 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 4) Batchlet process() of batchlets.step2
12:22:30,012 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 4) Batchlet process() of batchlets.step3
12:23:00,011 INFO  [org.jberet.samples.wildfly.batchletsingleton] (EJB default - 1) Starting job execution: 15
12:23:00,012 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 5) Batchlet process() of batchlets.step1, exit status: COMPLETE
12:23:00,013 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 5) Batchlet process() of batchlets.step2
12:23:00,014 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 5) Batchlet process() of batchlets.step3
12:23:30,006 INFO  [org.jberet.samples.wildfly.batchletsingleton] (EJB default - 1) Starting job execution: 16
12:23:30,007 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 6) Batchlet process() of batchlets.step1, exit status: COMPLETE
12:23:30,008 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 6) Batchlet process() of batchlets.step2
12:23:30,008 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 6) Batchlet process() of batchlets.step3
12:24:00,009 INFO  [org.jberet.samples.wildfly.batchletsingleton] (EJB default - 1) Starting job execution: 17
12:24:00,010 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 7) Batchlet process() of batchlets.step1, exit status: STOP
12:24:00,010 INFO  [org.jberet.samples.wildfly.batchletsingleton] (Batch Thread - 7) Batchlet process() of batchlets.step3
12:24:21,392 INFO  [org.wildfly.extension.undertow] (ServerService Thread Pool -- 94) WFLYUT0022: Unregistered web context: '/batchlet-singleton' from server 'default-server'
12:24:21,440 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-1) WFLYSRV0028: Stopped deployment batchlet-singleton.war (runtime-name: batchlet-singleton.war) in 49ms
```

3. Undeploy application:

    ```
    mvn wildfly:undeploy
    ```
