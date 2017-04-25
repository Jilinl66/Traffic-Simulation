package simulation;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyPanels extends JPanel{
	
	/**
	 * Create panel for painting
	 * @author LiuJilin
	 */
	private static final long serialVersionUID = 1L;
	Container parent;
	boolean fullscreen=false;
	JFrame frame = new JFrame();
	SimulationApplet simulationApplet = new SimulationApplet();
	
	public MyPanels(){
		setForeground(Color.WHITE);
		setBackground(Color.LIGHT_GRAY);
		setFont(new Font("Arial", Font.BOLD, 25));
		frame.add(this);
		frame.pack();
//		switchFullScreen();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getWidth(), getHeight());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		simulationApplet.updatePaint(g);
	}
}