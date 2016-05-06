package s9.itba;

public class Main {

	public static void main(String[] args) {
		Storage s = new Storage(10, 20, 5);
		Storage.friccion = 0.95;
		generateParticles(s);
	}

	private static void generateParticles(Storage s) {
		double radio = s.getD() / 10;
		for (double l = 1; l <= s.maxParticlesY(); l++) {
			for (double w = 1; w <= s.maxParticlesX(); w++) {
				Particle p = new Particle((2*w-1)*radio, (2*l-1)*radio, radio, radio);
				s.addParticle(p);
			}
		}
	}

}
