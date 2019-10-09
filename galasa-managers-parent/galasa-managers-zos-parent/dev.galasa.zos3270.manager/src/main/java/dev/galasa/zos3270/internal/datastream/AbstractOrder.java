/*
 * Copyright (c) 2019 IBM Corporation.
 */
package dev.galasa.zos3270.internal.datastream;

public abstract class AbstractOrder {

    @Override
    public String toString() {
        return "Ignore SQ";
    }

    public abstract byte[] getBytes();

}