package s9.itba;

public class Main {

	public static void main(String[] args) {
		double dt = 0.1;
		double max = 10;
		double step = 0;

		Storage s = new Storage(10, 20, 5);
		Simulation sim = new Simulation(dt, max, step, 0.95, s);
		sim.run();

	}

}
