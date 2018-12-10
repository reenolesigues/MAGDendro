package imageprocessor;

import java.awt.Color;
import java.awt.image.BufferedImage;
public class GrayScale {

	public GrayScale() {}
	
	public static double[][] grayMap(BufferedImage image){
		
		if(image.getType() == BufferedImage.TYPE_BYTE_GRAY)
			System.out.println("GRAYIMAGE!");
		
		int width = image.getWidth();
		int height = image.getHeight();
		double[][] output = new double[width][height];
		
        for(int i = 0 ; i < width ; i++ ){   
            for(int j = 0 ; j < height ; j++ ){ 
            	Color pixelColor = new Color(image.getRGB(i, j)); 
                output[i][j] = (0.257*pixelColor.getRed())+(0.504*pixelColor.getGreen())+(0.098*pixelColor.getBlue())+16;
            }
        }
		
		return output;
	}
}
