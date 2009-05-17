package napplelabs.dbssim.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import napplelabs.dbssim.neuron.NeuronType;
import net.miginfocom.swing.MigLayout;

public class NewNeuronDialog {
	
	private JDialog dialog;
	private Color color = Color.white;
	private JPanel neuronPreviewPanel;
	protected String filePath;
	private NeuronType neuronType = null;
	private JTextField nameText;

	public NewNeuronDialog() {
		this.dialog = new JDialog();
		
		dialog.addComponentListener(new ComponentListener() {

			public void componentHidden(ComponentEvent e) {
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentResized(ComponentEvent e) {	
			}

			public void componentShown(ComponentEvent e) {
			}
			
		});
		dialog.setSize(348, 215);
		dialog.setLocationRelativeTo(null);
		dialog.setTitle("New Neuron");
		dialog.setModal(true);
		Container pane = dialog.getContentPane();
		pane.setLayout(new BorderLayout());
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new MigLayout("wrap 2"));
		
		JLabel nameLabel = new JLabel("Name:");
		nameText = new JTextField();
		contentPanel.add(nameLabel);
		contentPanel.add(nameText, "w 200");
		
		neuronPreviewPanel = new JPanel() {
			@Override
			public void paint(final Graphics _g) {
				Graphics2D g = (Graphics2D) _g;
				
				g.setPaint(color);
				g.setStroke(new BasicStroke(0.75f));
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.fillOval(2, 2, getWidth()-4, getHeight()-4);
				g.setColor(Color.BLACK);
				g.drawOval(2, 2, getWidth()-4, getHeight()-4);
			}
		};
		contentPanel.add(neuronPreviewPanel, "w 50, h 50");
		
		JButton chooseColor = new JButton("Choose Color");
		chooseColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				color = JColorChooser.showDialog(NewNeuronDialog.this.dialog, "Choose Neuron Color", color);
				neuronPreviewPanel.repaint();
			}
			
		});
		contentPanel.add(chooseColor);
		
		final JLabel fileLabel = new JLabel("Media File:");
		final JLabel pathLabel = new JLabel("None");
		JButton fileButton = new JButton("Choose Media File");
		fileButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(NewNeuronDialog.this.dialog);
				fd.setVisible(true);
				
				if(fd.getFile() != null) {
					filePath = fd.getDirectory() + fd.getFile();
					fileLabel.setText(fd.getFile());
				}
				
			}
			
		});
		
		contentPanel.add(fileLabel, "w 100:100:100");
		contentPanel.add(fileButton);
		
		pane.add(contentPanel, BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		JButton ok = new JButton("Create Neuron");
		ok.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				okPressed();
			}
			
		});
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cancelPressed();
			}
			
		});
		
		controlPanel.add(cancel);
		controlPanel.add(ok);
		
		pane.add(controlPanel, BorderLayout.SOUTH);
	}
	
	public NewNeuronDialog setVisisble(boolean b) {
		dialog.setVisible(b);
		return this;
	}
	
	private void okPressed() {
		neuronType = new NeuronType(nameText.getText(), filePath, color);
		setVisisble(false);
	}
	
	private void cancelPressed() {
		setVisisble(false);
	}
	
	public static void main(String[] args) {
		NewNeuronDialog d = new NewNeuronDialog().setVisisble(true);
		NeuronType t = d.getNeuronType();
		System.out.println(t);
	}

	
	/**
	 * Can return null
	 * @return
	 */
	public NeuronType getNeuronType() {
		return neuronType;
	}

}
