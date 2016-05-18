package s9.itba;

import java.util.HashSet;
import java.util.Set;


public class Simulation {
	
	private final double GRAVITY = 9.8;
	public static final double mass = 0.01;

	double friccion = 0;
	Storage s = null;
	private Set<Particle> particles;
	private Set<Particle> outOfBox;
	Set<Particle> toBeRemoved = new HashSet<Particle>();
	private Grid grid;

	public Simulation(double friccion, Storage s) {
		this.s = s;
		s.friccion = friccion;
		this.particles = s.getParticles();
		this.outOfBox = new HashSet<Particle>();
		double L = s.getL()+2*0.1;
		this.grid = new LinearGrid(L, (int)Math.floor(L/(s.getD()/5))/2, s.getParticles());
	}

	public void run(double totalTime, double dt, double dt2) {
		int percentage=-1;
		double time = 0, printTime = 0;
		// Set forces and calculate previous
		Set<Particle> previous = new HashSet<Particle>();
		getF(particles);
		for(Particle p: particles){
			Vector prevPos = eulerPos(p,-dt);
			Vector prevVel = eulerVel(p,-dt);
			p.previous = new Particle(p.ID,prevPos.x,prevPos.y,prevVel.x,prevVel.y,p.r,p.m);
			previous.add(p.previous);
		}
		getF(previous);
		while(time<=totalTime){
			if((int)(100*time/totalTime)!=percentage){
				percentage = (int)(100*time/totalTime);
				System.out.println(percentage + "%");
				System.out.println("N° particles = " + particles.size());
			}
			if(printTime<=time){
				Output.getInstace().write(particles,time);
				Output.getInstace().writeEnergy(particles, printTime);
				Output.getInstace().writeAmount(particles, printTime);
				printTime += dt2;
			}
			beeman(particles,dt);
			for(Particle p: particles){
				if(p.ry<0){
					p.outOfBox = true;
					outOfBox.add(p);
				}
			}
			for(Particle p: outOfBox){
				particles.remove(p);
				if(p.ry<-s.getL()/4)
					toBeRemoved.add(p);
			}
			for(Particle p: toBeRemoved){
				outOfBox.remove(p);
			}
			toBeRemoved.clear();
			time += dt;
		}
	}
	
	private void beeman(Set<Particle> particles, double dt){
		for(Particle p: particles){
			p.next = new Particle(p.ID, 0, 0, 0, 0, p.r, p.m);
			p.next.rx = p.rx + p.vx*dt + (2.0/3.0)*p.f.x*dt*dt/p.m - (1.0/6.0)*p.previous.f.x*dt*dt/p.m;
			p.next.ry = p.ry + p.vy*dt + (2.0/3.0)*p.f.y*dt*dt/p.m - (1.0/6.0)*p.previous.f.y*dt*dt/p.m;
		}
		//predict next vel
		Set<Particle> predicted = new HashSet<>();
		for(Particle p: particles){
			p.pred = new Particle(p.ID, p.rx, p.ry, 0, 0, p.r, p.m);
			p.pred.vx = p.vx + (3.0/2.0)*(p.f.x/p.m)*dt-0.5*(p.previous.f.x/p.m)*dt;
			p.pred.vy = p.vy + (3.0/2.0)*(p.f.y/p.m)*dt-0.5*(p.previous.f.y/p.m)*dt;
			predicted.add(p.pred);
		}
			
		
		//calculate next accel using position and predicted vel
		getF(predicted);
		
		for(Particle p: particles){
			if(p.pred != null && p.pred.f != null){
				p.next.f = p.pred.f;
				
				p.next.vx = p.vx + (5.0/12.0)*p.next.f.x*dt/p.m + (2.0/3.0)*p.f.x*dt/p.m - (1.0/12.0)*p.previous.f.x*dt/p.m; 
				p.next.vy = p.vy + (5.0/12.0)*p.next.f.y*dt/p.m + (2.0/3.0)*p.f.y*dt/p.m - (1.0/12.0)*p.previous.f.y*dt/p.m;
				
				p.previous.rx = p.rx;
				p.previous.ry = p.ry;
				p.previous.vx = p.vx;
				p.previous.vy = p.vy;
				p.previous.f = p.f;
				
				p.vx = p.next.vx;
				p.vy = p.next.vy;
				p.f = p.next.f;
			}
			p.rx = p.next.rx;
			p.ry = p.next.ry;
		}
	}
	
	private void getF(Set<Particle> particles){
		clearMarks(particles);
		for(Particle p: particles)
			p.f = new Vector(0,-p.m * GRAVITY);
		for(Particle p: particles){
			if(p.outOfBox)
				return;
			if(!p.checked){
				p.checked = true;
				for(Particle p2: particles){
					if(!p.equals(p2) && !p2.checked){
						p.collision(p2);
					}
				}
			}
		}
		// CON CELL INDEX METHOD
		/*for(Particle p: particles){
			if(p.outOfBox)
				return;
			if(!p.checked){
				// Check own cell
				for (Particle p2: grid.getCell(p).getParticles()){
					System.out.println(grid.getCell(p).getParticles().size());
					if (!p.equals(p2) && !p2.checked){
						p.collision(p2);
					}
				}
				// Check neighbouring cells
				for(Cell cell: grid.getCell(p).getNeighbours()){
					for(Particle p2: cell.getParticles()){
						if(!p.equals(p2))
							p.collision(p2);
					}
				}
				p.checked = true;
			}
		}*/
		// Check wall Collision
		for(Particle p: particles){
			if(p.ry>=-p.r)
				p.collisionWall(s.getW(), s.getL(), s.getD());
		}
	}
	
	private void updateCell(Particle p){
		Cell previous = grid.getCell(p.previous);
		Cell current = grid.getCell(p);
		if(!previous.equals(current)){
			previous.getParticles().remove(p);
			if(current != null){
				grid.insert(p);
			}
		}
	}
	
	private Vector eulerPos(Particle part, double dt){
		double x = part.rx + dt*part.vx + dt*dt*part.f.x/(2*part.m);
		double y = part.ry + dt*part.vy + dt*dt*part.f.y/(2*part.m);
		return new Vector(x,y);
	}
	
	private Vector eulerVel(Particle part, double dt){
		double velx = part.vx + dt*part.f.x/part.m;
		double vely = part.vy + dt*part.f.y/part.m;
		return new Vector(velx,vely);
	}
	
	public void clearMarks(Set<Particle> particles){
		for(Particle p: particles){
			p.checked = false;
		}
	}
}
