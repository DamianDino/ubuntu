import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
	static class Task {
		public final static String INPUT_FILE = "in";
		public final static String OUTPUT_FILE = "out";

		int n, m;
		int[] dist;

		private void readInput() {
			try {
				Scanner sc = new Scanner(new File(INPUT_FILE));
				n = sc.nextInt();
				m = sc.nextInt();
				dist = new int[n];
				for (int i = 0; i < n; i++) {
					dist[i] = sc.nextInt();
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int result) {
			try {
				PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
				pw.printf("%d\n", result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int getResult() {
			// TODO: Aflati numarul minim de opriri necesare pentru
			// a ajunge la destinatie.
			int nrStops = 0; //nr de opriri
			int coord = 0;

			while(coord != dist[dist.length-1]) {  // parcurgem de la B la A
				int i = dist.length-1; //se scade din dimesiunea vectroului de distante
				while((coord+m)<dist[i]) {  // se verifica daca poate ajunge pana la benzinarie
					i--;
				}
				coord = dist[i];
				nrStops++;
			}
			return nrStops-1; //se adauga destinatia ca oprire
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
