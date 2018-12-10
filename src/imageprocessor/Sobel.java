package imageprocessor;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class Sobel {

	double Gx[][], Gy[][], G[][], angle[][];
	private int width;
	private int height;
	private BufferedImage outputSobel;
	
	public Sobel(double[][] gray) {
		width = gray.length;
		height = gray[0].length;
        double[][] output = new double[width][height];
           
        for(int i = 0 ; i < width ; i++ ){  
            for(int j = 0 ; j < height ; j++ ){  
            	output[i][j] = gray[i][j];
            }
        }   
		
		Gx = new double[width][height];
		Gy = new double[width][height];
		G = new double[width][height];
		angle = new double[width][height];
		
		for (int i=0; i<width; i++) {   
		      for (int j=0; j<height; j++) {   
		        if (i==0 || i==width-1 || j==0 || j==height-1)   
		          Gx[i][j] = Gy[i][j] = G[i][j] = 0; // Image boundary cleared   
		        else{   
		          Gx[i][j] = output[i+1][j-1] + 2*output[i+1][j] + output[i+1][j+1] -   
		          output[i-1][j-1] - 2*output[i-1][j] - output[i-1][j+1];
		          
		          Gy[i][j] = output[i-1][j+1] + 2*output[i][j+1] + output[i+1][j+1] -   
		          output[i-1][j-1] - 2*output[i][j-1] - output[i+1][j-1];
		          
		          G[i][j]  = Math.abs(Gx[i][j]) + Math.abs(Gy[i][j]);
		          
		          angle[i][j] = Math.atan(Gy[i][j]/Gx[i][j]);
		        }   
		      }   
		    }   
		
	}
	public BufferedImage generateEdges(){
		outputSobel = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster wr = outputSobel.getRaster();
		for (int i=0; i<width; i++) {   
		      for (int j=0; j<height; j++) {
		    	  wr.setSample(i,j,0,G[i][j]);
		      }
		}
		return outputSobel;
	}
	
	public double[][] getMagnitude() {return G;}

	public double[][] getAngle() {return angle;}
	
	public double[][] getGX() {return Gx;}
	
	public double[][] getGY() {return Gy;}
	
	public BufferedImage getSobelOutput(){ return outputSobel; }
}
