package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GraphPanel extends JPanel {

	/**
	 * Create the panel.
	 * @param relativeRadius 
	 */
	private Vector<Integer> relativeRadius;
	private BufferedImage grayImage;
	
	public GraphPanel(Vector<Integer> relativeRadius, ArrayList<Vector<Point>> groupRings, int mode, BufferedImage grayImage) {
		this.relativeRadius = relativeRadius;
		this.grayImage = grayImage;
		configure();
		repaint();
	}

	@Override
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		
		int wInterval = 450/relativeRadius.size();
		int hInterval;
		int prevX;
		int prevY;
		int maxH = 0;
		
//		g2d.drawImage(convertToImage(grayImage), 0, 0, this);
		g2d.setColor(Color.BLACK);
		g2d.drawString("Variations in Ring Widths with respect to its inner ring", 100, 230);
		
		for(int i=0; i<relativeRadius.size(); i++){
			if(relativeRadius.get(i)>maxH)
				maxH = relativeRadius.get(i);
		}
		hInterval = 250/maxH;
		prevX = wInterval/2;
		prevY = 250-(relativeRadius.get(0)*hInterval);
		for(int i=0; i<relativeRadius.size(); i++){
			int x = (i*wInterval)+(wInterval/2);
			int y = 250-(relativeRadius.get(i)*hInterval);
			g2d.setColor(Color.BLUE);
			g2d.fillOval(x-5,y-5, 10, 10);
			g2d.drawLine(prevX, prevY, x,y);
			prevX = x;
			prevY = y;
		}
		
		Toolkit.getDefaultToolkit().sync();
        g.dispose();
	}

	private void configure() {
		setSize(450, 250);
		setLocation(0,0);
		setBorder(BorderFactory.createLineBorder(Color.pink));
	}
	
	public Image convertToImage( BufferedImage bfImage ){
		return bfImage.getScaledInstance(bfImage.getWidth(), bfImage.getHeight(), 0);
	}

	public void adjust() {
	}

}
