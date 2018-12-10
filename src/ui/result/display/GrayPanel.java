package ui.result.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;

import ConvexHull.Point2D;

public class GrayPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	
	private Image dispImage;
	private Point center;
	private boolean isCenterDetection = false, isDrawPoints = false, isDrawHull = false, isDefault = false, isFinalDisplay = false;
	private Vector<Point> points;
	private Vector<Point2D> hullPoints;
	private int width, height;
	private ArrayList<Vector<Point>> filtered;
	

	public GrayPanel() {
		setSize(200, 250);
	}


	@Override
	public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(dispImage, 0, 0, this);
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }


	public void defaultView() {
		isDefault = true;
		repaint();
	}


	public void displayImage(Image image) {
		dispImage = image;
		isDefault = false;
		isCenterDetection = false;
		isDrawHull = false;
		isDrawPoints = false;
		repaint();
	}

}
