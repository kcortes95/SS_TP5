package s9.itba;

import java.awt.Color;
import java.util.*;



public class Particle {

	
	
	public Particle previous, next;
    static int counter = 1;
    public Vector f;
    public double rx, ry;    
    public double vx, vy;
    public double ax, ay;
    public double r;    
    public double m;   
    private Color c;     
    public int ID;
    public boolean checked = false;
    public double kn, kt;

    public Particle(double rx, double ry, double vx, double vy, double ax, double ay, double radius, double mass, Color color) {
        this.vx = vx;
        this.vy = vy;
        this.ax = ax;
        this.ay = ay;
        this.rx = rx;
        this.ry = ry;
        this.r = radius;
        this.m  = mass;
        this.c  = color;
        this.ID = counter++;
    }
    
    public Particle(int ID, double rx, double ry, double vx, double vy, double radius, double mass){
    	this(rx,ry,vx,vy,0,0,radius,mass,Color.red);
    	this.ID = ID;
    }
    
    public Particle(double r, double m,  Color c) {
    	 rx = (Math.random() * (0.5-2*r)) + r;
         ry = (Math.random() * (0.5-2*r)) + r;
         vx = 0.1 * (Math.random()*2 - 1);
         vy = Math.sqrt(0.1*0.1-vx*vx)*(Math.random()<0.5?1:-1);
         this.r = r;
         this.m   = m;
         this.c  = c;
         this.ID = counter++;
  	}
    
    public Particle(double rx, double vx, double ax, double r, double m){
    	this(rx,0,vx,0,ax,0,r,m,Color.RED);
    }
  
    public void move(double dt) {
        rx += vx * dt;
        ry += vy * dt;
    }

    public double getDistance(Particle other){
    	return Math.sqrt(Math.pow(this.rx-other.rx,2)+Math.pow(this.ry-other.ry, 2));
    }
    
    public int hashCode(){
    	return ID;
    }
    
    public boolean equals(Object o){
    	if(o == null)
    		return false;
    	if(o.getClass() != this.getClass())
    		return false;
    	Particle other = (Particle) o;
    	if(other.ID != this.ID)
    		return false;
    	return true;
    }
    
    @Override
    public String toString() {
    	return "" + ID;
    }
    
    public Color getC() {
		return c;
	}
    
    public double getSpeed(){
    	return Math.sqrt(vx*vx+vy*vy);
    }
    
    public double distanceToOrigin(){
    	return Math.sqrt(rx*rx+ry*ry);
    }
    
    
	
	
	
	
	
	
	List<Particle> particlesColided = new ArrayList<>();

	

	

	
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

	public boolean collision(Particle p) {
		if (getSuperposition(p) >= 0) {
			particlesColided.add(p);
			double normal = calculateNormalForce(p, kn);
			double tangencial = calculateTanForce(p, kt);
			f.x += normal * Math.cos(getAngle(p)) + tangencial * Math.sin(getAngle(p));
			f.y += normal * Math.sin(getAngle(p)) + tangencial * Math.cos(getAngle(p));
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
		if (this.rx - r == 0 || this.rx + r == L) {
			f.y += f.x*Math.signum(f.x)*0.1; //////(CAMBIAR EL MU)
			f.x = 0;
			state = true;
		}

		double posY = this.ry + r;
		double bound1 = (W - D) / 2;
		double bound2 = (W + D) / 2;
		if ((posY >= 0 && posY <= bound1) || (posY >= bound2 && posY <= W)) {
			f.y = 0;
			f.x -= f.y*0.1*Math.signum(f.x);
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
		return this.r + p.r - Math.abs(p.r - this.r);
	}

	private double calculateNormalForce(Particle p, double kn) {
		return -kn * getSuperposition(p);
	}

	private double calculateTanForce(Particle p, double kt) {
		return -kt * getSuperposition(p) * getRelVelocity(p);
	}

	


}
