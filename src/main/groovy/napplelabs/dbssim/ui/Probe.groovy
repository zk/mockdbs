package napplelabs.dbssim.ui

import edu.umd.cs.piccolo.PNode
import java.awt.Color
import edu.umd.cs.piccolo.nodes.PPath
import napplelabs.dbssim.neuron.NeuronPathimport ddf.minim.Minimimport java.awt.BasicStrokeimport java.awt.geom.Point2Dimport edu.umd.cs.piccolo.nodes.PTextimport java.awt.Font

class Probe extends PNode {
	
	private double depth;
	
	public Probe() {
		init()
	}
	
	public void init() {
		PPath path = PPath.createLine(0, -1000, 0, 2000);
		//path.rotate(Math.PI) 
		path.stroke = new BasicStroke(3)
		path.paint = Color.lightGray
		path.strokePaint = Color.lightGray
		
		probe = PPath.createLine(0, 0, 0, -3000);
		
		probe.offset = new Point2D.Double(0, -2000) 
		probe.stroke = new BasicStroke(5)
		
		addChild(path)
		addChild(probe)
		
		(10..-20).each {int i->
			PText t = new PText(-i+"mm")
			t.font = new Font("Arial", Font.PLAIN, 16)
			t.offset = new Point2D.Double(30, i*100)
			t.textPaint = Color.lightGray
			addChild(t)
		}
		
	}
	
	PPath probe
	
	public void setDepth(double depth) {
		
		this.depth = depth
		probe.offset = new Point2D.Double(0.0, -depth * 100.0)  
	}
	
	public double getDepth() {
		return this.depth
	}
}