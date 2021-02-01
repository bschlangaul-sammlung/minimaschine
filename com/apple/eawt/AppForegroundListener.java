/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import com.apple.eawt.AppEvent;
import com.apple.eawt.AppEventListener;

public interface AppForegroundListener
extends AppEventListener {
    public void appRaisedToForeground(AppEvent.AppForegroundEvent var1);

    public void appMovedToBackground(AppEvent.AppForegroundEvent var1);
}

