package napplelabs.dbssim.canvasviews;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * Represents the view of a probe on the canvas.
 * @author zkim
 *
 */

public class Probe extends PNode {

	private PPath probe;
	private double depth;

	public Probe() {
		PPath path = PPath.createLine(0, -1000, 0, 2000);
		// path.rotate(Math.PI)
		path.setStroke(new BasicStroke(3));
		path.setPaint(Color.lightGray);
		path.setStrokePaint(Color.lightGray);

		setProbe(PPath.createLine(0, 0, 0, -3000));

		getProbe().setOffset(new Point2D.Double(0, -2000));
		getProbe().setStroke(new BasicStroke(5));

		addChild(path);
		addChild(getProbe());

		for (int i = 10; i > -20; i++) {
			PText t = new PText(-i + "mm");
			t.setFont(new Font("Arial", Font.PLAIN, 16));
			t.setOffset(new Point2D.Double(30, i * 100));
			t.setTextPaint(Color.lightGray);
			addChild(t);
		}

	}

	public void setDepth(double depth) {
		
		this.depth = depth;
		getProbe().setOffset(new Point2D.Double(0.0, -depth * 100.0));  
	}

	public double getDepth() {
		return this.depth;
	}

	public void setProbe(PPath probe) {
		this.probe = probe;
	}

	public PPath getProbe() {
		return probe;
	}
}
