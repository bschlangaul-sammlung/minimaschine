/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt.event;

import com.apple.eawt.event.GesturePhaseEvent;
import com.apple.eawt.event.GesturePhaseListener;
import com.apple.eawt.event.GestureUtilities;
import com.apple.eawt.event.MagnificationEvent;
import com.apple.eawt.event.MagnificationListener;
import com.apple.eawt.event.RotationEvent;
import com.apple.eawt.event.RotationListener;
import com.apple.eawt.event.SwipeEvent;
import com.apple.eawt.event.SwipeListener;

public abstract class GestureAdapter
implements GesturePhaseListener,
MagnificationListener,
RotationListener,
SwipeListener {
    public void gestureBegan(GesturePhaseEvent gesturePhaseEvent) {
        GestureUtilities.unimplemented();
    }

    public void gestureEnded(GesturePhaseEvent gesturePhaseEvent) {
        GestureUtilities.unimplemented();
    }

    public void magnify(MagnificationEvent magnificationEvent) {
        GestureUtilities.unimplemented();
    }

    public void rotate(RotationEvent rotationEvent) {
        GestureUtilities.unimplemented();
    }

    public void swipedDown(SwipeEvent swipeEvent) {
        GestureUtilities.unimplemented();
    }

    public void swipedLeft(SwipeEvent swipeEvent) {
        GestureUtilities.unimplemented();
    }

    public void swipedRight(SwipeEvent swipeEvent) {
        GestureUtilities.unimplemented();
    }

    public void swipedUp(SwipeEvent swipeEvent) {
        GestureUtilities.unimplemented();
    }
}

