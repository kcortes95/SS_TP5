package s9.itba;

import java.awt.Color;
import java.util.*;



public class Particle {


	public static final double kn = Math.pow(10, 5);
	public static final double kt = 2*kn;
	public static final double mu = kn;
	
	public Particle previous, next, pred;
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
    
    public Particle(double rx, double ry, double radius, double mass){
    	this(rx,ry,0,0,0,0,radius,mass,Color.red);
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
    
	public double calculatePressure(double friction, double D) {
		return 1 - Math.exp((-4 * friction * ry) / D);
	}

	public Vector getNormalVersor(Particle p){
		Vector relDist = new Vector(p.rx-this.rx,p.ry-this.ry);
		relDist.x /= getDistance(p);
		relDist.y /= getDistance(p);
		return relDist;
	}
	
	public Vector getTanVersor(Particle p) {
		Vector relDist = new Vector(p.rx-this.rx,p.ry-this.ry);
		relDist.x /= getDistance(p);
		relDist.y /= getDistance(p);
		return new Vector(-relDist.y,relDist.x);
	}
	
	public double getTanVel(Vector tVersor){
		return Math.sqrt(Math.pow(this.vx*tVersor.x,2)+Math.pow(this.vy*tVersor.y,2))*(angBetweenVec(tVersor, new Vector(vx,vy))<Math.PI/2?1:-1);
	}
	
	public Vector getRelPos(Particle other){
		return new Vector(other.rx-this.rx,other.ry-this.ry);
	}
	
	/*
	 * Devuelve cual es el angulo que forman las dos particulas respecto a sus
	 * posiciones Esto lo hacemos para despues poder calcular cuales son las
	 * fuerzas en X y en Y (
	 */
	public double getAngle(Particle p) {
		return Math.atan2(p.ry - this.ry, p.rx - this.rx);
	}

	public void collision(Particle p) {
		/*if(Double.isNaN(getSuperposition(p))){
			System.out.println("EN GETSUPERPOSITION");
			try{Thread.sleep(10000);}catch(Exception e){};
		}*/
		if (getSuperposition(p) > 0) {
			double nForce = calculateNormalForce(p, kn);
			double tForce = calculateTanForce(p, kt);
			/*if(Double.isNaN(nForce) || Double.isNaN(tForce) || Double.isInfinite(nForce) || Double.isInfinite(tForce)){
				System.out.println("EN FORCE");
				System.out.println("Superpos: " + getSuperposition(p));
				System.out.println("Pos this: " + this.rx + "," + this.ry);
				System.out.println("Pos other: " + p.rx + "," + p.ry);
				System.out.println("Vel this: " + this.vx + "," + this.vy);
				System.out.println("Vel other: " + p.vx + "," + p.vy);
				try{Thread.sleep(10000);}catch(Exception e){};
			}*/
			Vector nVersor = getNormalVersor(p);
			Vector tVersor = getTanVersor(p);
			//if(Double.isNaN(nVersor.x) || Double.isNaN(nVersor.x) || Double.isNaN(nVersor.y) || Double.isNaN(tVersor.y)){
			/*if(nVersor.x<-1 || nVersor.x>1 || nVersor.y<-1 || nVersor.y>1 || tVersor.x<-1 || tVersor.x>1 || tVersor.y<-1 || tVersor.y>1){
				System.out.println("EN VERSORES");
				System.out.println("nVERSOR : " + nVersor.x + "," + nVersor.y);
				System.out.println("tVERSOR : " + tVersor.x + "," + tVersor.y);
				System.out.println("Superpos: " + getSuperposition(p));
				System.out.println("Pos this: " + this.rx + "," + this.ry);
				System.out.println("Pos other: " + p.rx + "," + p.ry);
				System.out.println("Vel this: " + this.vx + "," + this.vy);
				System.out.println("Vel other: " + p.vx + "," + p.vy);
				try{Thread.sleep(10000);}catch(Exception e){};
			}*/
			/*if(Math.abs(nForce)>10000 || Math.abs(tForce)>10000){
				System.out.println("\nEntre " + ID + " y " + p.ID);
				System.out.println("F antes: " + f.x + ", " + f.y);
				System.out.println("nForce: " + nForce);
				System.out.println("tForce: " + tForce);
				System.out.println("Superpos: " + getSuperposition(p));
				System.out.println("Pos this: " + this.rx + "," + this.ry);
				System.out.println("Pos other: " + p.rx + "," + p.ry);
				System.out.println("Vel this: " + this.vx + "," + this.vy);
				System.out.println("Vel other: " + p.vx + "," + p.vy);
				try{Thread.sleep(100);}catch(Exception e){};
			}*/
			f.x += nForce * nVersor.x + tForce * tVersor.x;
			f.y += nForce * nVersor.y + tForce * tVersor.y;
			p.f.x += -nForce * nVersor.x - tForce * tVersor.x;
			p.f.y += -nForce * nVersor.y - tForce * tVersor.y;
			//System.out.println("F desp: " + f.x + ", " + f.y);
		}
	}

	public void collisionWall(double W, double L, double D) {
		double limitPos=0, limitVPos;
		if (this.rx - r <= 0){
			limitPos= this.rx-r;
			//rx = r;
		}else if (this.rx + r >= W){
			limitPos = this.rx+r-W;
			//rx = W-r;
		}
		if ( limitPos != 0){
			//System.out.println("Vertical: v: " + vx + "," + vy);
			//System.out.println("F: " + f.x + "," + f.y);
			//System.out.println("pos: " + rx + "," + ry + "  - r: " + r);
			this.f.x -= limitPos * mu;
			//System.out.println("vy: " + vy + " - Limitpos: " + limitPos);
			this.f.y -= kt*this.vy*limitPos*Math.signum(limitPos);//*0.05; 
			//System.out.println("Desp f: " +f.x + "," + f.y);
			//System.out.println();

			/*if(Double.isInfinite(this.f.x) || Double.isInfinite(this.f.y)){
				System.out.println("EN WALL COLLISION - PARED");
				System.out.println("fx = " + this.f.x + " - fy = " + this.f.y);
				System.out.println("vx = " + this.vx + " - vy = " + this.vy);
				try{Thread.sleep(10000);}catch(Exception e){};
			}*/
		}
		limitVPos = r-this.ry;
		if (limitVPos>0 && (rx<=(W-D)/2 || rx>=(W+D)/2)){
			this.f.y += limitVPos * mu;
			this.f.x -= kt*this.vx*limitVPos;//*0.01;
			//System.out.println("Desp f: " +f.x + "," + f.y);
			//System.out.println();
			//try{Thread.sleep(100);}catch(Exception e){};
			//ry = r;
			/*if(Double.isInfinite(this.f.x) || Double.isInfinite(this.f.y)){
				System.out.println("EN WALL COLLISION - PISO");
				try{Thread.sleep(10000);}catch(Exception e){};
			}*/
		}
		limitVPos = this.ry+r-L;
		if(limitVPos>0){
			this.f.y -= limitVPos * mu;
			this.f.x -= kt*this.vx*limitVPos;
			/*if(Double.isInfinite(this.f.x) || Double.isInfinite(this.f.y)){
				System.out.println("EN WALL COLLISION - TECHO");
				try{Thread.sleep(10000);}catch(Exception e){};
			}*/
		}
	}

	public double getSuperposition(Particle p) {
		return this.r + p.r - Math.sqrt(Math.pow(this.rx-p.rx,2)+Math.pow(this.ry-p.ry, 2));
	}

	private double calculateNormalForce(Particle p, double kn) {
		return -kn * getSuperposition(p);
	}

	private double calculateTanForce(Particle p, double kt) {
		Vector tVersor = getTanVersor(p);
		return -kt * getSuperposition(p) * (getTanVel(tVersor)-p.getTanVel(tVersor));
	}
	
	private double angBetweenVec(Vector v1, Vector v2){
		return Math.acos((v1.x*v2.x+v1.y*v2.y)/(Math.sqrt(v1.x*v1.x+v1.y*v1.y)*Math.sqrt(v2.x*v2.x+v2.y*v2.y)));
	}
}
