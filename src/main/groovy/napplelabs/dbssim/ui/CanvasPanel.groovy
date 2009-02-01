package napplelabs.dbssim.ui

import edu.umd.cs.piccolo.PCanvas
import java.awt.BorderLayout
import javax.swing.JPanel
import net.miginfocom.swing.MigLayout
import javax.swing.JButton
import java.awt.event.ActionListener
import javax.swing.JSlider
import edu.umd.cs.piccolo.nodes.PPath
import java.awt.Color
import edu.umd.cs.piccolo.PNode
import edu.umd.cs.piccolo.util.PPaintContext
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler
import edu.umd.cs.piccolo.event.PInputEvent
import java.awt.BasicStroke
import edu.umd.cs.piccolo.nodes.PText
import java.awt.geom.Point2D
import java.awt.Font

/**
 * Created by IntelliJ IDEA.
 * User: zkim
 * Date: Jan 31, 2009
 * Time: 7:37:16 PM
 * To change this template use File | Settings | File Templates.
 */

public class CanvasPanel extends JPanel {

  PCanvas canvas = new PCanvas();

  public CanvasPanel() {
    layout = new BorderLayout()
    add(canvas, BorderLayout.CENTER)
    JPanel panel = new JPanel()
    add(panel, BorderLayout.EAST)
    panel.layout = new BorderLayout()
    panel.add(new FeaturePanel(canvas), BorderLayout.CENTER)
    panel.add(new DepthPanel(), BorderLayout.WEST)

    canvas.zoomEventHandler = new NapPZoomEventHandler()
    canvas.animatingRenderQuality = PPaintContext.HIGH_QUALITY_RENDERING
    canvas.interactingRenderQuality = PPaintContext.HIGH_QUALITY_RENDERING
    canvas.camera.setViewOffset(1000 / 2, 700 / 2);

    Probe probe = new Probe()
    probe.rotate(Math.PI / 360 * 30)
    canvas.layer.addChild(probe)

    //canvas.layer.

    canvas.layer.addInputEventListener(new NodeDragHandler(probe))


  }
}

class FeaturePanel extends JPanel {

  PCanvas canvas

  public FeaturePanel(PCanvas canvas) {
    this.canvas = canvas
    init()
  }

  public void init() {
    layout = new MigLayout("wrap 1")
    JButton thal = new JButton("Thalamus")
    thal.addActionListener({
      NeuronPath path = new ThalamusPath()
      path.translate (canvas.camera.viewBounds.centerX, canvas.camera.viewBounds.centerY)

      canvas.layer.addChild(path)
    } as ActionListener)
    add(thal)

    JButton stn = new JButton("STN")
    stn.addActionListener({
      NeuronPath path = new StnPath()
      path.translate (canvas.camera.viewBounds.centerX, canvas.camera.viewBounds.centerY)

      canvas.layer.addChild(path)
    } as ActionListener)
    add(stn)

    JButton snr = new JButton("SNr")
    snr.addActionListener({
      NeuronPath path = new SnrPath()
      path.translate (canvas.camera.viewBounds.centerX, canvas.camera.viewBounds.centerY)

      canvas.layer.addChild(path)
    } as ActionListener)
    add(snr)

  }

}

class DepthPanel extends JPanel {
  public DepthPanel() {
    init()
  }

  public void init() {
    JSlider slider = new JSlider(JSlider.VERTICAL)
    layout = new BorderLayout()
    add(slider, BorderLayout.CENTER)
    slider.maximum = 200
    slider.minimum = -100
    slider.value = 200
  }
}

class NeuronPath extends PNode {
  Color color

  public NeuronPath(Color color) {
    this.color = color
    init()
  }

  public void init() {
    PPath path = PPath.createEllipse(0, 0, 20, 20)
    path.paint = color

    addChild(path)
  }
}

class ThalamusPath extends NeuronPath {
  public ThalamusPath() {
    super(Color.blue)
  }
}

class StnPath extends NeuronPath {
  public StnPath() {
    super(Color.red)
  }
}

class SnrPath extends NeuronPath {
  public SnrPath() {
    super(Color.green)
  }
}

class NodeDragHandler extends PDragSequenceEventHandler {
  Probe probe

  public NodeDragHandler(Probe probe) {
    this.probe = probe
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
    //if (probe.children.contains(node)) return
    node.translate(e.getDelta().width, e.getDelta().height);

  }
}

class Probe extends PNode {
  public Probe() {
    init()
  }

  public void init() {
    PPath path = PPath.createLine(0, 2000, 0, -1000);
    path.rotate(Math.PI) 
    path.stroke = new BasicStroke(3)
    path.paint = Color.lightGray
    path.strokePaint = Color.lightGray

    //PPath probe = PPath.createLine(0, 1000)

    addChild(path)
    
    (10..-20).each {int i->
      PText t = new PText(-i+"mm")
      t.font = new Font("Arial", Font.PLAIN, 16)
      t.offset = new Point2D.Double(30, i*100)
      t.textPaint = Color.lightGray
      addChild(t)
    }



    
  }

  public void setDepth(float depth) {

  }

  public float getDepth() {
    
  }
}

