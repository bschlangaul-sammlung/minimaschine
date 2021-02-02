/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import com.apple.eawt.Application;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;

public abstract class CocoaComponent
extends Canvas {
    public abstract int createNSView();

    public void update(Graphics graphics) {
        throw Application.unimplemented();
    }

    public void paint(Graphics graphics) {
        throw Application.unimplemented();
    }

    public long createNSViewLong() {
        throw Application.unimplemented();
    }

    public final void sendMessage(int n, Object object) {
        throw Application.unimplemented();
    }

    public abstract Dimension getMaximumSize();

    public abstract Dimension getMinimumSize();

    public abstract Dimension getPreferredSize();
}

