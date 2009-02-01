/*
 * Copyright (c) 2008, Piccolo2D project, http://piccolo2d.org
 * Copyright (c) 1998-2008, University of Maryland
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 *
 * None of the name of the University of Maryland, the name of the Piccolo2D project, or the names of its
 * contributors may be used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package napplelabs.dbssim.ui;

import java.awt.event.InputEvent;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.event.PZoomEventHandler;

/**
 * <b>ZoomEventhandler</b> provides event handlers for basic zooming of the
 * canvas view with the right (third) button. The interaction is that the
 * initial mouse press defines the zoom anchor point, and then moving the mouse
 * to the right zooms with a speed proportional to the amount the mouse is moved
 * to the right of the anchor point. Similarly, if the mouse is moved to the
 * left, the the view is zoomed out.
 * <P>
 * On a Mac with its single mouse button one may wish to change the standard
 * right mouse button zooming behavior. This can be easily done with the
 * PInputEventFilter. For example to zoom with button one and shift you would do
 * this:
 * <P>
 * <code>
 * <pre>
 * zoomEventHandler.getEventFilter().setAndMask(InputEvent.BUTTON1_MASK | 
 *                                              InputEvent.SHIFT_MASK);
 * </pre>
 * </code>
 * <P>
 * 
 * @version 1.0
 * @author Jesse Grosjean
 */
public class NapPZoomEventHandler extends PZoomEventHandler {

    private double minScale = 0;
    private double maxScale = Double.MAX_VALUE;

    /**
     * Creates a new zoom handler.
     */
    public NapPZoomEventHandler() {
        super();
        //setEventFilter(new PInputEventFilter(InputEvent.));
    }

    // ****************************************************************
    // Zooming
    // ****************************************************************

    /**
     * Returns the minimum view magnification factor that this event handler is
     * bound by. The default is 0.
     * 
     * @return the minimum camera view scale
     */
    public double getMinScale() {
        return minScale;
    }

    /**
     * Sets the minimum view magnification factor that this event handler is
     * bound by. The camera is left at its current scale even if
     * <code>minScale</code> is larger than the current scale.
     * 
     * @param minScale the minimum scale, must not be negative.
     */
    public void setMinScale(double minScale) {
        this.minScale = minScale;
    }

    /**
     * Returns the maximum view magnification factor that this event handler is
     * bound by. The default is Double.MAX_VALUE.
     * 
     * @return the maximum camera view scale
     */
    public double getMaxScale() {
        return maxScale;
    }

    /**
     * Sets the maximum view magnification factor that this event handler is
     * bound by. The camera is left at its current scale even if
     * <code>maxScale</code> is smaller than the current scale. Use
     * Double.MAX_VALUE to specify the largest possible scale.
     * 
     * @param maxScale the maximum scale, must not be negative.
     */
    public void setMaxScale(double maxScale) {
        this.maxScale = maxScale;
    }

    protected void dragActivityFirstStep(PInputEvent aEvent) {
        
    }

    protected void dragActivityStep(PInputEvent aEvent) {
        
    }
    
    public void processEvent(final PInputEvent evt, final int i) {
		if (evt.isMouseWheelEvent()) {
			
			Point2D viewZoomPoint = evt.getPosition();
			
			PCamera camera = evt.getCamera();
			double dx = - evt.getWheelRotation();
			
	        double scaleDelta = (1.0 + (0.05 * dx));

	        double currentScale = camera.getViewScale();
	        double newScale = currentScale * scaleDelta;

	        if (newScale < minScale) {
	            scaleDelta = minScale / currentScale;
	        }
	        if ((maxScale > 0) && (newScale > maxScale)) {
	            scaleDelta = maxScale / currentScale;
	        }

	        camera.scaleViewAboutPoint(scaleDelta, viewZoomPoint.getX(), viewZoomPoint.getY());
		}
	}

    // ****************************************************************
    // Debugging - methods for debugging
    // ****************************************************************

}
