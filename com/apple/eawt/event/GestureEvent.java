/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt.event;

import com.apple.eawt.event.GestureUtilities;

public abstract class GestureEvent {
    GestureEvent() {
        GestureUtilities.unimplemented();
    }

    public void consume() {
        GestureUtilities.unimplemented();
    }

    protected boolean isConsumed() {
        GestureUtilities.unimplemented();
        return false;
    }
}

