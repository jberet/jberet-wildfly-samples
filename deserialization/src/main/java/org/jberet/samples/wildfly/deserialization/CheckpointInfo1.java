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

import java.io.Serializable;

public final class CheckpointInfo1 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int number;

    public CheckpointInfo1(final int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(final int number) {
        this.number = number;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckpointInfo1)) return false;

        final CheckpointInfo1 that = (CheckpointInfo1) o;

        if (number != that.number) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckpointInfo1{");
        sb.append("number=").append(number);
        sb.append('}');
        return sb.toString();
    }
}
