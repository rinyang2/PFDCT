
public class Counter {
	public int counter = 0;
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public int getCounter() {
		return counter;
	}
	public void tick() {
		counter++;
	}
	public void dTick() {
		counter--;
	}
}
