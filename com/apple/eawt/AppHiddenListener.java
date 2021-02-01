/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import com.apple.eawt.AppEvent;
import com.apple.eawt.AppEventListener;

public interface AppHiddenListener
extends AppEventListener {
    public void appHidden(AppEvent.AppHiddenEvent var1);

    public void appUnhidden(AppEvent.AppHiddenEvent var1);
}

