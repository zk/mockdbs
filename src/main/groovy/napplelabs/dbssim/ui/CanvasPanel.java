package napplelabs.dbssim.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.thoughtworks.xstream.XStream;

import ddf.minim.Minim;

import net.miginfocom.swing.MigLayout;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.util.PPaintContext;

import napplelabs.dbssim.neuron.NeuronPath;
import napplelabs.dbssim.neuron.NeuronPathRep;

public class CanvasPanel extends JPanel {

	PCanvas canvas = new PCanvas();

	List<NeuronPath> neurons = new ArrayList<NeuronPath>();

	private Probe probe;

	private Minim minim;

	private JLabel label;

	public CanvasPanel(Minim minim, JLabel label) {
		this.label = label;
		this.minim = minim;
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout());

		PLayer backgroundLayer = new PLayer();
		canvas.getCamera().addLayer(1, backgroundLayer);

		probe = new Probe();

		panel.add(createFeaturePanel(), BorderLayout.CENTER);
		panel.add(createDepthPanel(), BorderLayout.WEST);

		canvas.setZoomEventHandler(new NapPZoomEventHandler());
		canvas.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		canvas.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		canvas.getCamera().setViewOffset(1000 / 2, 700 / 2);


		probe.rotate(Math.PI / 360 * 30);
		backgroundLayer.addChild(probe);

		canvas.getLayer().addInputEventListener(new NodeDragHandler(this));

		
		load();
		//Birds eye stuff
		
		BirdsEyeView view = new BirdsEyeView();
		view.connect(canvas, new PLayer[] { canvas.getLayer(), backgroundLayer} );
		
		
		//view.viewedCanvas = canvas;		
		//view.addLayer(canvas.getLayer());
		//view.addLayer(backgroundLayer);
		
		canvas.getCamera().addChild(view.getCamera());
		
		
	}

	private Component createFeaturePanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("wrap 1"));
		JButton thal = new JButton("Thalamus");
		thal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNeuron(new NeuronPath(Color.blue, "/Users/zkim/napplelabs/dbssim/src/main/resources/7-PD-gpi.wav", minim));
			}				
		});
		panel.add(thal);

		JButton stn = new JButton("STN");
		stn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNeuron(new NeuronPath(Color.red, "/Users/zkim/napplelabs/dbssim/src/main/resources/10-PD-STN.wav", minim));
			}
		});
		panel.add(stn);

		JButton snr = new JButton("SNr");
		snr.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				addNeuron(new NeuronPath(Color.green, "/Users/zkim/napplelabs/dbssim/src/main/resources/15-PD-SNr.wav", minim));
			}
		});
		panel.add(snr);
		
		return panel;

	}

	public void addNeuron(NeuronPath path) {
		path.translate (canvas.getCamera().getViewBounds().getCenterX(), canvas.getCamera().getViewBounds().getCenterY());
		canvas.getLayer().addChild(path);
		neurons.add(path);
		
		persist();
	}

	private Component createDepthPanel() {
		JPanel panel = new JPanel();
		final JSlider slider = new JSlider(JSlider.VERTICAL);
		panel.setLayout(new BorderLayout());
		panel.add(slider, BorderLayout.CENTER);
		slider.setMaximum(2000);
		slider.setMinimum(-1000);
		slider.setValue(2000);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				probe.setDepth((double) slider.getValue() / 1000);
				canvas.repaint();
				recalcDistances();
				DecimalFormat twoPlaces = new DecimalFormat("0.00");
				label.setText(twoPlaces.format(-probe.getDepth()) + " mm");
				Rectangle2D rect = canvas.getCamera().getViewBounds();
				double x = rect.getX();
				double y = rect.getY();
				
				double w = rect.getWidth();
				double h = rect.getHeight();
				
				double p_x = probe.getProbe().getGlobalTranslation().getX();
				double p_y = probe.getProbe().getGlobalTranslation().getY();
				
				Rectangle2D new_r = new Rectangle2D.Double(p_x - w/2, p_y - h/2, w, h);
				
				//canvas.getCamera().animateViewToCenterBounds(new_r, true, 1);
				canvas.getCamera().setViewBounds(new_r);
			}
		});

		return panel;	 

	}
	
	public void recalcDistances() {
		Point2D p = probe.getProbe().getGlobalTranslation();
		for(NeuronPath np: neurons) {
			float diff = (float) p.distance(np.getPath().getGlobalTranslation());
			if(diff < 100) {
				np.setLevel((100f - diff)/100f);
			} else {
				np.setLevel(0f);
			}
		}
	}
	
	public void persist() {
		XStream xs = new XStream();

        //Write to a file in the file system
        try {
        	
        	List<NeuronPathRep> reps = new ArrayList<NeuronPathRep>();
        	for(NeuronPath p: neurons) {
        		NeuronPathRep r = new NeuronPathRep();
        		r.color = p.getColor();
        		r.mediaFile = p.getMediaFile();
        		r.location = p.getGlobalTranslation();
        		reps.add(r);
        	}
        	
            ObjectOutputStream os = xs.createObjectOutputStream(new FileWriter(new File("./neurons")));
            
            
            os.writeObject(reps);
            os.close();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void load() {
		XStream xs = new XStream();

        //Write to a file in the file system
        try {
            ObjectInputStream is = xs.createObjectInputStream(new FileReader(new File("./neurons")));
            List<NeuronPathRep> paths = (List<NeuronPathRep>) is.readObject();
            
            for(NeuronPathRep r: paths) {
            	NeuronPath np = new NeuronPath(r.color, r.mediaFile, minim);
            	np.setOffset(r.location);
            	addNeuron(np);
    		}
            
            is.close();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
