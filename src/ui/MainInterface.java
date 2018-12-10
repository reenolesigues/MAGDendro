package ui;

import imageclass.ImageMethods;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.Color;

import javax.swing.filechooser.FileFilter;
import javax.swing.JButton;
import javax.swing.JLabel;
import dendro.RingAnalyzer;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.JSpinner;
import javax.swing.border.BevelBorder;

public class MainInterface extends JFrame implements Runnable{
	private JPanel framePane;
	private JLabel lblPreview;
	private JFileChooser chooser;
	private JButton btnChangeImage;
	private JPanel inputPanel;
	private BufferedImage inputImage;
	private Thread mainThread;
	private RingAnalyzer analyzer;

	private String imgSrc;
	private ImageIcon imgIcn;
	private Image image;
	private JLabel lblAgeCount;
	private JLabel lblUrl;
	
	private int dilationFactor, pruningFactor;
	private JButton lblViewMethodology;
	private JProgressBar progressBar;
	private JLabel drop;
	private JSpinner radiusSpinner;
	private JSpinner lengthSpinner;
	private JLabel lblSize;
	
	/**
	 * Create the frame.
	 */
	public MainInterface() {
		setBackground(SystemColor.activeCaptionText);
		configureFrame();
		configureComponents();
		initializeInput();
	}

	//configure components
	private void configureComponents() {
		inputPanel = new JPanel();
		inputPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Input Image", TitledBorder.CENTER, TitledBorder.TOP, null, Color.WHITE));
		inputPanel.setBackground(SystemColor.textInactiveText);
		inputPanel.setBounds(10, 11, 278, 331);
		framePane.add(inputPanel);
		inputPanel.setLayout(null);
		inputPanel.setOpaque(false);
		
		btnChangeImage = new JButton("Change Image");
		btnChangeImage.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.WHITE));
		btnChangeImage.setBackground(SystemColor.window);
		btnChangeImage.setFont(new Font("Verdana", Font.BOLD, 10));
		btnChangeImage.setBounds(13, 296, 115, 25);
		btnChangeImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileFilter filter1 = new ExtensionFileFilter("Images Only - JPG and JPEG", new String[] { "JPG", "JPEG" });
			    chooser.setFileFilter(filter1);
			    chooser.setAcceptAllFileFilterUsed(false);
				if(chooser.showOpenDialog(MainInterface.this)==JFileChooser.APPROVE_OPTION){
					String path = chooser.getSelectedFile().getAbsolutePath();
					String arr[] = path.split("\\\\");
					lblUrl.setText(arr[arr.length-1]);
					inputImage = ImageMethods.readImage(path);
					lblSize.setText(""+inputImage.getWidth()+"x"+inputImage.getHeight());
					radiusSpinner.setModel(new SpinnerNumberModel(1,1,5,1));
					lengthSpinner.setModel(new SpinnerNumberModel(1,1,50,1));
					
					if(inputImage.getWidth()<50||inputImage.getHeight()<50){
						JOptionPane.showMessageDialog(null, "Warning! Image size is very small.");
					}
					else{
						try {					
							BufferedImage resizedImage = new BufferedImage(250, 250, inputImage.getType());						
							Graphics2D g = resizedImage.createGraphics();
							g.drawImage(inputImage, 0, 0, 250, 250, null);
							g.dispose();
							lblPreview.setIcon(new ImageIcon(resizedImage));
							lblAgeCount.setText("");
							lblViewMethodology.setVisible(false);
							ImageMethods.getPixelData(inputImage);
						} catch (Exception e) {}
					}					
				}
			}
		});
		inputPanel.add(btnChangeImage);
		
		lblPreview = new JLabel("preview");
		lblPreview.setBackground(Color.GRAY);
		lblPreview.setBounds(13, 27, 250, 250);
		inputPanel.add(lblPreview);
		
		lblUrl = new JLabel("url");
		lblUrl.setBackground(SystemColor.textInactiveText);
		lblUrl.setForeground(Color.WHITE);
		lblUrl.setFont(new Font("Verdana", Font.BOLD, 10));
		lblUrl.setBounds(13, 280, 250, 14);
		inputPanel.add(lblUrl);
		
		lblSize = new JLabel("size");
		lblSize.setVisible(false);
		lblSize.setForeground(Color.WHITE);
		lblSize.setFont(new Font("Verdana", Font.BOLD, 10));
		lblSize.setBackground(SystemColor.textInactiveText);
		lblSize.setBounds(47, 291, 54, 14);
		inputPanel.add(lblSize);
		
		JLabel lblSize_1 = new JLabel("Size: ");
		lblSize_1.setVisible(false);
		lblSize_1.setForeground(Color.WHITE);
		lblSize_1.setFont(new Font("Verdana", Font.BOLD, 10));
		lblSize_1.setBounds(13, 291, 46, 14);
		inputPanel.add(lblSize_1);
		
		JPanel parameterPanel = new JPanel();
		parameterPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Parameters", TitledBorder.CENTER, TitledBorder.TOP, null, Color.WHITE));
		parameterPanel.setBackground(SystemColor.textInactiveText);
		parameterPanel.setBounds(298, 11, 280, 132);
		framePane.add(parameterPanel);
		parameterPanel.setLayout(null);
		parameterPanel.setOpaque(false);
		
		JButton btnAnalyze = new JButton("Analyze");
		btnAnalyze.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.WHITE));
		btnAnalyze.setBackground(SystemColor.window);
		btnAnalyze.setFont(new Font("Verdana", Font.BOLD, 10));
		btnAnalyze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dilationFactor = (int)radiusSpinner.getValue();
				pruningFactor = (int)lengthSpinner.getValue();
				preprocess();
				lblAgeCount.setText("waiting for result...");
					
			}
		});
		btnAnalyze.setBounds(92, 98, 103, 23);
		parameterPanel.add(btnAnalyze);
		
		JLabel lblEdgeDilation = new JLabel("Dilation/Erosion Radius: ");
		lblEdgeDilation.setForeground(Color.WHITE);
		lblEdgeDilation.setBackground(SystemColor.textInactiveText);
		lblEdgeDilation.setFont(new Font("Verdana", Font.BOLD, 10));
		lblEdgeDilation.setBounds(40, 26, 141, 23);
		parameterPanel.add(lblEdgeDilation);
		
		JLabel lblPruningLength = new JLabel("Pruning Length:");
		lblPruningLength.setForeground(Color.WHITE);
		lblPruningLength.setBackground(SystemColor.textInactiveText);
		lblPruningLength.setFont(new Font("Verdana", Font.BOLD, 10));
		lblPruningLength.setBounds(40, 64, 103, 23);
		parameterPanel.add(lblPruningLength);
		
		radiusSpinner = new JSpinner();
		radiusSpinner.setFont(new Font("Verdana", Font.BOLD, 11));
		radiusSpinner.setBounds(191, 23, 43, 28);
		radiusSpinner.setModel(new SpinnerNumberModel(1,1,5,1));
		parameterPanel.add(radiusSpinner);
		
		lengthSpinner = new JSpinner();
		lengthSpinner.setFont(new Font("Verdana", Font.BOLD, 11));
		lengthSpinner.setBounds(191, 61, 43, 28);
		lengthSpinner.setModel(new SpinnerNumberModel(1,1,50,1));
		parameterPanel.add(lengthSpinner);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Result", TitledBorder.CENTER, TitledBorder.TOP, null, Color.WHITE));
		panel.setBackground(SystemColor.textInactiveText);
		panel.setBounds(298, 203, 278, 74);
		framePane.add(panel);
		panel.setLayout(null);
		panel.setOpaque(false);
		
		JLabel lblAge = new JLabel("Approximate Age: ");
		lblAge.setForeground(Color.WHITE);
		lblAge.setBackground(SystemColor.textInactiveText);
		lblAge.setFont(new Font("Verdana", Font.BOLD, 10));
		lblAge.setBounds(10, 15, 122, 27);
		panel.add(lblAge);
		
		lblAgeCount = new JLabel("???");
		lblAgeCount.setBackground(SystemColor.textInactiveText);
		lblAgeCount.setForeground(Color.WHITE);
		lblAgeCount.setFont(new Font("Verdana", Font.BOLD, 12));
		lblAgeCount.setBounds(129, 14, 126, 27);
		panel.add(lblAgeCount);
		
		lblViewMethodology = new JButton("Show Processing");
		lblViewMethodology.setBackground(SystemColor.textInactiveText);
		lblViewMethodology.setForeground(Color.RED);
		lblViewMethodology.setVisible(false);
		lblViewMethodology.setFont(new Font("Verdana", Font.BOLD, 10));
		lblViewMethodology.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				analyzer.methodology();
			}
		});
		lblViewMethodology.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblViewMethodology.setForeground(Color.BLACK);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblViewMethodology.setForeground(Color.RED);
			}
		});
		lblViewMethodology.setBounds(68, 47, 166, 16);
		panel.add(lblViewMethodology);
		chooser = new JFileChooser();
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.textInactiveText);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Status", TitledBorder.CENTER, TitledBorder.TOP, null, Color.WHITE));
		panel_1.setBounds(298, 142, 280, 61);
		framePane.add(panel_1);
		panel_1.setLayout(null);
		panel_1.setOpaque(false);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 21, 260, 29);
		panel_1.add(progressBar);
		progressBar.setForeground(new Color(128, 0, 0));
		progressBar.setStringPainted(true);
		progressBar.setBorderPainted(true);
		
		framePane.add(drop);
	}

	
	//FLOW STARTS HERE
	private void preprocess() {
		analyzer = new RingAnalyzer(inputImage, this, dilationFactor, pruningFactor);
		mainThread = new Thread(this);
		mainThread.start();
	}

	//default tree cross section
	private void initializeInput(){		
		String initPath = "dataset/Sample 01-13.jpg";
		String arr[] = initPath.split("/");
		lblUrl.setText(arr[arr.length-1]);
		inputImage = ImageMethods.readImage(initPath);
		lblSize.setText(""+inputImage.getWidth()+"x"+inputImage.getHeight());
		lblPreview.setIcon(new ImageIcon(inputImage.getScaledInstance(250, 250, 0)));
	}
	
	//configure main frame
	private void configureFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {}
		
		imgSrc = "resource/icon.jpg";
		imgIcn = new ImageIcon(imgSrc);
		image = imgIcn.getImage();
		setIconImage(image);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(591,378);
		setLocationRelativeTo(null);
		setTitle("Digital Dendrochronology");
		setResizable(false);
		framePane = new JPanel();
		framePane.setBackground(SystemColor.inactiveCaptionText);
		framePane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(framePane);
		framePane.setLayout(null);
		
		drop = new JLabel();
		drop.setBounds(0,0,585,368);
		drop.setOpaque(false);
		imgSrc = "resource/drop.jpg";
		imgIcn = new ImageIcon(imgSrc);
		image = imgIcn.getImage();
		drop.setIcon(imgIcn);
	}
	
	public void printResult(Integer age) {
		lblAgeCount.setText(""+age+" YEAR(S)");
	} 


	@Override
	public void run() {
		boolean isRunning = true;
		while(isRunning){
			lblViewMethodology.setVisible(false);
			radiusSpinner.setEnabled(false);
			lengthSpinner.setEnabled(false);
				analyzer.grayScale();
				progressBar.setValue(10);
				
				analyzer.edgeDetect();
				progressBar.setValue(20);
				
				analyzer.otsuThreshold();
				progressBar.setValue(30);
			
				analyzer.dilate();
				progressBar.setValue(40);
				
				analyzer.erode();
				progressBar.setValue(50);
				
				analyzer.hilditchThin();
				progressBar.setValue(60);
			
				analyzer.pruning();
				progressBar.setValue(70);
			
				analyzer.centerCalculation();
				progressBar.setValue(80);
			
				analyzer.grahamScan();
				progressBar.setValue(90);
			
				analyzer.testPointSelection();
				progressBar.setValue(95);
				
				analyzer.getResults();
				progressBar.setValue(100);
				isRunning = false;
			}
			lblViewMethodology.setVisible(true);
			radiusSpinner.setEnabled(true);
			lengthSpinner.setEnabled(true);
		} 
}
