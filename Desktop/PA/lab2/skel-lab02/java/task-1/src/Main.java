import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;

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
			double res = 0.0;
			int crtWeight = 0;
			
			List<Obj> objectList = new ArrayList<Obj>();

			for (Obj o : objs){
				objectList.add(o);
			}
			
			// sortam obiectele pe care le avem in functie de raportul pret/greutate			
			Collections.sort(objectList, new Comparator<Obj>(){
				public int compare (Obj o1, Obj o2){
					double raport = (double)((double)(o2.price)/(double)(o2.weight) - (double)(o1.price)/(double)(o1.weight));
					if (raport < 0.0)
						return -1;
					if (raport == 0.0)
						return 0;  // la fel
					return 1;
				}
			});

			int i=0;
			for(Obj o : objectList){
				objs[i] = objectList.get(i);
				i++;
			}

			i = 0;
			while((crtWeight < w) && (i < n)){
				if (objs[i].weight < (w - crtWeight)){
					res += objs[i].price;		//creste profitul
					crtWeight += objs[i].weight;	// creste greutatea rucsacului
				}else{
					res += (double)(1.0*(w - crtWeight)/objs[i].weight*objs[i].price);
					crtWeight = w;
				}
				i++;
			}

			return res;
		}

		public void solve() {
			readInput();
			writeOutput(getResult());
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
