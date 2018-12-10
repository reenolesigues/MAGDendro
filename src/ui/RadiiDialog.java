package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class RadiiDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel tableModel;
	private String[] headers = new String[]{ "<html><b>Ring Number</b></html>", "<html><b>Radius From Center</b></html>", "<html><b>Width From Inner Ring</b></html>"};
	private String[] rowData;
	private JScrollPane scrollPane;
	private  Vector<Integer> relativeRadius = new Vector<Integer>();
	private Vector<Integer> radii;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private GraphPanel g;
	private ArrayList<Vector<Point>> groupRings;
	private int mode;
	private BufferedImage grayImage;
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
	public RadiiDialog(Vector<Integer> radii, ArrayList<Vector<Point>> groupRings, int mode, BufferedImage grayImage) {
		this.radii = radii;
		this.groupRings = groupRings;
		this.mode = mode;
		this.grayImage = grayImage;
		configure();
		preCalculate();
		printRadiiData();
	}

	private void preCalculate() {
		try {
			tableModel = new DefaultTableModel(){
				@Override
				public boolean isCellEditable(int arg0, int arg1) {
					return false;
				}
			};
			table = new JTable(tableModel);
			table.getTableHeader().setReorderingAllowed(false);
			table.setShowGrid(false);
			table.setShowVerticalLines(false);
			table.setRowSelectionAllowed(true);
			for(int i=0; i<headers.length;i++){
				tableModel.addColumn(headers[i]);
			}
			tableModel.setColumnCount(3);
			table.setRowHeight(20);
			
			for(int  i=radii.size()-1, ctr = 1; i>=0; i--, ctr++){
				int prevR = 0;
				if(i==radii.size()-1)
					prevR=0;
				else if(i<radii.size()-1)
					prevR = radii.get(i+1);
				else
					prevR = radii.get(radii.size()-1);
				int rR = (radii.get(i)-prevR);
				relativeRadius.add(rR);
				rowData = new String[]{"Ring "+ctr,""+radii.get(i),""+rR};
				tableModel.addRow(rowData);
			}
			
			table.updateUI();
		} catch (Exception e) {e.printStackTrace();}
		
		g = new GraphPanel(relativeRadius, groupRings, mode, grayImage);
	}

	private void printRadiiData() {
		scrollPane.setViewportView(table);
	}

	private void configure() {
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (ClassNotFoundException | InstantiationException| IllegalAccessException | UnsupportedLookAndFeelException e1) { e1.printStackTrace(); }
		setModal(true);
		setSize(484, 335);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		imgSrc = "resource/icon.jpg";
		imgIcn = new ImageIcon(imgSrc);
		image = imgIcn.getImage();
		setIconImage(image);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 450, 250);
		contentPanel.add(scrollPane);
		
		JRadioButton rdbtnTabularView = new JRadioButton("Tabular View");
		rdbtnTabularView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				printRadiiData();
			}
		});
		buttonGroup.add(rdbtnTabularView);
		rdbtnTabularView.setSelected(true);
		rdbtnTabularView.setBounds(10, 265, 97, 23);
		contentPanel.add(rdbtnTabularView);
		
		JRadioButton rdbtnGraphicalView = new JRadioButton("Graphical View");
		rdbtnGraphicalView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				drawRadiiData();
			}
		});
		buttonGroup.add(rdbtnGraphicalView);
		rdbtnGraphicalView.setBounds(110, 265, 109, 23);
		contentPanel.add(rdbtnGraphicalView);
	}

	protected void drawRadiiData() {
		scrollPane.setViewportView(g);
		g.adjust();
	}
}
