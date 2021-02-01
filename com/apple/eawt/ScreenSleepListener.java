/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import com.apple.eawt.AppEvent;
import com.apple.eawt.AppEventListener;

public interface ScreenSleepListener
extends AppEventListener {
    public void screenAboutToSleep(AppEvent.ScreenSleepEvent var1);

    public void screenAwoke(AppEvent.ScreenSleepEvent var1);
}

