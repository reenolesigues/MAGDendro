package util;

import java.awt.Point;
import java.util.Vector;

public class ModeCandidate {

	private int value;
	private int occurence = 0;
	
	public ModeCandidate(int value) {
		this.value = value;
	}
	
	public int getValue(){ return value; }
	
	public void addOccurence() {
		occurence++;
	}
	
	public int countOccurence() { return occurence; }
}
