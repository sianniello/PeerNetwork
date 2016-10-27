package vectorclock;

import java.util.ArrayList;

public class VectorClock {

	private ArrayList<Integer> vector;
	private int index; //indice modificabile del vettore

	public VectorClock() {
		vector = new ArrayList<Integer>();
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void tick() {
		vector.set(index, vector.get(index) + 1);
	}

	public void add() {
		vector.add(1);
	}

	public String toString() {
		String output = "";
		for(int v : vector)
			output = output + "[" + vector.indexOf(v) + " - " + v + "]";
		return output;
	}

	public int getSize() {
		return vector.size();
	}

	public ArrayList<Integer> getVector() {
		return vector;
	}

	public void receiveAction(VectorClock rvc) {
		if(rvc.getSize() > vector.size()) {
			vector = rvc.getVector();
			this.add();
		}
	}

}
