package s9.itba;

import java.util.Set;


public class Simulation {

	private final double GRAVITY = 9.8;
	public static final double mass = 0.01;

	double dt = 0;
	double max = 0;
	double step = 0;
	double friccion = 0;
	Storage s = null;
	private Set<Particle> particles;

	public Simulation(double dt, double max, double step, double friccion, Storage s) {
		this.dt = dt;
		this.max = max;
		this.step = step;
		this.s = s;
		s.friccion = friccion;
	}

	public void run() {
		/*
		 * Esto esta modelado con la fuerza bruta... Obviamente hay que
		 * implementar el Cell Index Method para calcular cuales son los vecinos
		 */
		/*while (step <= max) {
			for (Particle p1 : s.particles) {
				for (Particle p2 : s.particles) {
					if (!p1.equals(p1)) {
						System.out.println("entro");
						p1.collision(s.getW(), s.getL(), s.getD(), p2);
					}
				}
				p1.vx = p1.vx + p1.f.x * dt / p1.m;
				p1.vy = p1.vy + p1.f.y * dt / p1.m;
			}
			step += dt;
		}*/
	}
	
	private void beeman(Particle p){
		p.next = new Particle(p.ID, 0, 0, 0, 0, p.r, p.m);
		p.next.rx = p.rx + p.vx*dt + (2.0/3.0)*p.f.x*dt*dt/p.m - (1.0/6.0)*p.previous.f.x*dt*dt/p.m;
		p.next.ry = p.ry + p.vy*dt + (2.0/3.0)*p.f.y*dt*dt/p.m - (1.0/6.0)*p.previous.f.y*dt*dt/p.m;
		p.next.f = getF(p.next);
		p.next.vx = p.vx + (1.0/3.0)*p.next.f.x*dt/p.m + (5.0/6.0)*p.f.x*dt/p.m - (1.0/6.0)*p.previous.f.x*dt/p.m; 
		p.next.vy = p.vy + (1.0/3.0)*p.next.f.y*dt/p.m + (5.0/6.0)*p.f.y*dt/p.m - (1.0/6.0)*p.previous.f.y*dt/p.m;
		
		p.previous.rx = p.rx;
		p.previous.ry = p.ry;
		p.previous.vx = p.vx;
		p.previous.vy = p.vy;
		p.previous.f = p.f;
		
		p.rx = p.next.rx;
		p.ry = p.next.ry;
		p.vx = p.next.vx;
		p.vy = p.next.vy;
		p.f = p.next.f;
	}
	
	private Vector getF(Particle p){
		Vector aux = new Vector (0,0);
		p.f.y= -p.m * GRAVITY;
		for (Particle p1: particles){
			if (!p.equals(p1)){
				p1.collision(p);
			}
		}
		return aux;
	}

}
