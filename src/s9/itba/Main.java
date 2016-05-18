package s9.itba;

public class Main {

	public static void main(String[] args) {
		double dt = 0.02*Math.sqrt(0.01/Math.pow(10,5));
		double dt2 = 0.01;

		Storage s = new Storage(10, 15, 6);
		s.generateRandomParticles(2);
		Simulation sim = new Simulation(0.95, s);
		sim.run(15,dt,dt2);
	}
}
