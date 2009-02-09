package napplelabs.dbssim.ui;

import napplelabs.dbssim.neuron.NeuronPath;
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