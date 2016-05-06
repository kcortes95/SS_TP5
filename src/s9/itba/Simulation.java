package s9.itba;

public class Simulation {

	double dt = 0;
	double max = 0;
	double step = 0;
	double friccion = 0;
	Storage s = null;

	public Simulation(double dt, double max, double step, double friccion, Storage s) {
		this.dt = dt;
		this.max = max;
		this.step = step;
		this.s = s;
		s.friccion = friccion;
	}

	public void run() {
		generateParticles(s);

		/*
		 * Esto esta modelado con la fuerza bruta... Obviamente hay que
		 * implementar el Cell Index Method para calcular cuales son los vecinos
		 */
		while (step <= max) {
			for (Particle p1 : s.particles) {
				for (Particle p2 : s.particles) {
					if (!p1.equals(p1)) {
						System.out.println("entro");
						p1.collision(s.getW(), s.getL(), s.getD(), p2);
					}
				}
				p1.vx = p1.vx + p1.xForce * dt / p1.mass;
				p1.vy = p1.vy + p1.yForce * dt / p1.mass;
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
