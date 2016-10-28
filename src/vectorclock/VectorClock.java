package vectorclock;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.HashMap;

public class VectorClock implements Serializable{

	private HashMap<InetSocketAddress, Integer> vector;
	private InetSocketAddress key;
	
	public VectorClock() {
		vector = new HashMap<>();
	}
	
	public VectorClock(InetSocketAddress inetSocketAddress) {
		vector = new HashMap<>();
		this.key = inetSocketAddress;
		vector.put(key, 1);
		tock();
	}

	public void setKey(InetSocketAddress k) {
		this.key = k;
		vector.put(k, 1);
	}
	
	public HashMap<InetSocketAddress, Integer> getVector() {
		return vector;
	}
	
	public void tock() {
		vector.put(key, vector.get(key) + 1);
	}
	
	public void receiveAction(HashMap<InetSocketAddress, Integer> v) {
			v.put(key, vector.get(key));
			vector = v;
			tock();
	}
	
	public String toString() {
		return "VECTOR CLOCK: [" + vector.toString() + "]";
	}
	
}
