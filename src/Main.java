import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;

//import main.MainFrame;
import java.awt.Color;
import javax.swing.JProgressBar;

import ui.MainInterface;

public class Main extends JFrame implements Runnable{

	private ImageIcon imgIcon;
	private Thread splashThread;
	private double time = 0;
	private JLabel imgLabel;
	private String imgSrc;
	private ImageIcon imgIcn;
	private Image image;
	private JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) { }
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (ClassNotFoundException | InstantiationException| IllegalAccessException | UnsupportedLookAndFeelException e1) { e1.printStackTrace(); }
		imgSrc = "resource/icon.jpg";
        imgIcn = new ImageIcon(imgSrc);
        image = imgIcn.getImage();
        setIconImage(image);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setSize(400, 200);		
		setLocationRelativeTo(null);
		imgLabel = new JLabel("");
		imgLabel.setDoubleBuffered(true);
		imgLabel.setOpaque(false);
		imgLabel.setBackground(Color.DARK_GRAY);
		imgLabel.setBounds(0, 0, 400, 200);
		
		imgSrc = "resource/splash.jpg";
		imgIcon = new ImageIcon(imgSrc);
		imgLabel.setIcon(imgIcon);
		
		getContentPane().add(imgLabel);
		
		progressBar = new JProgressBar();
		progressBar.setForeground(Color.black);
		progressBar.setStringPainted(true);
		progressBar.setBorderPainted(true);
		progressBar.setString("loading resources...");
		progressBar.setVisible(false);
		
		getContentPane().add(progressBar, BorderLayout.SOUTH);
		
		splashThread = new Thread( this );
		splashThread.start();
	}

	@Override
	public void run() {
		while(time<200){
			time++;
			progressBar.setValue((int)((time/200)*100));
			try { Thread.sleep(10); } catch (Exception e) {e.printStackTrace();}
		}
		this.dispose();
		invokeMain();
	}

	private void invokeMain() {
		MainInterface frame = new MainInterface();
		frame.setVisible(true);
	}
}
