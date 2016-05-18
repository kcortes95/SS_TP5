package s9.itba;

import java.util.HashSet;
import java.util.Set;

public class Storage {

	private double W;
	private double L;
	private double D;
	double friccion;

	private Set<Particle> particles = null;

	public Storage(double W, double L, double D, double friccion) {
		this.W = W;
		this.L = L;
		this.D = D;
		this.friccion = friccion;
		this.particles = new HashSet<Particle>();
	}

	public Storage(double W, double L, double D) {
		this(W, L, D, 0);
	}
	
	public void generateRandomParticles(double time){
		double start = System.currentTimeMillis();
		while(System.currentTimeMillis()-start<time*1000){
			Particle p = generateRandomPos();
			if(isValidPos(p)){
				particles.add(p);
			}
		}
	}
	
	private Particle generateRandomPos(){
		double radius = D/20;
		double x = Math.random()*(W-2*radius)+radius;
		double y = Math.random()*(L-2*radius)+radius;
		return new Particle(x, y, radius, Simulation.mass);
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
	
	public Set<Particle> getParticles() {
		return particles;
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
	
	public boolean isValidPos(Particle p){
		for(Particle p2: particles){
			if(p.getSuperposition(p2)>0)
				return false;
		}
		return true;
	}
}
