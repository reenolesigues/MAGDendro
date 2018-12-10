package imageclass;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;

public class ImageMethods {
	
	public static Pixel[][] getPixelData(BufferedImage image){		
		int height = image.getHeight();
        int width = image.getWidth();
        Pixel[][] pixelarray = new Pixel[width][height];
        for(int i=0; i<width; i++){
        	for(int j=0; j<height; j++){
                pixelarray[i][j] = new Pixel(image.getRGB(i, j));                
        	}
        }        
        return pixelarray;
	}

	public static BufferedImage convertToGrayScale(BufferedImage image){
		//Create a gray level image of the same size.
		BufferedImage gray = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		//Get the graphics context for the gray level image.
		Graphics2D g2d = gray.createGraphics();
		// Render the input image on it.
		g2d.drawImage(image,0,0,null);
		// Store the resulting image using the PNG format.	       
		//ImageIO.write(gray,"JPG",new File("gray.jpg"));
		return gray;
	}
	
	public static BufferedImage convertToBinaryImage(BufferedImage image){
		BufferedImage bin = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D g2d = bin.createGraphics();
		g2d.drawImage(image,0,0,null);
		return bin;
	}
	
	public static void saveBufferedImageToFile(BufferedImage image, String filename){
		try {
			Graphics2D g2d = image.createGraphics();
			g2d.drawImage(image,0,0,null);	       
			ImageIO.write(image,"JPG",new File(filename+".jpg"));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static BufferedImage blur(BufferedImage image){
		float[] matrix = {
		        0.111f, 0.111f, 0.111f, 
		        0.111f, 0.111f, 0.111f, 
		        0.111f, 0.111f, 0.111f,
		    };
		BufferedImageOp op = new ConvolveOp(new Kernel(7, 1, matrix));
		BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage convolvedImage = op.filter(image, output);
		return convolvedImage;
	}
	
	public static BufferedImage horizontalEdgeDetect(BufferedImage image){
		float[] matrix = {
				1,2,1,
				0,0,0,
				-1,-2,-1,
		    };
		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrix));
		BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage convolvedImage = op.filter(image, output);
		return convolvedImage;
	}
	
	public static BufferedImage verticalEdgeDetect(BufferedImage image){
		float[] matrix = {
				-1,0,1,
				-2,0,2,
				-1,0,1,
		    };
		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrix));
		BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage convolvedImage = op.filter(image, output);
		return convolvedImage;
	}
	
	public static BufferedImage diagonalEdgeDetect(BufferedImage image){
		float[] matrix = {
				-1,0,0,
				0,1,0,
				0,0,0,
		    };
		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrix));
		BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage convolvedImage = op.filter(image, output);
		return convolvedImage;
	}
	
	public static BufferedImage laPlacianFilter(BufferedImage image){
		float[] matrix = {
				-1,2,-1,
				2,-4,2,
				-1,2,-1,
		    };
		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrix));
		BufferedImage convolvedImage = op.filter(image, null);
		return convolvedImage;
	}
	
	public static BufferedImage horizontalGradient(BufferedImage image){
		float[] matrix = {
				1,0,-1,
		    };
		BufferedImageOp op = new ConvolveOp(new Kernel(1, 3, matrix));
		BufferedImage convolvedImage = op.filter(image, null);
		return convolvedImage;
	}
	
	public static BufferedImage readImage(String fileLocation) {
		BufferedImage img = null;
		try {
			FileInputStream inFile = new FileInputStream(fileLocation);   
	        img = ImageIO.read(inFile);
//			img = ImageIO.read(new File(fileLocation));
		} catch (Exception e) { e.printStackTrace(); }
		return img;
	}

	public static int[] getPixelArray(BufferedImage image) {
		BufferedImage binImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D g2d = binImg.createGraphics();
		g2d.drawImage(image,0,0,null);
		
		int[] imageArray = new int[ binImg.getWidth() * binImg.getHeight() ];
		PixelGrabber grabber = new PixelGrabber(binImg, 0, 0, binImg.getWidth(), binImg.getHeight(), imageArray, 0, binImg.getWidth());
		try{
			grabber.grabPixels();
		}
		catch( InterruptedException pixelGrabberE ){
			System.out.println( pixelGrabberE );
		}
		

		return imageArray;
	}
	
public static int rgb2hsv(int r, int g, int b, int hsv[]) {
		
		int min;    //Min. value of RGB
		int max;    //Max. value of RGB
		int delMax; //Delta RGB value
		
		if (r > g) { min = g; max = r; }
		else { min = r; max = g; }
		if (b > max) max = b;
		if (b < min) min = b;
								
		delMax = max - min;
	 
		float H = 0, S;
		float V = max;
		   
		if ( delMax == 0 ) { H = 0; S = 0; }
		else {                                   
			S = delMax/255f;
			if ( r == max ) 
				H = (      (g - b)/(float)delMax)*60;
			else if ( g == max ) 
				H = ( 2 +  (b - r)/(float)delMax)*60;
			else if ( b == max ) 
				H = ( 4 +  (r - g)/(float)delMax)*60;   
		}
								 
		hsv[0] = (int)(H);
		hsv[1] = (int)(S*100);
		hsv[2] = (int)(V*100);
		
		return hsv[1];
	}
	
}
