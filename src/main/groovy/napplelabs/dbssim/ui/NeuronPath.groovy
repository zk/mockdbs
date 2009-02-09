package napplelabs.dbssim.ui

import edu.umd.cs.piccolo.PNode
import java.awt.Color
import edu.umd.cs.piccolo.nodes.PPath
import napplelabs.dbssim.neuron.NeuronPathimport ddf.minim.Minimimport java.awt.BasicStrokeimport java.awt.geom.Point2Dimport edu.umd.cs.piccolo.nodes.PTextimport java.awt.Font
class ThalamusPath extends NeuronPath {
  public ThalamusPath(Minim minim) {
    super(Color.blue, "/Users/zkim/napplelabs/dbssim/src/main/resources/Rage Against The Machine - rage against the machine - 06 - Know Your Enemy.mp3", minim)
  }
}

class StnPath extends NeuronPath {
  public StnPath(Minim minim) {
    super(Color.red, "/Users/zkim/napplelabs/dbssim/src/main/resources/10-PD-STN.wav", minim)
  }
}

class SnrPath extends NeuronPath {
  public SnrPath(Minim minim) {
    super(Color.green, "/Users/zkim/napplelabs/dbssim/src/main/resources/15-PD-SNr.wav", minim)
  }
}

class Probe extends PNode {
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
		probe.offset = new Point2D.Double(0.0, -depth * 1000.0)  
	}
	
	public double getDepth() {
		return probe.offset.y / 100
	}
}