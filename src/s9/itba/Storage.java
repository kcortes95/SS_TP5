package s9.itba;

import java.util.ArrayList;
import java.util.List;

public class Storage {

	private double W;
	private double L;
	private double D;
	double friccion;

	List<Particle> particles = null;

	public Storage(double W, double L, double D, double friccion) {
		this.W = W;
		this.L = L;
		this.D = D;
		this.friccion = friccion;
	}

	public Storage(double W, double L, double D) {
		this(W, L, D, 0);
	}

	public double getD() {
		return D;
	}

	public double getL() {
		return L;
	}

	public double getW() {
		return W;
	}

	public void addParticles(List<Particle> particles) {
		this.particles = particles;
	}

	public void addParticle(Particle particle) {
		if (particles == null)
			particles = new ArrayList<Particle>();

		particles.add(particle);
	}

	public int maxParticles() {
		return maxParticlesX()*maxParticlesY();
	}
	
	public int maxParticlesX(){
		return (int)(W / D) * 10;
	}
	
	public int maxParticlesY(){
		return (int)(L / D) * 10;
	}

}
