/*
 * Copyright (c) 2014-2016 Red Hat, Inc. and/or its affiliates.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.jberet.samples.wildfly.deserialization;

import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.chunk.AbstractItemReader;
import jakarta.batch.runtime.context.StepContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
public class ItemReader1 extends AbstractItemReader {
    @Inject
    StepContext stepContext;

    @Inject
    @BatchProperty(name = "fail.on")
    int failOn;

    @Inject
    @BatchProperty(name = "number.limit")
    int numberLimit;

    int currentNumber;

    @Override
    public Object readItem() throws Exception {
        if (++currentNumber >= numberLimit) {
            return null;
        }
        stepContext.setPersistentUserData(new org.jberet.samples.wildfly.deserialization.Data1("Current number: " + currentNumber));
        if (currentNumber == failOn) {
            throw new ArithmeticException("currentNumber matches fail.on number: " + currentNumber);
        }
        return currentNumber;
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return new org.jberet.samples.wildfly.deserialization.CheckpointInfo1(currentNumber);
    }

    @Override
    public void open(final Serializable checkpoint) throws Exception {
        super.open(checkpoint);
        System.out.printf("Reader open with checkpoint %s%n", checkpoint);
        if(checkpoint!=null) {
            currentNumber = ((org.jberet.samples.wildfly.deserialization.CheckpointInfo1) checkpoint).getNumber();
        }
    }
}
