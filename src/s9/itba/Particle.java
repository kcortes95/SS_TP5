package s9.itba;

public class Particle {

	double rx, ry;
	double vx, vy;
	double ax, ay;
	double radio;
	double mass;

	double normalForce = 0;
	double tanForce = 0;

	public Particle(double rx, double ry, double radio, double mass) {
		this.rx = rx;
		this.ry = ry;
		this.radio = radio;
		this.mass = mass;
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

}
