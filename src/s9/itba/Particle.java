package s9.itba;

import java.util.*;

public class Particle {

	double rx, ry;
	double vx, vy;
	double ax, ay;
	double radio;
	double mass;

	double xForce = 0;
	double yForce = 0;

	double kn, kt;

	int id = 0;
	private static int idPart = 0;

	// Agrego todas las particulas que colisionaron en un cierto dt
	List<Particle> particlesColided = new ArrayList<>();

	public Particle(double rx, double ry, double radio, double mass) {
		this.rx = rx;
		this.ry = ry;
		this.radio = radio;
		this.mass = mass;
		id = idPart;
		idPart++;
	}

	public Particle(double rx, double ry, double vx, double vy, double radio, double mass) {
		this(rx, ry, radio, mass);
		this.vx = vx;
		this.vy = vy;
	}

	public Particle(double rx, double ry, double vx, double vy, double ax, double ay, double radio, double mass) {
		this(rx, ry, vx, vy, radio, mass);
		this.ax = ax;
		this.ay = ay;
	}

	public double calculatePressure(double friction, double D) {
		return 1 - Math.exp((-4 * friction * rx) / D);
	}

	public double getVelocity() {
		return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
	}

	public double getRelVelocity(Particle p) {
		return p.getVelocity() - this.getVelocity();
	}

	/*
	 * Devuelve cual es el angulo que forman las dos particulas respecto a sus
	 * posiciones Esto lo hacemos para despues poder calcular cuales son las
	 * fuerzas en X y en Y (
	 */
	public double getAngle(Particle p) {
		return Math.atan2(p.ry - this.ry, p.rx - this.rx);
	}

	private boolean collision(Particle p) {
		if (getSuperposition(p) >= 0) {
			particlesColided.add(p);
			double normal = calculateNormalForce(p, kn);
			double tangencial = calculateTanForce(p, kt);
			xForce += normal * Math.cos(getAngle(p)) + tangencial * Math.sin(getAngle(p));
			yForce += normal * Math.sin(getAngle(p)) + tangencial * Math.cos(getAngle(p));
			return true;
		}
		return false;
	}

	/*
	 * Si colisiono con una pared, la fuerza cambia
	 * de sentido
	 */
	private boolean collisionWall(double W, double L, double D) {
		boolean state = false;
		if (this.rx - radio == 0 || this.rx + radio == L) {
			xForce = -1 * xForce;
			state = true;
		}

		double posY = this.ry + radio;
		double bound1 = (W - D) / 2;
		double bound2 = (W + D) / 2;
		if ((posY >= 0 && posY <= bound1) || (posY >= bound2 && posY <= W)) {
			yForce = -1 * yForce;
			state = true;
		}
		return state;
	}

	public boolean collision(double W, double L, double D, Particle p) {
		boolean cParticle = collision(p);
		boolean cWall = collisionWall(W, L, D);
		return cParticle || cWall;
	}

	public double getSuperposition(Particle p) {
		return this.radio + p.radio - Math.abs(p.radio - this.radio);
	}

	private double calculateNormalForce(Particle p, double kn) {
		return -kn * getSuperposition(p);
	}

	private double calculateTanForce(Particle p, double kt) {
		return -kt * getSuperposition(p) * getRelVelocity(p);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Particle))
			return false;

		Particle p = (Particle) obj;
		return this.id == p.id;
	}

}
