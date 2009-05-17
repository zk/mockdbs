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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import napplelabs.dbssim.canvasviews.NeuronPath;
import napplelabs.dbssim.canvasviews.Probe;
import napplelabs.dbssim.neuron.NeuronPathRep;
import napplelabs.dbssim.neuron.NeuronType;
import net.miginfocom.swing.MigLayout;

import com.thoughtworks.xstream.XStream;

import ddf.minim.Minim;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.util.PPaintContext;


public class ContentPanel extends JPanel {

	PCanvas canvas = new PCanvas();

	List<NeuronPath> neurons = new ArrayList<NeuronPath>();

	private Probe probe;

	private Minim minim;

	private JLabel label;

	private JSlider slider;

	private final List<NeuronType> neuronTypes;

	private JPanel featurePanel;

	private JButton newNeuronButton;

	public ContentPanel(final List<NeuronType> neuronTypes, final Minim minim,
			final JLabel label) {
		this.neuronTypes = neuronTypes;
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
		canvas
				.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		canvas.getCamera().setViewOffset(1000 / 2, 700 / 2);

		probe.rotate(Math.PI / 360 * 30);
		backgroundLayer.addChild(probe);

		canvas.getLayer().addInputEventListener(new NodeDragHandler(this));
		canvas.getLayer().addInputEventListener(new PBasicInputEventHandler() {
			@Override
			public void mouseClicked(PInputEvent evt) {
				PNode picked = evt.getPickedNode();
				
				final NeuronPath path = findNeuronPath(picked);
				
				JPopupMenu menu = createPopupMenu(path);
				
				double posX = evt.getCanvasPosition().getX();
				double posY = evt.getCanvasPosition().getY();
				
				menu.show(canvas, (int) posX, (int) posY);
				
				/*if(path != null) {
					addNeuron(NeuronPath.copy(path));
				}*/
			}
		});
		

		load();
		// Birds eye stuff

		BirdsEyeView view = new BirdsEyeView();
		view.connect(canvas,
				new PLayer[] { canvas.getLayer(), backgroundLayer });

		// view.viewedCanvas = canvas;
		// view.addLayer(canvas.getLayer());
		// view.addLayer(backgroundLayer);

		view.getCamera().setOffset(10, 10);

		canvas.getCamera().addChild(view.getCamera());

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {
					public boolean dispatchKeyEvent(final KeyEvent e) {
						boolean discardEvent = false;

						if (e.getID() == KeyEvent.KEY_PRESSED) {

							if (e.getKeyCode() == 40) {
								updateProbeDepth(probe.getDepth() - 0.01);

								slider
										.setValue((int) (probe.getDepth() * 1000));

								discardEvent = true;
							}

							if (e.getKeyCode() == 38) {

								updateProbeDepth(probe.getDepth() + 0.01);

								slider
										.setValue((int) (probe.getDepth() * 1000));
								discardEvent = true;
							}

						}
						return discardEvent;
					}
				});

	}
	
	/**
	 * Creates the popup menu shown when a NeuronPath is right-clicked
	 * on.
	 * @return 
	 */
	public JPopupMenu createPopupMenu(final NeuronPath neuronPath) {
		JPopupMenu menu = new JPopupMenu();
		menu.add(new AbstractAction("Clone " + neuronPath.getName()) {
			public void actionPerformed(ActionEvent e) {
				addNeuronPath(NeuronPath.copy(neuronPath));
			}
		});
		
		menu.add(new AbstractAction("Remove " + neuronPath.getName()) {
			public void actionPerformed(ActionEvent e) {
				removeNeuronPath(neuronPath);
			}
		});
		
		return menu;
	}

	private void removeNeuronPath(final NeuronPath path) {
		path.removeFromParent();
		path.setPlaying(false);
		neurons.remove(path);
	}

	/**
	 * Walks up parents until neuron path is found. Returns null if not.
	 * @param picked
	 * @return NeuronPath found, or null if none found.
	 */
	private NeuronPath findNeuronPath(PNode picked) {
		if(picked instanceof NeuronPath) return (NeuronPath) picked;
		PNode node = picked;
		while(node != null) {
			if(node.getParent() instanceof NeuronPath) return (NeuronPath) node.getParent();
			node = node.getParent();
		}
		return null;
	}
	
	/**
	 * Adds panel to the end of the feature panel
	 * @param panel
	 */
	private void addFeaturePanel(JPanel panel) {
		/*int addNeuronButtonIndex = 0;
		for(Component c: featurePanel.getComponents()) {
			if(c == newNeuronButton) {
				break;
			}
			addNeuronButtonIndex++;
		}
		
		featurePanel.add(panel, addNeuronButtonIndex);*/
		
	}

	private Component createFeaturePanel() {
		
		featurePanel = new JPanel();
		featurePanel.setLayout(new MigLayout("wrap 2"));
		
		newNeuronButton = new JButton("Add new neuron");
		newNeuronButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				/*JFrame frame = walkToFindJFrame(ContentPanel.this);
				if(frame == null) {
					System.out.println("Couldn't find jframe");
					return;
				}
				FileDialog fd = new FileDialog(frame);
				fd.setVisible(true);
				
				String file = fd.getDirectory() + fd.getFile();
				if(file == null) {
					System.out.println("File not chosen");
					return;
				}
				
				System.out.println(file);
				
				NeuronType nt = new NeuronType("Unknown", file, Color.white);
				featurePanel.add(createNeuronPanel(nt), "wrap");
				featurePanel.validate();*/
				
				NewNeuronDialog nnd = new NewNeuronDialog();
				nnd.setVisisble(true);
				NeuronType nt = nnd.getNeuronType();
				if(nt != null) {
					featurePanel.add(createNeuronPanel(nt), "wrap");
					featurePanel.validate();
				}
				
			}
			
		});
		featurePanel.add(newNeuronButton, "wrap");
		
		
		
		for(final NeuronType nt: neuronTypes) {
			JPanel np = createNeuronPanel(nt);
			
			//button.setFont(new Font("Arial", Font.PLAIN, 8));
			
			featurePanel.add(np, "wrap");
		}
		
		

		/*
		 * JButton importButton = new JButton("Import Media");
		 * panel.add(importButton, "wrap");
		 * 
		 * JComboBox userNeuronsCombo = new JComboBox();
		 * userNeuronsCombo.addItem("hello world this is zack");
		 * userNeuronsCombo.addItem("qwer asdf zxcv ");
		 * panel.add(userNeuronsCombo, "wrap");
		 * 
		 * JButton addButton = new JButton("Add");
		 * addButton.addActionListener(new ActionListener() {
		 * 
		 * public void actionPerformed(ActionEvent e) { new AddMediaHud(); }
		 * 
		 * }); panel.add(addButton, "wrap");
		 */
		return featurePanel;

	}

	private JPanel createNeuronPanel(final NeuronType nt) {
		JPanel np = new JPanel();
		np.setLayout(new MigLayout("wrap 2, ins 0"));
		
		JButton button = new JButton(nt.getName());
		
		JPanel neuronPreviewPanel = new JPanel() {
			@Override
			public void paint(final Graphics _g) {
				Graphics2D g = (Graphics2D) _g;
				
				g.setPaint(nt.getColor());
				g.setStroke(new BasicStroke(0.75f));
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.fillOval(2, 2, getWidth()-4, getHeight()-4);
				g.setColor(Color.BLACK);
				g.drawOval(2, 2, getWidth()-4, getHeight()-4);
			}
		};
		
		np.add(neuronPreviewPanel, "width 30, height 30");
		np.add(button);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				addNeuronPath(new NeuronPath(nt, minim));
			}
		});
		return np;
	}

	/**
	 * Walks up getParent() till a JFrame is found, or returns null.
	 * @param c
	 * @return found JFrame, or null if none found.
	 */
	protected JFrame walkToFindJFrame(Container c) {
		Container curr = c;
		while(curr != null) {
			if(curr.getParent() instanceof JFrame) return (JFrame) curr.getParent();
			curr = curr.getParent();
		}
		
		return null;
	}

	public void addNeuronPath(final NeuronPath path) {
		path.translate(canvas.getCamera().getViewBounds().getCenterX(), canvas
				.getCamera().getViewBounds().getCenterY());
		canvas.getLayer().addChild(path);
		neurons.add(path);

		path.getPath().addInputEventListener(new PBasicInputEventHandler() {
			@Override
			public void mouseClicked(final PInputEvent evt) {
				if (evt.isLeftMouseButton() && evt.getClickCount() == 2) {
					removeNeuronPath(path);
				}
			}

			
		});

		persist();
	}

	private Component createDepthPanel() {
		JPanel panel = new JPanel();
		slider = new JSlider(JSlider.VERTICAL);
		panel.setLayout(new BorderLayout());
		panel.add(slider, BorderLayout.CENTER);
		slider.setMaximum(20000);
		slider.setMinimum(-10000);
		slider.setValue(20000);
		slider.setFocusable(false);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(final ChangeEvent e) {
				double depth = (double) slider.getValue() / 1000;
				updateProbeDepth(depth);
			}

		});

		return panel;

	}

	private void updateProbeDepth(final double depth) {
		probe.setDepth(depth);
		canvas.repaint();
		recalcDistances();
		DecimalFormat twoPlaces = new DecimalFormat("0.00");
		label.setText(twoPlaces.format(probe.getDepth()) + " mm");
		Rectangle2D rect = canvas.getCamera().getViewBounds();
		// double x = rect.getX();
		// double y = rect.getY();

		double w = rect.getWidth();
		double h = rect.getHeight();

		double p_x = probe.getProbe().getGlobalTranslation().getX();
		double p_y = probe.getProbe().getGlobalTranslation().getY();

		Rectangle2D new_r = new Rectangle2D.Double(p_x - w / 2, p_y - h / 2, w,
				h);

		// canvas.getCamera().animateViewToCenterBounds(new_r, true, 1);
		canvas.getCamera().setViewBounds(new_r);
	}

	public void recalcDistances() {
		Point2D p = probe.getProbe().getGlobalTranslation();
		for (NeuronPath np : neurons) {

			double diameter = np.getPath().getWidth();
			float diff = (float) p
					.distance(np.getPath().getGlobalTranslation());
			if (diff < diameter) {
				np.setLevel((float) ((diameter - diff) / diameter));
			} else {
				np.setLevel(0f);
			}
		}
	}

	public void persist() {
		XStream xs = new XStream();

		// Write to a file in the file system
		try {

			List<NeuronPathRep> reps = new ArrayList<NeuronPathRep>();
			for (NeuronPath p : neurons) {
				NeuronPathRep r = new NeuronPathRep();
				r.color = p.getColor();
				r.mediaFile = p.getMediaFile();
				r.location = p.getGlobalTranslation();
				r.name = p.getName();
				reps.add(r);
			}

			ObjectOutputStream os = xs.createObjectOutputStream(new FileWriter(
					new File("./neurons")));

			os.writeObject(reps);
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void load() {
		XStream xs = new XStream();

		// Write to a file in the file system

		if (!new File("./neurons").exists())
			return;

		try {
			ObjectInputStream is = xs.createObjectInputStream(new FileReader(
					new File("./neurons")));
			List<NeuronPathRep> paths = (List<NeuronPathRep>) is.readObject();

			for (NeuronPathRep r : paths) {
				NeuronPath np = new NeuronPath(r.color, r.name, r.mediaFile, minim);
				np.setOffset(r.location);
				addNeuronPath(np);
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
