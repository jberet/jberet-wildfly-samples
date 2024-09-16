/*
 * Copyright (c) 2016 Red Hat, Inc. and/or its affiliates.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.jberet.samples.wildfly.restreader;

import jakarta.batch.api.listener.StepListener;
import jakarta.batch.runtime.context.JobContext;
import jakarta.batch.runtime.context.StepContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Properties;

/**
 * This step listener class sets the step exception message as the
 * job exit status for test {@code testError}. The job exit status
 * is then verified in the test client.
 */
@Named
public class StepListener1 implements StepListener {
    @Inject
    private StepContext stepContext;

    @Inject
    private JobContext jobContext;

    @Override
    public void beforeStep() throws Exception {

    }

    @Override
    public void afterStep() throws Exception {
        final Exception exception = stepContext.getException();
        if (exception != null) {
            final Properties jobProperties = jobContext.getProperties();
            if (jobProperties != null) {
                final String testName = jobProperties.getProperty("testName");
                if ("testError".equals(testName)) {
                    final String message = exception.getMessage();
                    System.out.printf("For test method %s, step exception message: %s%n", testName, message);
                    //only save step exception message as exit status for test testError
                    jobContext.setExitStatus(message);
                }
            }
        }
    }

}
