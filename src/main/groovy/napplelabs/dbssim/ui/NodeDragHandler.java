/*
	MockDBS: Deep Brain Stimulation Simulator
    Copyright (C) 2009 Zachary Kim

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package napplelabs.dbssim.ui;

import napplelabs.dbssim.canvasviews.NeuronPath;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

class NodeDragHandler extends PDragSequenceEventHandler {
	
	private CanvasPanel cp;
	
	public NodeDragHandler(CanvasPanel cp) {
		this.cp = cp;
		getEventFilter().setMarksAcceptedEventsAsHandled(true);
	}
	
	public void mouseEntered(PInputEvent e) {
		if (e.getButton() == 0) {
			//e.getPickedNode().setPaint(Color.red);
		}
	}
	
	public void mouseExited(PInputEvent e) {
		if (e.getButton() == 0) {
			//e.getPickedNode().setPaint(Color.white);
		}
	}
	
	public void drag(PInputEvent e) {
		PNode node = e.getPickedNode();
		PNode n = node;
		while(true) {
			n = n.getParent();
			if(n instanceof NeuronPath) break;
			if(n == null) break;
		}
		
		if(n == null) return;
		//if (probe.children.contains(node)) return
		n.translate(e.getDelta().width, e.getDelta().height);
		
		cp.recalcDistances();
		cp.persist();
	}
}