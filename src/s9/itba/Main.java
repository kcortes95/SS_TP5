package s9.itba;

public class Main {

	public static void main(String[] args) {
		double dt = 0.1;
		double max = 10;
		double step = 0;

		Storage s = new Storage(10, 20, 5);
		Storage.friccion = 0.95;
		generateParticles(s);

		/*
		 * Esto esta modelado con la fuerza bruta... Obviamente hay que implementar el
		 * Cell Index Method para calcular cuales son los vecinos
		 */
		while (step <= max) {
			for (Particle p1 : s.particles) {
				for(Particle p2 : s.particles){
					if(!p1.equals(p1)){
						p1.collision(s.getW(), s.getL(), s.getD(), p2);
					}
				}
			}
			step += dt;
		}

	}

	private static void generateParticles(Storage s) {
		double radio = s.getD() / 10;
		for (double l = 1; l <= s.maxParticlesY(); l++) {
			for (double w = 1; w <= s.maxParticlesX(); w++) {
				Particle p = new Particle((2 * w - 1) * radio, (2 * l - 1) * radio, radio, radio);
				s.addParticle(p);
			}
		}
	}

}
