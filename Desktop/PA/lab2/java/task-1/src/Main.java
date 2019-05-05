import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
	static class Obj {
		public int weight;
		public int price;

		public Obj() {
			weight = 0;
			price = 0;
		}
	};

	static class Task {
		public final static String INPUT_FILE = "in";
		public final static String OUTPUT_FILE = "out";

		int n, w;
		Obj[] objs;

		private void readInput() {
			try {
				Scanner sc = new Scanner(new File(INPUT_FILE));
				n = sc.nextInt();
				w = sc.nextInt();
				objs = new Obj[n];
				for (int i = 0; i < n; i++) {
					objs[i] = new Obj();
					objs[i].weight = sc.nextInt();
					objs[i].price = sc.nextInt();
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(double result) {
			try {
				PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
				pw.printf("%.4f\n", result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private double getResult() {
			// TODO: Aflati profitul maxim care se poate obtine cu
			// obiectele date.
			double result = 0.0;
			int currentWeight = 0;
			
			List<Main.Obj> array = new ArrayList<Main.Obj>();
			
			for(Obj o:objs) {
				array.add(o);
			}
			
			Collections.sort(array,new Comparator<Obj>() {
				public int compare(Obj o1, Obj o2) {
	                double diff = (double)(1.0*o2.price/o2.weight) - (double)(1.0*o1.price/o1.weight);
	                if(diff < 0.0) {
	                	return -1;
	                }
	                if(diff == 0.0) {
	                	return 0;
	                }
	                
	                return 1;
	            }
			});
			
			int i = 0;
			for(Obj o:array) {
				//System.out.println("w: " + o.weight + " p: " + o.price);
				objs[i] = array.get(i);
				i++;
			}
			
			i = 0;
			
			while((currentWeight < w) && (i<n)) {
				if(objs[i].weight < (w - currentWeight)) {
					result+= objs[i].price;
					currentWeight+= objs[i].weight;
					
				}else {
					result+= (double)(1.0*(w-currentWeight)/objs[i].weight*objs[i].price);
					currentWeight = w;
					
				}
				i++;
			}
			
			
			return result;
		}

		public void solve() {
			readInput();
			writeOutput(getResult());
		}
	}

	public static void main(String[] args) {
		new Task().solve();
		
		/*Task t = new Task();
		t.n = 3;
		t.w = 3;
		
		Obj o1 = new Obj();
		o1.weight = 1;
		o1.price = 2;
		
		Obj o2 = new Obj();
		o2.weight = 2;
		o2.price = 2;
		
		Obj o3 = new Obj();
		o3.weight = 3;
		o3.price = 4;
		
		t.objs = new Obj[t.n];
		t.objs[0] = o1;
		t.objs[1] = o2;
		t.objs[2] = o3;
		
		System.out.println(t.getResult());*/
		
		
	}
}
