/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import com.apple.eawt.AppEvent;
import com.apple.eawt.AppEventListener;

public interface UserSessionListener
extends AppEventListener {
    public void userSessionDeactivated(AppEvent.UserSessionEvent var1);

    public void userSessionActivated(AppEvent.UserSessionEvent var1);
}

