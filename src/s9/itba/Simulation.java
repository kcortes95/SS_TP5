package s9.itba;

import java.util.HashSet;
import java.util.Set;


public class Simulation {
	
	private final double GRAVITY = 9.8;
	public static final double mass = 0.01;

	double friccion = 0;
	Storage s = null;
	private Set<Particle> particles;
	private Grid grid;

	public Simulation(double friccion, Storage s) {
		this.s = s;
		s.friccion = friccion;
		this.particles = s.getParticles();
		double L = s.getL()+2*(int)Math.floor(s.getL()/(s.getD()/5));
		this.grid = new LinearGrid(L, (int)Math.floor(s.getL()/(s.getD()/5)), s.getParticles());
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
				/*for(Particle p: particles)
					System.out.println("vx: " + p.vx + " - fx: " + p.f.x);*/
			}
			if(printTime<=time){
				/*double K = totalKineticEnergy(particles);
				double U = totalPotentialEnergy(particles);
				Output.getInstace().writeEnergies(K+U,K,U, printTime);*/
				Output.getInstace().write(particles,time);
				printTime += dt2;
			}
			beeman(particles,dt);
			/*for(Particle p: particles)
				updatePos(p, dt);*/
			time += dt;
		}
	}
	
	private void beeman(Set<Particle> particles, double dt){
		for(Particle p: particles){
			//if(p.vx!=0)
				//System.out.println("EN beeman entro, rx=" + p.rx + " - ry=" + p.ry + " | p.vx=" + p.vx + " - p.vy=" + p.vy);
			p.next = new Particle(p.ID, 0, 0, 0, 0, p.r, p.m);
			p.next.rx = p.rx + p.vx*dt + (2.0/3.0)*p.f.x*dt*dt/p.m - (1.0/6.0)*p.previous.f.x*dt*dt/p.m;
			p.next.ry = p.ry + p.vy*dt + (2.0/3.0)*p.f.y*dt*dt/p.m - (1.0/6.0)*p.previous.f.y*dt*dt/p.m;
			if(Double.isNaN(p.next.rx) || Double.isNaN(p.next.ry)){
				System.out.println("0 - EN P.NEXT.RX");
				System.out.println("p.rx = " + p.rx + " - p.ry: " + p.ry);
				System.out.println("p.vx = " + p.vx + " - p.vy: " + p.vy);
				System.out.println("p.fx = " + p.f.x + " - p.fy: " + p.f.y);
				System.out.println("p.prev.f.x = " + p.previous.f.x + " - p.previous.f.y: " + p.previous.f.x);
				try{Thread.sleep(5000);}catch(Exception e){};
			}
		}
		//System.out.println("f era: " + p.f.x + "," + p.f.y);
		//predict next vel
		Set<Particle> predicted = new HashSet<>();
		for(Particle p: particles){
			p.pred = new Particle(p.ID, p.rx, p.ry, 0, 0, p.r, p.m);
			p.pred.vx = p.vx + (3.0/2.0)*(p.f.x/p.m)*dt-0.5*(p.previous.f.x/p.m)*dt;
			p.pred.vy = p.vy + (3.0/2.0)*(p.f.y/p.m)*dt-0.5*(p.previous.f.y/p.m)*dt;
			if(Double.isNaN(p.pred.vx) || Double.isNaN(p.pred.vy))
				System.out.println("1 - EN P.pred.RX");
			predicted.add(p.pred);
		}
			
		//System.out.println("Predicted v: " + predicted.vx + "," + predicted.vy);
		
		//calculate next accel using position and predicted vel
		getF(predicted);
		
		for(Particle p: particles){
			if(Double.isNaN(p.pred.f.x) || Double.isNaN(p.pred.f.y)){
				System.out.println("f era: " + p.f.x + "," + p.f.y);
				System.out.println("rx,ry: " + p.pred.rx + "," + p.pred.ry);
				System.out.println("Predicted v: " + p.pred.vx + "," + p.pred.vy);
			}
			p.next.f = p.pred.f;
			
			p.next.vx = p.vx + (5.0/12.0)*p.next.f.x*dt/p.m + (2.0/3.0)*p.f.x*dt/p.m - (1.0/12.0)*p.previous.f.x*dt/p.m; 
			p.next.vy = p.vy + (5.0/12.0)*p.next.f.y*dt/p.m + (2.0/3.0)*p.f.y*dt/p.m - (1.0/12.0)*p.previous.f.y*dt/p.m;
			if(Double.isNaN(p.next.vx) || Double.isNaN(p.next.vy)){
				System.out.println("EN NEXT VY ( CORRECCION )");
				System.out.println("f era: " + p.f.x + "," + p.f.y);
				System.out.println("rx,ry: " + p.pred.rx + "," + p.pred.ry);
				System.out.println("Predicted v: " + p.pred.vx + "," + p.pred.vy);
				try{Thread.sleep(5000);}catch(Exception e){};
			}
			
			p.previous.rx = p.rx;
			p.previous.ry = p.ry;
			p.previous.vx = p.vx;
			p.previous.vy = p.vy;
			p.previous.f = p.f;
			
			if(Double.isNaN(p.next.rx) || Double.isNaN(p.next.ry))
				System.out.println("P.NEXT.R");
			p.rx = p.next.rx;
			p.ry = p.next.ry;
			p.vx = p.next.vx;
			p.vy = p.next.vy;
			p.f = p.next.f;
		}
		/*System.out.println("EN beeman salgo, vx=" + p.vx + " - vy=" + p.vy);
		try{Thread.sleep(1000);}catch(Exception e){};*/
	}
	
	/*private void beeman(Particle p, double dt){
		//if(p.f.x!=0)
			//System.out.println("EN beeman entro, fx=" + p.f.x + " - fy=" + p.f.y);
		p.next = new Particle(p.ID, 0, 0, 0, 0, p.r, p.m);
		p.next.rx = p.rx + p.vx*dt + (2.0/3.0)*p.f.x*dt*dt/p.m - (1.0/6.0)*p.previous.f.x*dt*dt/p.m;
		p.next.ry = p.ry + p.vy*dt + (2.0/3.0)*p.f.y*dt*dt/p.m - (1.0/6.0)*p.previous.f.y*dt*dt/p.m;
		
		//System.out.println("f era: " + p.f.x + "," + p.f.y);
		//predict next vel
		Particle predicted = new Particle(p.ID, p.rx, p.ry, 0, 0, p.r, p.m);
		predicted.vx = p.vx + (3.0/2.0)*(p.f.x/p.m)*dt-0.5*(p.previous.f.x/p.m)*dt;
		predicted.vy = p.vy + (3.0/2.0)*(p.f.y/p.m)*dt-0.5*(p.previous.f.y/p.m)*dt;
		
		//System.out.println("Predicted v: " + predicted.vx + "," + predicted.vy);
		
		//calculate next accel using position and predicted vel
		getF(predicted);
		p.next.f = predicted.f;
		
		//if(p.next.f.x!=0)
			//System.out.println("getF de predicted (next) = " + p.next.f.x + "," + p.next.f.y);
		
		p.next.vx = p.vx + (5.0/12.0)*p.next.f.x*dt/p.m + (2.0/3.0)*p.f.x*dt/p.m - (1.0/12.0)*p.previous.f.x*dt/p.m; 
		p.next.vy = p.vy + (5.0/12.0)*p.next.f.y*dt/p.m + (2.0/3.0)*p.f.y*dt/p.m - (1.0/12.0)*p.previous.f.y*dt/p.m;
		
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
		//System.out.println("EN beeman salgo, vx=" + p.vx + " - vy=" + p.vy);
		//try{Thread.sleep(1000);}catch(Exception e){};
	}*/
	
	/*private void getF(Particle p){
		p.f = new Vector(0,-p.m * GRAVITY);
		//for(Particle p2: particles){
		//	if(!p.equals(p2)){
		//		p.collision(p2);
		//	}
		//}
		// It has left the silo
		if(grid.getCell(p)!=null){
			// Check own cell
			for (Particle p2: grid.getCell(p).getParticles()){
				if (!p.equals(p2)){
					p.collision(p2);
				}
			}
			// Check neighbouring cells
			for(Cell cell: grid.getCell(p).getNeighbours()){
				for(Particle p2: cell.getParticles()){
					p.collision(p2);
				}
			}
		}
		// Check wall Collision
		p.collisionWall(s.getW(), s.getL(), s.getD());
		if(p.f.y>0)
			p.f.y*=0.9;
	}*/
	
	private void getF(Set<Particle> particles){
		clearMarks(particles);
		for(Particle p: particles)
			p.f = new Vector(0,-p.m * GRAVITY);
		for(Particle p: particles){
			p.checked = true;
			for(Particle p2: particles){
				if(!p.equals(p2) && !p2.checked){
					p.collision(p2);
				}
			}
		}
		// It has left the silo
		/*if(grid.getCell(p)!=null){
			// Check own cell
			for (Particle p2: grid.getCell(p).getParticles()){
				if (!p.equals(p2)){
					p.collision(p2);
				}
			}
			// Check neighbouring cells
			for(Cell cell: grid.getCell(p).getNeighbours()){
				for(Particle p2: cell.getParticles()){
					p.collision(p2);
				}
			}
		}*/
		// Check wall Collision
		for(Particle p: particles)
			p.collisionWall(s.getW(), s.getL(), s.getD());
		/*if(p.f.y>0)
			p.f.y*=0.9;*/
	}
	
	/*private void updatePos(Particle p, double dt){
		double M = grid.getM();
		double cellLength = grid.getL()/grid.getM();
		int cellX = (int) Math.floor((p.rx+M)/cellLength);
		int cellY = (int) Math.floor((p.ry+M)/cellLength);
		beeman(p, dt);
		// If its in the silo update the cells
		if(cellX>0 && cellX<=M && cellY>0 && cellY<=M){
			int newCellX = (int)Math.floor((p.rx+M)/cellLength);
			int newCellY = (int)Math.floor((p.ry+M)/cellLength);
			if(newCellX != cellX ||newCellY != cellY){
				//left silo
				if(newCellX< 0 || newCellX >= M || newCellY < 0 || newCellY >= M){
					grid.getCell(cellX, cellY).getParticles().remove(p);
					return;
				}
				grid.getCell(cellX, cellY).getParticles().remove(p);
				grid.insert(p);
			}
		}
	}*/
	
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
		for(Particle p: particles)
			p.checked = false;
	}
}
