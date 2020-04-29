/*
 * Copyright (c) 2020 Red Hat, Inc. and/or its affiliates.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.jberet.samples.wildfly.throttle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.jberet.runtime.JobExecutionImpl;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class ThrottleSingletonBean {
    private static final String JOB_NAME = "throttle";
    private static final int NUM_EXECUTIONS = 10;
    private static final long WAIT_MINUTES = 2;

    @PostConstruct
    private void postConstruct() {
        final JobOperator jobOperator = BatchRuntime.getJobOperator();
        runTest(jobOperator, NUM_EXECUTIONS, true);
    }

    /**
     * Starts concurrent job executions.
     *
     * @param jobOperator the job operator
     * @param count       number of times to start the test job
     * @param concurrent  a flag whether to start the test job serially or concurently
     */
    private void runTest(final JobOperator jobOperator, final int count, final boolean concurrent) {
        final List<Long> jobExecutionIds = new CopyOnWriteArrayList<Long>();

        if (concurrent) {
            final List<Thread> threads = new ArrayList<Thread>();
            for (int i = 0; i < count; i++) {
                final Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jobExecutionIds.add(jobOperator.start(JOB_NAME, null));
                    }
                });
                threads.add(t);
                t.start();
            }
            for (final Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        } else {
            for (int i = 0; i < count; ++i) {
                jobExecutionIds.add(jobOperator.start(JOB_NAME, null));
            }
        }

        for (final Long id : jobExecutionIds) {
            final JobExecutionImpl exe = (JobExecutionImpl) jobOperator.getJobExecution(id);
            try {
                exe.awaitTermination(WAIT_MINUTES, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                // ignore
            }
        }

        System.out.printf("JobExecutions %s %s%n", jobExecutionIds.size(), jobExecutionIds);
        for (final Long id : jobExecutionIds) {
            System.out.printf("JobExecution %s : %s%n", id, jobOperator.getJobExecution(id).getBatchStatus());
        }
    }
}
