package imageclass;

import java.awt.Color;

public class Pixel {

	public int alpha;		//opacity
	public int red;			//red component
	public int green;		//green component
	public int blue;		//blue component
	
	// constructor
	public Pixel(int argb) {
		Color color = new Color(argb);
		alpha = color.getAlpha();
		red = color.getRed();
        green = color.getGreen();
        blue = color.getBlue();
	}
	//get RED pixel value
	public int getRedValue(){ return red; }
	//get GREEN pixel value
	public int getGreenValue(){ return green; }
	//get BLUE pixel value
	public int getBlueValue(){ return blue; }
}
