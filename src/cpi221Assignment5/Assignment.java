package cpi221Assignment5;

public class Assignment {
	
	//declare var for total earned and total max
	private int totalEarned;
	private int totalMax;

	//constructor 
	public Assignment(int totalEarned, int totalMax) {
		this.totalEarned = totalEarned;
		this.totalMax = totalMax;
	}

	//getters 
	public int getTotalEarned() {
		return totalEarned;
	}


	public int getTotalMax() {
		return totalMax;
	}

}
