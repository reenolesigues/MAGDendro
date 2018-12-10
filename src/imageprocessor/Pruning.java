package imageprocessor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.Stack;


public class Pruning {

	int width;
	int height;
	int pruningFactor;
	int[][] edgeMap;
	Stack<Point> members;
	ArrayList<Stack<Point>> groups = new ArrayList<Stack<Point>>();
	
	public Pruning(BufferedImage hilditchImage, int pruningFactor) {
		width = hilditchImage.getWidth();
		height = hilditchImage.getHeight();
		this.pruningFactor = pruningFactor;
		edgeMap = new int[width][height];
		Raster edgeRaster = hilditchImage.getData();
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				if (edgeRaster.getSample(i,j,0) == 0)
					edgeMap[i][j] = 0;
				else
					edgeMap[i][j] = 1;
			}
		}
		
		filterLength();
	}

	private void filterLength() {
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				if(edgeMap[i][j]==1){
					members = new Stack<Point>();
					pushConnectedToStack(i,j);
					groups.add(members);
//					if(members.size()>pruningFactor){
//						for(int in = 0; in<members.size(); in++)
//							edgeMap[members.get(in).x][members.get(in).y] = 1;
//					}
				}
			}
		}
		for(int ctr = 0; ctr<groups.size(); ctr++){
			Stack<Point> curStack = groups.get(ctr);
			if(curStack.size()>pruningFactor){
				for(int index = 0; index<curStack.size(); index++)
					edgeMap[curStack.get(index).x][curStack.get(index).y] = 1;
			}
		}
	}

	private void pushConnectedToStack(int i, int j) {
		members.push(new Point(i,j));
		edgeMap[i][j] = 0;
		try {
			if(edgeMap[i][j-1]==1) pushConnectedToStack(i, j-1); 
			if(edgeMap[i+1][j-1]==1) pushConnectedToStack(i+1, j-1); 
			if(edgeMap[i+1][j]==1) pushConnectedToStack(i+1, j); 
			if(edgeMap[i+1][j+1]==1) pushConnectedToStack(i+1, j+1); 
			if(edgeMap[i][j+1]==1) pushConnectedToStack(i, j+1); 
			if(edgeMap[i-1][j+1]==1) pushConnectedToStack(i-1, j+1);
			if(edgeMap[i-1][j]==1) pushConnectedToStack(i-1, j);
		} catch (Exception e) { } 
	}

	public BufferedImage threshold() {
		BufferedImage thresholdedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				if(edgeMap[i][j]>0){
					int rgb = 255<<16|255<<8|255;
			    	thresholdedImage.setRGB(i, j, rgb);
				}
				else{
					int rgb = 0<<16|0<<8|0;
			    	thresholdedImage.setRGB(i, j, rgb);
				}
			}
		}
		return thresholdedImage;
	}
}
