package ui;

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

public class ProcessPreviewPanel extends JPanel {

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
	

	public ProcessPreviewPanel(int width, int height) {
		this.width = width;
		this.height = height;
		setSize(width, height);
		setLocation(0, 0);
	}


	@Override
	public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        
        if(isDefault){
        	g2d.setColor(Color.BLACK);
        	g2d.drawString("No Preview To Display", 10, 10);
        }
        else{
        	g2d.drawImage(dispImage, 0, 0, this);
        	if(isCenterDetection){
        		g2d.setColor(Color.RED);
        		g2d.fillRect(center.x-15, center.y-15, 30, 30);
        	}
        	if(isDrawHull){
        		for(int i = 0; i<hullPoints.size(); i++){
            		Point2D p1 = hullPoints.get(i);
            		Point2D p2 = hullPoints.get((i+1)%hullPoints.size());
//            		g2d.setColor(Color.red);
//            		g2d.drawLine((int)p1.x(), (int)p1.y(), (int)p2.x(), (int)p2.y());
            		g2d.setColor(Color.red);
//            		g2d.fillOval(p1.x()-2, p1.y()-2, 4, 4);
            		g2d.fillRect(p1.x()-4, p1.y()-4, 8, 8);
            	} 
        	}
        	if(isDrawPoints){
            	
            	for(int i = 0; i<points.size(); i++){
            		Point curPoint = points.get(i);
            		g2d.setColor(Color.RED);
            		g2d.fillOval(curPoint.x-3, curPoint.y-3, 6, 6);
            	}
            }
        	if(isFinalDisplay){
        		for(int i=0; i<filtered.size(); i++){
        			Vector<Point> curRay = filtered.get(i);
        			g2d.setColor(Color.DARK_GRAY);
        			for(int cIndex = curRay.size()-1; cIndex>=0; cIndex--){
        				g2d.drawString(""+(cIndex+1), curRay.get(cIndex).x, curRay.get(cIndex).y);
        			}
        		}
        	}
        }
        
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


	public void displayImageCenter(Image image, Point centerPoint) {
		dispImage = image;
		center = centerPoint;
		isCenterDetection = true;
		isDefault = false;
		isDrawHull = false;
		isDrawPoints = false;
		repaint();
	}


	public void displayImageHull(Image image, Vector<Point2D> hulls) {
		dispImage = image;
		hullPoints = hulls;
		isDrawHull = true;
		isDefault = false;
		repaint();
	}


	public void displayImageTestPoints(Image image, Vector<Point> testPoints) {
		dispImage = image;
		points = testPoints;
		isDrawPoints = true;
		isDefault = false;
		repaint();
	}


	public void displayImageTestPointsOverlay(Image image, ArrayList<Vector<Point>> groupRings, int mode) {
		dispImage = image;
		filtered = new ArrayList<Vector<Point>>(); 
		for(int i=0; i<groupRings.size(); i++){
			Vector<Point> ray = groupRings.get(i);
			if(ray.size() == mode)
				filtered.add(ray);
		}
		isFinalDisplay = true;
		isDefault = false;
		isCenterDetection = false;
		isDrawHull = false;
		isDrawPoints = false;
		repaint();
	}

}
