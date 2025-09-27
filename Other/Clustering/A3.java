// Nathan Hawrylak, 7268774

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class A3 {

	ArrayList<Node> vector;
	ArrayList<Data> data;
	int length, time;
	double[][] Umap;
	Node[][] map;
	
	
	
	A3(int length, int time){
		
		
		vector = new ArrayList<>();
		data = new ArrayList<>();
		Umap = new double[(int) Math.sqrt(length)][(int) Math.sqrt(length)];
		map = new Node[(int) Math.sqrt(length)][(int) Math.sqrt(length)];
		this.time = time;
		this.length = length;
		
		init();
		train();
		map();
		printmap();
	}
	
	void map(){
		
		for (int y = 0; y < map.length; y++){
			for (int x = 0; x < map.length; x++){
				
				map[x][y] = vector.get((int) (y*Math.sqrt(length))+x);
			}
		}
		
		for (int y = 0; y < map.length; y++){
			for (int x = 0; x < map.length; x++){
				
				Umap[x][y] = meanDis(x,y)*heat(x,y);
			}
		}
	}
	
	void printmap(){
		
		for (int y = 0; y < map.length; y++){
			for (int x = 0; x < map.length; x++){
				
				System.out.print(Umap[x][y] + " ");
			} System.out.println();
		}
	}
	
	double heat(int x, int y){
		
		double[] type = new double[data.size()];
		
		for (int z = 0; z < data.size(); z++){
			
			type[z] = data.get(z).type*radialFun(map[x][y].V,data.get(z).V,1);
		}
		
		return average(type);
	}
	
	double average(double[] d){
		
		double ret = 0;
		
		for (int z = 0; z < d.length; z++){
			
			ret += d[z];
		}
		
		return ret;
	}
	
	double meanDis(int x, int y){
		
		double ret = 0;	
		
		ret += euclidianDist(map[x][y].V,map[wrap(x+1)][wrap(y)].V);
		ret += euclidianDist(map[x][y].V,map[wrap(x+1)][wrap(y+1)].V);
		ret += euclidianDist(map[x][y].V,map[wrap(x+1)][wrap(y-1)].V);
		ret += euclidianDist(map[x][y].V,map[wrap(x-1)][wrap(y)].V);
		ret += euclidianDist(map[x][y].V,map[wrap(x-1)][wrap(y+1)].V);
		ret += euclidianDist(map[x][y].V,map[wrap(x-1)][wrap(y-1)].V);
		ret += euclidianDist(map[x][y].V,map[wrap(x)][wrap(y+1)].V);
		ret += euclidianDist(map[x][y].V,map[wrap(x)][wrap(y-1)].V);
		
		return ret/8;
	}
	
	int wrap(int i){
		
		if (i == Math.sqrt(length)){
			return 0;
		} else if (i == -1){
			return (int) Math.sqrt(length)-1;
		} else {
			
			return i;
		}
	}
	
	void train(){
		
		for (int z = time; z > 0; z--){
			
			Random rand = new Random();
			int r = rand.nextInt(data.size());
			
			int closest = search(data.get(r).V);
			
			update(closest, r, z);
		}
	}
	
	void update(int c, int r, int step){
		
		double lr = step/time;
		
		for (int z = 0; z < vector.size(); z++){
			
			double[] v = vector.get(z).V;
			
			double[] upd = subtract(data.get(r).V,vector.get(c).V);
			upd = multiply(upd, radialFun(v,vector.get(c).V,step));
			upd = multiply(upd, lr);
			
			vector.get(z).V = add(v,upd);
		}
	}
	
	double[] multiply(double[] V, double value){
		
		double[] v = new double[V.length];
		
		for (int z = 0; z < v.length; z++){
			
			v[z] = V[z]*value;
		}
		
		return v;
	}
	
	double[] add(double[] V1, double[] V2){
		
		double[] v = new double[V1.length];
		
		for (int z = 0; z < v.length; z++){
			
			v[z] = V1[z]+V2[z];
		}
		
		return v;
	}
	
	double[] subtract(double[] V1, double[] V2){
		
		double[] v = new double[V1.length];
		
		for (int z = 0; z < v.length; z++){
			
			v[z] = V1[z]-V2[z];
		}
		
		return v;
	}
	
	int search(double[] input){
		
		int closest = 0;
		double val = euclidianDist(vector.get(0).V, input);
		
		for (int z = 0; z < vector.size(); z++){
			
			double d = euclidianDist(vector.get(z).V, input);
			
			if (d < val){
				
				closest = z;
				val = d;
			}
		}
		
		return closest;
	}
	
	double radialFun(double[] V1, double[] V2, int step){
		
		double r = euclidianDist(V1,V2);
		double t = 1/step;
		
		return Math.exp(-1*(Math.pow((r*t),2)));
	}
	
	double euclidianDist(double[] V1, double[] V2){
		
		double d = 0;
		
		for (int z = 0; z < V1.length; z++){
			
			d += Math.pow((V2[z] - V1[z]), 2);
		}
		
		return Math.sqrt(d);
	}
	
	void init(){
		
		readFile("");
		
		for (int z = 0; z < length*length; z++){
			vector.add(new Node(n, (int) z%length, (int) z/length));
		}
	}
	
	void readFile(String filename){

        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);

            int z = (int) myReader.nextDouble();
            n  = (int) myReader.nextDouble();
            myReader.nextLine();

            for (int y = 0; y < z; y++){

                String[] v = myReader.nextLine().split(" ");
                data.add(new Data(shorten(v), convert(Integer.valueOf(v[0]))));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
	
	int convert( int i){
		if (i== 0){
			return -1;
		} else {
			return i;
		}
	}
	
	double[] shorten(String[] V){
		
		//double[] v = Arrays.stream(V).mapToDouble(Double::parseDouble).toArray();
		double[] ret = new double[V.length-1];
		
		for (int z = 0; z < ret.length; z++){
			ret[z] = Double.valueOf(V[z+1]);
		}
		
		return ret;
	}
	
	double sigmoid(double d){
		return 1/(1+Math.exp(-d));
	}
	
	public static void main(String[] args){
		
		int length = 7;
		int time = 10000;
		
		A3 a = new A3((int) Math.pow(length,2),time);
	}
}
