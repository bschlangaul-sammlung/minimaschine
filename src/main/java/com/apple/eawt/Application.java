/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEventListener;
import com.apple.eawt.ApplicationListener;
import com.apple.eawt.OpenFilesHandler;
import com.apple.eawt.OpenURIHandler;
import com.apple.eawt.PreferencesHandler;
import com.apple.eawt.PrintFilesHandler;
import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitStrategy;
import java.awt.Image;
import java.awt.Point;
import java.awt.PopupMenu;
import javax.swing.JMenuBar;

public class Application {
    static RuntimeException unimplemented() {
        return new RuntimeException("Unimplemented");
    }

    public static Application getApplication() {
        throw Application.unimplemented();
    }

    @Deprecated
    public Application() {
        throw Application.unimplemented();
    }

    public void addAppEventListener(AppEventListener appEventListener) {
        throw Application.unimplemented();
    }

    public void removeAppEventListener(AppEventListener appEventListener) {
        throw Application.unimplemented();
    }

    public void setAboutHandler(AboutHandler aboutHandler) {
        throw Application.unimplemented();
    }

    public void setPreferencesHandler(PreferencesHandler preferencesHandler) {
        throw Application.unimplemented();
    }

    public void setOpenFileHandler(OpenFilesHandler openFilesHandler) {
        throw Application.unimplemented();
    }

    public void setPrintFileHandler(PrintFilesHandler printFilesHandler) {
        throw Application.unimplemented();
    }

    public void setOpenURIHandler(OpenURIHandler openURIHandler) {
        throw Application.unimplemented();
    }

    public void setQuitHandler(QuitHandler quitHandler) {
        throw Application.unimplemented();
    }

    public void setQuitStrategy(QuitStrategy quitStrategy) {
        throw Application.unimplemented();
    }

    public void enableSuddenTermination() {
        throw Application.unimplemented();
    }

    public void disableSuddenTermination() {
        throw Application.unimplemented();
    }

    public void requestForeground(boolean bl) {
        throw Application.unimplemented();
    }

    public void requestUserAttention(boolean bl) {
        throw Application.unimplemented();
    }

    public void openHelpViewer() {
        throw Application.unimplemented();
    }

    public void setDockMenu(PopupMenu popupMenu) {
        throw Application.unimplemented();
    }

    public PopupMenu getDockMenu() {
        throw Application.unimplemented();
    }

    public void setDockIconImage(Image image) {
        throw Application.unimplemented();
    }

    public Image getDockIconImage() {
        throw Application.unimplemented();
    }

    public void setDockIconBadge(String string) {
        throw Application.unimplemented();
    }

    public void setDefaultMenuBar(JMenuBar jMenuBar) {
        throw Application.unimplemented();
    }

    @Deprecated
    public void addApplicationListener(ApplicationListener applicationListener) {
        throw Application.unimplemented();
    }

    @Deprecated
    public void removeApplicationListener(ApplicationListener applicationListener) {
        throw Application.unimplemented();
    }

    @Deprecated
    public void setEnabledPreferencesMenu(boolean bl) {
        throw Application.unimplemented();
    }

    @Deprecated
    public void setEnabledAboutMenu(boolean bl) {
        throw Application.unimplemented();
    }

    @Deprecated
    public boolean getEnabledPreferencesMenu() {
        throw Application.unimplemented();
    }

    @Deprecated
    public boolean getEnabledAboutMenu() {
        throw Application.unimplemented();
    }

    @Deprecated
    public boolean isAboutMenuItemPresent() {
        throw Application.unimplemented();
    }

    @Deprecated
    public void addAboutMenuItem() {
        throw Application.unimplemented();
    }

    @Deprecated
    public void removeAboutMenuItem() {
        throw Application.unimplemented();
    }

    @Deprecated
    public boolean isPreferencesMenuItemPresent() {
        throw Application.unimplemented();
    }

    @Deprecated
    public void addPreferencesMenuItem() {
        throw Application.unimplemented();
    }

    @Deprecated
    public void removePreferencesMenuItem() {
        throw Application.unimplemented();
    }

    @Deprecated
    public static Point getMouseLocationOnScreen() {
        throw Application.unimplemented();
    }
}

