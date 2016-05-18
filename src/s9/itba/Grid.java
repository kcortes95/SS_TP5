package s9.itba;
import java.util.Set;


public abstract class Grid {
	
	private Cell[][] cells;
	private double L;
	private int M;
	private double offset;
	
	public Grid(double L, int M, Set<Particle> particles){
		this.L = L;
		this.M = M;
		cells = new Cell[M][M];
		for(int i=0; i<M; i++){
			for(int j=0; j<M; j++){
				cells[i][j] = new Cell();
			}
		}
		offset = M;
		insertParticles(particles);
		calculateNeighbours();
	}
	
	public abstract void calculateNeighbours();
	
	public void insertParticles(Set<Particle> particles){
		for(Particle p: particles){
			insert(p);
		}
	}
	
	public void insert(Particle p){
		int x = (int) (Math.floor((p.rx+offset)/(L/M)));
		int y = (int) (Math.floor((p.ry+offset)/(L/M)));
		/*if(x<0 || x>=M || y<0 || y>=M)
			System.out.println(p.rx + "," + p.ry + " - " + x + "," + y);*/
		cells[x][y].getParticles().add(p);
	}
	
	public void remove(Particle p){
		int x = (int) (Math.floor((p.rx+offset)/(L/M)));
		int y = (int) (Math.floor((p.ry+offset)/(L/M)));
		cells[x][y].getParticles().remove(p);
	}
	
	public Cell getCell(int x, int y){
		return cells[x][y];
	}
	
	public Cell getCell(Particle p){
		int x = (int) (Math.floor((p.rx+offset)/(L/M)));
		int y = (int) (Math.floor((p.ry+offset)/(L/M)));
		/*try{
			System.out.println(x + ", " + y);
			Thread.sleep(10);
		}catch(Exception e){
			
		}*/
		if(x<0 || x>=M || y<0 || y>=M){
			return null;
		}
		return cells[x][y];
	}
	
	public Cell[][] getGrid(){
		return cells;
	}
	
	public int getM(){
		return M;
	}
	
	public double getL() {
		return L;
	}
}
