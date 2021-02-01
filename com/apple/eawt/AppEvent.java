/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import com.apple.eawt.Application;
import java.io.File;
import java.net.URI;
import java.util.EventObject;
import java.util.List;

public abstract class AppEvent
extends EventObject {
    AppEvent() {
        super(Application.getApplication());
    }

    public static class SystemSleepEvent
    extends AppEvent {
        SystemSleepEvent() {
        }
    }

    public static class ScreenSleepEvent
    extends AppEvent {
        ScreenSleepEvent() {
        }
    }

    public static class UserSessionEvent
    extends AppEvent {
        UserSessionEvent() {
        }
    }

    public static class AppHiddenEvent
    extends AppEvent {
        AppHiddenEvent() {
        }
    }

    public static class AppForegroundEvent
    extends AppEvent {
        AppForegroundEvent() {
        }
    }

    public static class AppReOpenedEvent
    extends AppEvent {
        AppReOpenedEvent() {
        }
    }

    public static class QuitEvent
    extends AppEvent {
        QuitEvent() {
        }
    }

    public static class PreferencesEvent
    extends AppEvent {
        PreferencesEvent() {
        }
    }

    public static class AboutEvent
    extends AppEvent {
        AboutEvent() {
        }
    }

    public static class OpenURIEvent
    extends AppEvent {
        public URI getURI() {
            throw Application.unimplemented();
        }
    }

    public static class PrintFilesEvent
    extends FilesEvent {
    }

    public static class OpenFilesEvent
    extends FilesEvent {
        public String getSearchTerm() {
            throw Application.unimplemented();
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static abstract class FilesEvent
    extends AppEvent {
        public List<File> getFiles() {
            throw Application.unimplemented();
        }
    }
}

