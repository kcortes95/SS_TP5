package s9.itba;

public class Main {

	public static void main(String[] args) {
		double dt = 0.01;
		double dt2 = 0.1;

		Storage s = new Storage(10, 20, 5);
		s.generateRandomParticles(2);
		Simulation sim = new Simulation(0.95, s);
		sim.run(10,dt,dt2);
	}

}
