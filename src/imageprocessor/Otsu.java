package imageprocessor;

import java.awt.Color;
import java.awt.image.BufferedImage;

	public class Otsu {

	public static int otsuTreshold(BufferedImage original) {
		 
	    int[] histogram = imageHistogram(original);
	    int total = original.getHeight() * original.getWidth();
	 
	    float sum = 0;
	    for(int i=0; i<256; i++) sum += i * histogram[i];
	 
	    float sumB = 0;
	    int wB = 0;
	    int wF = 0;
	 
	    float varMax = 0;
	    int threshold = 0;
	 
	    for(int i=0 ; i<256 ; i++) {
	        wB += histogram[i];
	        if(wB == 0) continue;
	        wF = total - wB;
	 
	        if(wF == 0) break;
	 
	        sumB += (float) (i * histogram[i]);
	        float mB = sumB / wB;
	        float mF = (sum - sumB) / wF;
	 
	        float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
	 
	        if(varBetween > varMax) {
	            varMax = varBetween;
	            threshold = i;
	        }
	    }
	 
	    return threshold;
	 
	}
	
	 public static int[] imageHistogram(BufferedImage input) {
		 
	        int[] histogram = new int[256];
	 
	        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
	 
	        for(int i=0; i<input.getWidth(); i++) {
	            for(int j=0; j<input.getHeight(); j++) {
	                int red = new Color(input.getRGB (i, j)).getRed();
	                histogram[red]++;
	            }
	        }
	 
	        return histogram;
	 
	    }
	
	}