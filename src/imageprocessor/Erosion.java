package imageprocessor;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class Erosion{
        
	private int width, height;
	private int radius;
	private BufferedImage outputImage;
	
	public Erosion(BufferedImage image, int radius) {
		this.radius = radius;
		width = image.getWidth();
		height = image.getHeight();
		outputImage = new BufferedImage(width, height, image.getType());
		
		WritableRaster outRaster = outputImage.getRaster();
		Raster edgeRaster = image.getData(); 
		
		for(int i = 0; i<width; i++){
			for(int j=  0; j<height; j++){
				if(edgeRaster.getSample(i,j,0) == 0){
					int rgb1 = 0<<16|0<<8|0;
  		    	  	outputImage.setRGB(i, j, rgb1);
					
					for(int l = i-radius; l<i+radius; l++){
						int remK = radius-Math.abs(i-l);
						for (int m=j-remK; m<=j+remK; m++){
	                        if (l>=0 && m>=0 && l<width && m<height && edgeRaster.getSample(l,m,0) != 0){
	                        	int rgb = 0<<16|0<<8|0;
	          		    	  	outputImage.setRGB(l, m, rgb);
	                        }
	                    }
					}
				}
				else{
					int rgb1 = 255<<16|255<<8|255;
  		    	  	outputImage.setRGB(i, j, rgb1);
				}
			}
		}
	}
	
	public BufferedImage getErodedImage(){ return outputImage; }

}