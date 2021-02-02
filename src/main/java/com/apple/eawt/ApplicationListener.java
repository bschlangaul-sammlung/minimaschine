/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import com.apple.eawt.ApplicationEvent;
import java.util.EventListener;

@Deprecated
public interface ApplicationListener
extends EventListener {
    @Deprecated
    public void handleAbout(ApplicationEvent var1);

    @Deprecated
    public void handleOpenApplication(ApplicationEvent var1);

    @Deprecated
    public void handleOpenFile(ApplicationEvent var1);

    @Deprecated
    public void handlePreferences(ApplicationEvent var1);

    @Deprecated
    public void handlePrintFile(ApplicationEvent var1);

    @Deprecated
    public void handleQuit(ApplicationEvent var1);

    @Deprecated
    public void handleReOpenApplication(ApplicationEvent var1);
}

