package imageprocessor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.Vector;

import ConvexHull.Point2D;

public class PointSelection {

	public static Vector<Integer> rings;
	
	public static Vector<Point> selectAllEdgePoints(BufferedImage thinImage, Point centerPoint){
		Vector<Point> points = new Vector<Point>();
		int width = thinImage.getWidth();
		int height = thinImage.getHeight();
		int[][] edgeData = new int[width][height];
		Raster edgeRaster = thinImage.getData();
		for(int i = 0; i<width; i++){
			for(int j = 0; j<height; j++){
				edgeData[i][j] = edgeRaster.getSample(i,j,0) == 0 ? 0 : 1;
			}
		}
		//TAKES ALL POINTS IN THE EDGEMAP
		for(int i = 0; i<width; i++){
			for(int j=0; j<height; j++)
			if(edgeData[i][j]==1){
				points.add(new Point(i, j)); 
			}
		}
		
		return points;
		
	}

	public static Vector<Point> selectTestPoints(BufferedImage prunedImage, Point centerPoint, Vector<Point2D> hullPoints) {
		rings = new Vector<Integer>(); //vector of detected ring counts from the center to various hullpoints
		Vector<Point> testPoints = new Vector<Point>();
		Raster raster = prunedImage.getData();
		for(int i = 0; i<hullPoints.size(); i++){
			Point curPoint = new Point(hullPoints.get(i).x(), hullPoints.get(i).y());
			Vector<Point> betPoints = getInBetweenPoints(centerPoint, curPoint);
			int ringCount = 0;
			for(int inBetIndex = 0; inBetIndex<betPoints.size(); inBetIndex++){
				Point cp = betPoints.get(inBetIndex);
				if(raster.getSample(cp.x, cp.y, 0)!=0){
					testPoints.add(cp);
					ringCount++;
					inBetIndex++;
				}
			}
			rings.add(ringCount);
		}
		return testPoints;
	}
	
	public static Vector<Point> getInBetweenPoints(Point center, Point point) {
		Vector<Point> inBetweenPoints = new Vector<Point>();
		double xPointNew = point.x-center.x;
		double yPointNew = point.y-center.y;
		double slope = (yPointNew)/(xPointNew);
		double b = yPointNew - ((slope)*(xPointNew));
		
		if(xPointNew>0){
			for(double x = xPointNew; x>0; x--){
				double y = slope*x+b;
				inBetweenPoints.add(new Point((int)x+center.x, (int)y+center.y));
			}
		}
		
		else if(xPointNew<=0){
			for(double x = xPointNew; x<=0; x++){
				double y = slope*x+b;
				inBetweenPoints.add(new Point((int)x+center.x, (int)y+center.y));
			}
		}
		
		return inBetweenPoints;
	}
	
//	public static Vector<Point> getInBetweenPoints(Point center, Point point) {
//		Vector<Point> inBetweenPoints = new Vector<Point>();
//		double xPointNew = point.x-center.x;
//		double yPointNew = point.y-center.y;
//		double slope = (yPointNew)/(xPointNew);
//		double b = yPointNew - ((slope)*(xPointNew));
//		System.out.println("origX:"+point.x+"      origY:"+point.y);
//		System.out.println("centerX:"+center.x+"          centerY:"+center.y);
//		System.out.println("xNew:"+xPointNew+"        yNew:"+yPointNew);
//		System.out.println("slope:"+slope);
//		System.out.println("B:"+b);
//		
//		for(int x = 1; x<xPointNew; x++){
//			double y = (slope*x)+b;
//			inBetweenPoints.add(new Point((int)(x+Math.abs(xPointNew)), (int)(y+Math.abs(yPointNew))));
//			System.out.println("x:"+(x+Math.abs(xPointNew))+"      y:"+(y+Math.abs(yPointNew)));
//		}
//		for(int x = -1; x>xPointNew; x--){
//			double y = slope*x+b;
//			inBetweenPoints.add(new Point((int)(x+Math.abs(xPointNew)), (int)(y+Math.abs(yPointNew))));
//		}
//		
//		return inBetweenPoints;
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static ArrayList<Vector<Point>> groupTestPoints(BufferedImage prunedImage, Point centerPoint, Vector<Point2D> hullPoints) {
		ArrayList<Vector<Point>> toReturn = new ArrayList<Vector<Point>>();
		Raster raster = prunedImage.getData();
		
		for(int hullIndex = 0; hullIndex<hullPoints.size(); hullIndex++){
			Point curPoint = new Point(hullPoints.get(hullIndex).x(), hullPoints.get(hullIndex).y());
			Vector<Point> betPoints = getInBetweenPoints(centerPoint, curPoint);
			Vector<Point> edgePoints = new Vector<Point>();
			
			for(int betIndex = 0; betIndex<betPoints.size(); betIndex++){
				Point cp = betPoints.get(betIndex);
				try {
					if(raster.getSample(cp.x, cp.y, 0)!=0){
						edgePoints.add(cp);
					}
				} catch (Exception e) {  }
			}
			toReturn.add(edgePoints);
		}
		
		return toReturn;
	}
	
}
