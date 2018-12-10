package ui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ConvexHull.Point2D;

import java.awt.image.BufferedImage;

public class PreviewDialog extends JDialog {

	private ProcessPreviewPanel prevPanel;
	private String imgSrc;
	private ImageIcon imgIcn;
	private Image image;
	/**
	 * Create the dialog.
	 * @param radii 
	 * @param mode 
	 * @param groupRings 
	 * @param grayImage 
	 */
	private BufferedImage preview;
	
	public PreviewDialog(BufferedImage image) {
		this.preview = image;
		configure();
		prevPanel = new ProcessPreviewPanel(preview.getWidth(), preview.getHeight());
		setContentPane(prevPanel);
		prevPanel.displayImage(convertToImage(preview));
	}

	public PreviewDialog(BufferedImage image, Point centerPoint) {
		this.preview = image;
		configure();
		prevPanel = new ProcessPreviewPanel(preview.getWidth(), preview.getHeight());
		setContentPane(prevPanel);
		prevPanel.displayImageCenter(convertToImage(preview), centerPoint);
	}

	public PreviewDialog(BufferedImage image, Vector<Point2D> hullPoints) {
		this.preview = image;
		configure();
		prevPanel = new ProcessPreviewPanel(preview.getWidth(), preview.getHeight());
		setContentPane(prevPanel);
		prevPanel.displayImageHull(convertToImage(preview), hullPoints);
	}

	public PreviewDialog(BufferedImage image, Vector<Point> testPoints,boolean b) {
		this.preview = image;
		configure();
		prevPanel = new ProcessPreviewPanel(preview.getWidth(), preview.getHeight());
		setContentPane(prevPanel);
		prevPanel.displayImageTestPoints(convertToImage(preview), testPoints);
	}

	public PreviewDialog(BufferedImage image, ArrayList<Vector<Point>> groupRings, int mode) {
		this.preview = image;
		configure();
		prevPanel = new ProcessPreviewPanel(preview.getWidth(), preview.getHeight());
		setContentPane(prevPanel);
		prevPanel.displayImageTestPointsOverlay(convertToImage(preview), groupRings, mode);
	}

	private void configure() {
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (ClassNotFoundException | InstantiationException| IllegalAccessException | UnsupportedLookAndFeelException e1) { e1.printStackTrace(); }
		setModal(true);
		setSize(preview.getWidth()+17, preview.getHeight()+40);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		imgSrc = "resource/icon.jpg";
		imgIcn = new ImageIcon(imgSrc);
		image = imgIcn.getImage();
		setIconImage(image);
	}
	
	public Image convertToImage( BufferedImage bfImage ){
		return bfImage.getScaledInstance(bfImage.getWidth(), bfImage.getHeight(), 0);
	}
}
