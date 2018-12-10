package ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import ConvexHull.Point2D;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.border.BevelBorder;

public class SimulationFrame extends JDialog{

	private JPanel contentPane;
	
	private String imgSrc;
	private ImageIcon imgIcn;
	private Image image;

	/**
	 * Create the frame.
	 * @param testPoints 
	 * @param hullPoints 
	 * @param centerPoint 
	 * @param prunedImage 
	 * @param hilditchImage 
	 * @param dilationImage 
	 * @param otsuImage 
	 * @param sobelImage 
	 * @param grayImage 
	 * @param inputImage 
	 */
	private BufferedImage inputImage;
	private BufferedImage grayImage;
	private BufferedImage sobelImage;
	private BufferedImage otsuImage;
	private BufferedImage dilationImage;
	private BufferedImage erosionImage;
	private BufferedImage hilditchImage;
	private BufferedImage prunedImage;
	private Point centerPoint;
	private Vector<Point2D> hullPoints; 
	private Vector<Point> testPoints;
	private MainInterface mainInterface;
	private ArrayList<Vector<Point>> groupRings;
	private int mode;
	private JLabel inputLabel, grayLabel, sobelLabel, otsuLabel, dilationLabel, erosionLabel, hilditchLabel,
			pruningLabel, centerDetectLabel, grahamLabel, testPointsLabel;
	
	public SimulationFrame(BufferedImage inputImage, 
							BufferedImage grayImage, 
							BufferedImage sobelImage, 
							BufferedImage otsuImage, 
							BufferedImage dilationImage,
							BufferedImage erosionImage,
							BufferedImage hilditchImage, 
							BufferedImage prunedImage, 
							Point centerPoint, 
							Vector<Point2D> hullPoints, 
							Vector<Point> testPoints, 
							MainInterface mainInterface, 
							ArrayList<Vector<Point>> groupRings, 
							int mode) 
	{
		this.inputImage = inputImage;
		this.grayImage = grayImage;
		this.sobelImage = sobelImage;
		this.otsuImage = otsuImage;
		this.dilationImage = dilationImage;
		this.erosionImage = erosionImage;
		this.hilditchImage = hilditchImage;
		this.prunedImage = prunedImage;
		this.centerPoint = centerPoint;
		this.hullPoints = hullPoints;
		this.testPoints = testPoints;
		this.mainInterface = mainInterface;
		this.groupRings = groupRings;
		this.mode = mode;
		configureFrame();
		generatePanels();
		addListeners();
	}

	private void generatePanels() {
		inputLabel = new JLabel();
		inputLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		inputLabel.setIcon(new ImageIcon(convertToImage(inputImage).getScaledInstance(200,200,0)));
		inputLabel.setBounds(10,11,200,200);
		contentPane.add(inputLabel);
		
		grayLabel = new JLabel();
		grayLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		grayLabel.setIcon(new ImageIcon(convertToImage(grayImage).getScaledInstance(200,200,0)));
		grayLabel.setBounds(220,11,200,200);
		contentPane.add(grayLabel);
		
		sobelLabel = new JLabel();
		sobelLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		sobelLabel.setIcon(new ImageIcon(convertToImage(sobelImage).getScaledInstance(200,200,0)));
		sobelLabel.setBounds(430,11,200,200);
		contentPane.add(sobelLabel);
		
		otsuLabel = new JLabel();
		otsuLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		otsuLabel.setIcon(new ImageIcon(convertToImage(otsuImage).getScaledInstance(200,200,0)));
		otsuLabel.setBounds(640,11,200,200);
		contentPane.add(otsuLabel);
		
		dilationLabel = new JLabel();
		dilationLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		dilationLabel.setIcon(new ImageIcon(convertToImage(dilationImage).getScaledInstance(200,200,0)));
		dilationLabel.setBounds(850,11,200,200);
		contentPane.add(dilationLabel);
		
		erosionLabel = new JLabel();
		erosionLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		erosionLabel.setIcon(new ImageIcon(convertToImage(erosionImage).getScaledInstance(200,200,0)));
		erosionLabel.setBounds(1060,11,200,200);
		contentPane.add(erosionLabel);
		
		hilditchLabel = new JLabel();
		hilditchLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		hilditchLabel.setIcon(new ImageIcon(convertToImage(hilditchImage).getScaledInstance(200,200,0)));
		hilditchLabel.setBounds(10,268,200,200);
		contentPane.add(hilditchLabel);
		
		pruningLabel = new JLabel();
		pruningLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		pruningLabel.setIcon(new ImageIcon(convertToImage(prunedImage).getScaledInstance(200,200,0)));
		pruningLabel.setBounds(220,268,200,200);
		contentPane.add(pruningLabel);
		
		centerDetectLabel = new JLabel();
		centerDetectLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		centerDetectLabel.setIcon(new ImageIcon(convertToImage(drawCenterImage(prunedImage)).getScaledInstance(200,200,0)));
		centerDetectLabel.setBounds(430,268,200,200);
		contentPane.add(centerDetectLabel);
		
		grahamLabel = new JLabel();
		grahamLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		grahamLabel.setIcon(new ImageIcon(convertToImage(drawHullImage(prunedImage)).getScaledInstance(200,200,0)));
		grahamLabel.setBounds(640,268,200,200);
		contentPane.add(grahamLabel);
		
		testPointsLabel = new JLabel();
		testPointsLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		testPointsLabel.setIcon(new ImageIcon(convertToImage(drawTestPointsImage(prunedImage)).getScaledInstance(200,200,0)));
		testPointsLabel.setBounds(850,268,200,200);
		contentPane.add(testPointsLabel);
		
		JLabel lblInputImage = new JLabel("1. Input Image");
		lblInputImage.setForeground(SystemColor.window);
		lblInputImage.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblInputImage.setBounds(65, 222, 115, 26);
		contentPane.add(lblInputImage);
		
		JLabel lblGrayscale = new JLabel("2. Grayscale");
		lblGrayscale.setForeground(SystemColor.window);
		lblGrayscale.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblGrayscale.setBounds(277, 222, 115, 26);
		contentPane.add(lblGrayscale);
		
		JLabel lblSobelEdgeDetection = new JLabel("3. Sobel Edge Detection");
		lblSobelEdgeDetection.setForeground(SystemColor.window);
		lblSobelEdgeDetection.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSobelEdgeDetection.setBounds(464, 222, 147, 26);
		contentPane.add(lblSobelEdgeDetection);
		
		JLabel lblOtsuThresholding = new JLabel("4. Otsu Thresholding");
		lblOtsuThresholding.setForeground(SystemColor.window);
		lblOtsuThresholding.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblOtsuThresholding.setBounds(685, 222, 128, 26);
		contentPane.add(lblOtsuThresholding);
		
		JLabel lblDilation = new JLabel("5. Dilation");
		lblDilation.setForeground(SystemColor.window);
		lblDilation.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDilation.setBounds(918, 222, 115, 26);
		contentPane.add(lblDilation);
		
		JLabel lblErosion = new JLabel("6. Erosion");
		lblErosion.setForeground(SystemColor.window);
		lblErosion.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblErosion.setBounds(1128, 222, 115, 26);
		contentPane.add(lblErosion);
		
		JLabel lblHilditchsThinning = new JLabel("7. Hilditch's Thinning");
		lblHilditchsThinning.setForeground(SystemColor.window);
		lblHilditchsThinning.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblHilditchsThinning.setBounds(65, 479, 115, 26);
		contentPane.add(lblHilditchsThinning);
		
		JLabel lblPruning = new JLabel("8. Pruning");
		lblPruning.setForeground(SystemColor.window);
		lblPruning.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPruning.setBounds(277, 479, 115, 26);
		contentPane.add(lblPruning);
		
		JLabel lblCenterDetection = new JLabel("9. Center Detection");
		lblCenterDetection.setForeground(SystemColor.window);
		lblCenterDetection.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCenterDetection.setBounds(464, 479, 147, 26);
		contentPane.add(lblCenterDetection);
		
		JLabel lblGrahamScan = new JLabel("10. Graham Scan");
		lblGrahamScan.setForeground(SystemColor.window);
		lblGrahamScan.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblGrahamScan.setBounds(685, 479, 128, 26);
		contentPane.add(lblGrahamScan);
		
		JLabel lblSelectingTestPoints = new JLabel("11. Selecting Test Points");
		lblSelectingTestPoints.setForeground(SystemColor.window);
		lblSelectingTestPoints.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSelectingTestPoints.setBounds(874, 479, 159, 26);
		contentPane.add(lblSelectingTestPoints);
		
		JButton btnViewLabeledRings = new JButton("View Labeled Rings");
		btnViewLabeledRings.setBorder(new BevelBorder(BevelBorder.RAISED, Color.DARK_GRAY, Color.BLACK, Color.WHITE, Color.LIGHT_GRAY));
		btnViewLabeledRings.setBackground(Color.GRAY);
		btnViewLabeledRings.setFont(new Font("Verdana", Font.BOLD, 10));
		btnViewLabeledRings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PreviewDialog dialog = new PreviewDialog(inputImage, groupRings, mode);
				dialog.setTitle("Labeled Rings");
				dialog.setVisible(true);
			}
		});
		btnViewLabeledRings.setBounds(1091, 349, 152, 40);
		contentPane.add(btnViewLabeledRings);
		
		JLabel lblDrop = new JLabel("drop");
		lblDrop.setBounds(0, 0, 1269, 520);
		lblDrop.setOpaque(false);
		imgSrc = "resource/dropSimu.jpg";
		imgIcn = new ImageIcon(imgSrc);
		image = imgIcn.getImage();
		lblDrop.setIcon(imgIcn);
		contentPane.add(lblDrop);
	}

	private BufferedImage drawTestPointsImage(BufferedImage prunedImage) {
		BufferedImage testImage = new BufferedImage(prunedImage.getWidth(), prunedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster wr = testImage.getRaster();
		for(int i=0; i<prunedImage.getWidth(); i++){
			for(int j = 0; j<prunedImage.getHeight(); j++){
				wr.setSample(i, j, 0, prunedImage.getRGB(i, j));
			}
		}
		for(int h=0; h<testPoints.size(); h++){
			Point p = testPoints.get(h);
			for(int i = p.x-(prunedImage.getWidth()/80); i<p.x+(prunedImage.getWidth()/80);i++){
				for(int j = p.y-(prunedImage.getHeight()/80); j<p.y+(prunedImage.getHeight()/80);j++){
					try {
						wr.setSample(i, j, 0, 255/2);
					} catch (Exception e) {}
				}
			}
		}
		return testImage;
	}

	private BufferedImage drawHullImage(BufferedImage prunedImage) {
		BufferedImage hullImage = new BufferedImage(prunedImage.getWidth(), prunedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster wr = hullImage.getRaster();
		for(int i=0; i<prunedImage.getWidth(); i++){
			for(int j = 0; j<prunedImage.getHeight(); j++){
				wr.setSample(i, j, 0, prunedImage.getRGB(i, j));
			}
		}
		for(int h=0; h<hullPoints.size(); h++){
			Point2D p = hullPoints.get(h);
			for(int i = p.x()-(prunedImage.getWidth()/80); i<p.x()+(prunedImage.getWidth()/80);i++){
				for(int j = p.y()-(prunedImage.getHeight()/80); j<p.y()+(prunedImage.getHeight()/80);j++){
					try {
						wr.setSample(i, j, 0, 255/2);
					} catch (Exception e) {}
				}
			}
		}
		return hullImage;
	}

	private BufferedImage drawCenterImage(BufferedImage prunedImage) {
		BufferedImage centerImage = new BufferedImage(prunedImage.getWidth(), prunedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster wr = centerImage.getRaster();
		for(int i=0; i<prunedImage.getWidth(); i++){
			for(int j = 0; j<prunedImage.getHeight(); j++){
				wr.setSample(i, j, 0, prunedImage.getRGB(i, j));
			}
		}
		for(int i = centerPoint.x-(prunedImage.getWidth()/15); i<centerPoint.x+(prunedImage.getWidth()/15);i++){
			for(int j = centerPoint.y-(prunedImage.getHeight()/15); j<centerPoint.y+(prunedImage.getHeight()/15);j++){
				wr.setSample(i, j, 0, 255/2);
			}
		}
		return centerImage;
	}

	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				mainInterface.setVisible(true);
			}
		});
		
		inputLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				PreviewDialog dialog = new PreviewDialog(inputImage);
				dialog.setTitle("Input Image");
				dialog.setVisible(true);
			}
		});
		
		grayLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				PreviewDialog dialog = new PreviewDialog(grayImage);
				dialog.setTitle("Grayscale Image");
				dialog.setVisible(true);
			}
		});
		
		sobelLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				PreviewDialog dialog = new PreviewDialog(sobelImage);
				dialog.setTitle("Sobel Edge Detector");
				dialog.setVisible(true);
			}
		});
		
		otsuLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				PreviewDialog dialog = new PreviewDialog(otsuImage);
				dialog.setTitle("Otsu Thresholding");
				dialog.setVisible(true);
			}
		});
		
		dilationLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				PreviewDialog dialog = new PreviewDialog(dilationImage);
				dialog.setTitle("Dilation");
				dialog.setVisible(true);
			}
		});
		
		erosionLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				PreviewDialog dialog = new PreviewDialog(erosionImage);
				dialog.setTitle("Erosion");
				dialog.setVisible(true);
			}
		});
		
		hilditchLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				PreviewDialog dialog = new PreviewDialog(hilditchImage);
				dialog.setTitle("Hilditch's Thinning");
				dialog.setVisible(true);
			}
		});
		
		pruningLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				PreviewDialog dialog = new PreviewDialog(prunedImage);
				dialog.setTitle("Pruning");
				dialog.setVisible(true);
			}
		});
		
		centerDetectLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				PreviewDialog dialog = new PreviewDialog(prunedImage, centerPoint);
				dialog.setTitle("Center Detection");
				dialog.setVisible(true);
			}
		});
		
		grahamLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				PreviewDialog dialog = new PreviewDialog(prunedImage, hullPoints);
				dialog.setTitle("Graham Scan");
				dialog.setVisible(true);
			}
		});
		
		testPointsLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				PreviewDialog dialog = new PreviewDialog(prunedImage, testPoints, true);
				dialog.setTitle("Selecting Test Points");
				dialog.setVisible(true);
			}
		});
		
	}

	private void configureFrame() {
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (ClassNotFoundException | InstantiationException| IllegalAccessException | UnsupportedLookAndFeelException e1) { e1.printStackTrace(); }
		setModal(true);
		setSize(1285, 558);
		setTitle("Methodology");
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		imgSrc = "resource/icon.jpg";
		imgIcn = new ImageIcon(imgSrc);
		image = imgIcn.getImage();
		setIconImage(image);
	}

	public Image convertToImage( BufferedImage bfImage ){
		return bfImage.getScaledInstance(bfImage.getWidth(), bfImage.getHeight(), 0);
	}
}
