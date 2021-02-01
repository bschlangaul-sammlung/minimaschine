/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import com.apple.eawt.AppEvent;
import com.apple.eawt.QuitResponse;

public interface QuitHandler {
    public void handleQuitRequestWith(AppEvent.QuitEvent var1, QuitResponse var2);
}

