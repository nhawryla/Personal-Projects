// Nathan Hawrylak, 7268774

import java.util.Random;

public class Node {

	double[] V;
	double x,y;
	
	Node(int n, double x, double y){
		
		V = new double[n];
		this.x = x;
		this.y = y;
		
		for (int z = 0;z < V.length; z++){
			
			Random r = new Random();
			V[z] = 0 + (1 - 0) * r.nextDouble();
		}
	}
}
