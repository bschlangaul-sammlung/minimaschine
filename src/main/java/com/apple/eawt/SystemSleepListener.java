/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import com.apple.eawt.AppEvent;
import com.apple.eawt.AppEventListener;

public interface SystemSleepListener
extends AppEventListener {
    public void systemAboutToSleep(AppEvent.SystemSleepEvent var1);

    public void systemAwoke(AppEvent.SystemSleepEvent var1);
}

