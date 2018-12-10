package dendro;

import imageprocessor.CenterEstimator;
import imageprocessor.Dilation;
import imageprocessor.Erosion;
import imageprocessor.GrayScale;
import imageprocessor.Hilditch;
import imageprocessor.Otsu;
import imageprocessor.PointSelection;
import imageprocessor.Pruning;
import imageprocessor.Sobel;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;

import ConvexHull.GrahamScan;
import ConvexHull.Point2D;

import ui.MainInterface;
import ui.RadiiDialog;
import ui.SimulationFrame;
import util.ModeCandidate;

public class RingAnalyzer {

	int width;
	int height;
	private BufferedImage inputImage, grayImage, sobelImage, otsuImage;
	private double threshold;
	private MainInterface mainInterface;
	
	private double gray[][], magnitude[][];
	private BufferedImage hilditchImage;
	private Point centerPoint;
	private BufferedImage prunedImage;
	private Vector<Point> edgePoints, testPoints;
	private BufferedImage dilationImage;
	private BufferedImage erosionImage;
	private Vector<Point2D> hullPoints;
	private int dilationFactor, pruningFactor;
	private ArrayList<Vector<Point>> groupRings;
	private int mode;
	
	public RingAnalyzer(BufferedImage image, MainInterface mainInterface, int dilationFactor, int pruningFactor) {
		this.mainInterface = mainInterface;
		this.inputImage = image;
		this.dilationFactor = dilationFactor;
		this.pruningFactor = pruningFactor;
		width = inputImage.getWidth();
		height = inputImage.getHeight();
	}

	public void grayScale(){
		gray = GrayScale.grayMap(inputImage);
		
		grayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for (int i=0; i<width; i++) {   
		      for (int j=0; j<height; j++) {
		    	  int rgb = (int)gray[i][j]<<16|(int)gray[i][j]<<8|(int)gray[i][j];
		    	  grayImage.setRGB(i, j, rgb);
		      }
		}
		
	}
	
	public void edgeDetect(){
		Sobel sobel = new Sobel(gray);
		magnitude = sobel.getMagnitude();
		
		sobelImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i=0; i<width; i++) {   
		      for (int j=0; j<height; j++) {
		    	  int rgb = (int)magnitude[i][j]<<16|(int)magnitude[i][j]<<8|(int)magnitude[i][j];
		    	  sobelImage.setRGB(i, j, rgb);
		      }
		}
	}
	
	public void otsuThreshold() {
		threshold = Otsu.otsuTreshold(sobelImage)-10;
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				if(magnitude[i][j]>threshold){
					magnitude[i][j] = 255;
				}
				else{
					magnitude[i][j] = 0;
				}
			}
		}
		
		otsuImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for (int i=0; i<width; i++) {   
		      for (int j=0; j<height; j++) {
		    	  int rgb = (int)magnitude[i][j]<<16|(int)magnitude[i][j]<<8|(int)magnitude[i][j];
		    	  otsuImage.setRGB(i, j, rgb);
		      }
		}
		
	}
	
	public void dilate(){
		Dilation dilation = new Dilation(otsuImage, dilationFactor);
		dilationImage = dilation.getDilatedImmage();
	}
	
	public void erode() {
		Erosion erosion = new Erosion(dilationImage, dilationFactor+1);
		erosionImage = erosion.getErodedImage();
	}

	public void hilditchThin() {
		Hilditch hilditch = new Hilditch(erosionImage);
		hilditchImage = hilditch.thin();
	}

	public void pruning() {
		Pruning ringThresholder = new Pruning(hilditchImage, pruningFactor);
		prunedImage = ringThresholder.threshold();
	}
	
	public void centerCalculation() {
		centerPoint = CenterEstimator.calculateCenterPoint(prunedImage);
	}

	public void pointSelection() {
		edgePoints = PointSelection.selectAllEdgePoints(prunedImage, centerPoint);
	}

	public void grahamScan() {
		pointSelection();
		Point2D[] arrayPoints = new Point2D[edgePoints.size()];
		hullPoints = new Vector<Point2D>();
		for(int i=0; i<edgePoints.size(); i++){
			arrayPoints[i] = new Point2D(edgePoints.get(i).x, edgePoints.get(i).y);
		}
		
		GrahamScan graham = new GrahamScan(arrayPoints);
		for (Point2D p : graham.hull()){
			hullPoints.add(p);
		}
	}
	
	public void testPointSelection() {
		testPoints = PointSelection.selectTestPoints(prunedImage, centerPoint, hullPoints);
	}

	public void getResults() {
		Vector<Integer> rings = new Vector<Integer>();
		groupRings = PointSelection.groupTestPoints(prunedImage, centerPoint, hullPoints);
		for(int arrayListIndex = 0; arrayListIndex<groupRings.size(); arrayListIndex++){
			Vector<Point> ringPoints = groupRings.get(arrayListIndex);
			rings.add(ringPoints.size());
		}
		mainInterface.printResult(getMode(rings));
	}
	
	public int getMode( Vector< Integer > rings ){
		mode = 0;
		int occurence = 0;
		Vector<ModeCandidate> candidates = new Vector<ModeCandidate>();
		boolean isCandidate = true;
		for(int i = 0; i<rings.size(); i++){
			int ringCount = rings.get(i);
			for(int mdIndex = 0; mdIndex<candidates.size(); mdIndex++){
				ModeCandidate candid = candidates.get(mdIndex);
				if(candid.getValue()==ringCount){
					candid.addOccurence();
					isCandidate = false;
				}
			}
			if(isCandidate){
				ModeCandidate newCandidate = new ModeCandidate(ringCount);
				candidates.add(newCandidate);
			}
			isCandidate = true;
		}
		
		for(int i=0; i<candidates.size(); i++){
			ModeCandidate curCandidate = candidates.get(i);
			if(curCandidate.countOccurence()>occurence){
				occurence = curCandidate.countOccurence();
				mode = curCandidate.getValue();
			}
			if(curCandidate.countOccurence()==occurence && curCandidate.getValue()>mode){
				occurence = curCandidate.countOccurence();
				mode = curCandidate.getValue();
			}
		}
		return mode;
	}

	public void methodology() {
		SimulationFrame simulationFrame = new SimulationFrame(inputImage,grayImage, sobelImage, otsuImage, dilationImage,erosionImage, hilditchImage, prunedImage, centerPoint, hullPoints, testPoints, mainInterface, groupRings, mode);
		simulationFrame.setVisible(true);
	}

	public void showRadiiRecord() {
		Vector<Integer> radii = new Vector<>();
		for(int grIndexx = 0; grIndexx<groupRings.size(); grIndexx++){
			Vector<Point> curGroup = groupRings.get(grIndexx);
			if(curGroup.size()==mode){
				for(int cgIndex = 0; cgIndex<curGroup.size(); cgIndex++){
					Point p = curGroup.get(cgIndex);
					int newX = p.x-centerPoint.x;
					int newY = p.y-centerPoint.y;
					int r = (int)Math.sqrt(Math.pow(newX, 2)+Math.pow(newY, 2));
					radii.add(r);
				}
				//CONTINUE HERE!
				for(int cont = grIndexx+1; cont<groupRings.size(); cont++){
					Vector<Point> newGroup = groupRings.get(cont);
					if(newGroup.size()==mode){
						for(int ng=0,rIndex = 0; ng<newGroup.size(); ng++){
							Point p = newGroup.get(ng);
							int newX = p.x-centerPoint.x;
							int newY = p.y-centerPoint.y;
							int r = (int)Math.sqrt(Math.pow(newX, 2)+Math.pow(newY, 2));
							radii.set(rIndex, (radii.get(rIndex)+r)/2);
							rIndex++;
						}
					}
				}
				grIndexx = groupRings.size();
			}
		}
		
		RadiiDialog rd = new RadiiDialog(radii, groupRings, mode, grayImage);
		rd.setVisible(true);
	}
}
