package s9.itba;

public class Main {

	public static void main(String[] args) {
		/*double dt = 0.1;
		double max = 10;
		double step = 0;

		Storage s = new Storage(10, 20, 5);
		Simulation sim = new Simulation(dt, max, step, 0.95, s);
		sim.run();*/
		
		int avg = 1;
		
		Storage s = new Storage(100, 200, 2);
		for(double i = 0.1; i<=5; i+=0.1){
			double total = 0;
			for(int j = 0; j<avg; j++){
				s.generateRandomParticles(i);
				total += s.getParticles().size();
				s.getParticles().clear();
			}
			System.out.println(i + "\t" + total/avg);
		}
		//Output.getInstace().write(s.getParticles(), 0);
	}

}
