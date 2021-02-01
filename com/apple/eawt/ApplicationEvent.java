/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import com.apple.eawt.Application;
import java.util.EventObject;

@Deprecated
public class ApplicationEvent
extends EventObject {
    ApplicationEvent(Object object) {
        super(object);
        throw Application.unimplemented();
    }

    ApplicationEvent(Object object, String string) {
        super(object);
        throw Application.unimplemented();
    }

    @Deprecated
    public boolean isHandled() {
        throw Application.unimplemented();
    }

    @Deprecated
    public void setHandled(boolean bl) {
        throw Application.unimplemented();
    }

    @Deprecated
    public String getFilename() {
        throw Application.unimplemented();
    }
}

