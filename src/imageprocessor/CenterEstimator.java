package imageprocessor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.nio.Buffer;
import java.util.Vector;

public class CenterEstimator {

	public CenterEstimator() {}
	
	public static Point calculateCenterPoint(BufferedImage thinEdgeImage){
		int xAcc = 0, yAcc = 0, ctr = 0;
		Raster edgeRaster = thinEdgeImage.getData();
		
		for(int i = 0; i<thinEdgeImage.getWidth(); i++)
		{
			for(int j = 0; j<thinEdgeImage.getHeight(); j++)
			{
				if(edgeRaster.getSample(i,j,0) != 0){
					xAcc+=i;
					yAcc+=j;
					ctr++;
				}
			}
		}
		Point center = new Point(xAcc/ctr, yAcc/ctr);
		return center;
		
//		Vector<Point> edgePoints = new Vector<Point>();
//		Raster edgeRaster = thinEdgeImage.getData();
//		Point centerPoint = new Point(0,0);
//		int leastValue = 100000000;
//		
//		for(int i = 0; i<thinEdgeImage.getWidth(); i++) {
//				for(int j = 0; j<thinEdgeImage.getHeight(); j++){
//					if(edgeRaster.getSample(i,j,0) != 0){
//						edgePoints.add(new Point(i,j));
//					}
//				}
//			}
//		
//		for(int i = 0; i<thinEdgeImage.getWidth(); i++) {
//			for(int j = 0; j<thinEdgeImage.getHeight(); j++){
//				int objValue = 0;
//				for(int pIndex = 0; pIndex<edgePoints.size(); pIndex++){
//					Point curPoint = edgePoints.get(pIndex);
//					objValue+=Math.abs(i - curPoint.x);
//					objValue+=Math.abs(j - curPoint.y);
//				}
//				if(objValue<leastValue){
//					leastValue = objValue;
//					centerPoint.x = i;
//					centerPoint.y = j;
//				}
//			}
//		}
//		
//		return centerPoint;
	}
}
