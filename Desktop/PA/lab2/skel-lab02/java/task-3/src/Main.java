import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
	static class Homework {
		public int deadline;
		public int score;

		public Homework() {
			deadline = 0;
			score = 0;
		}
	}

	static class Task {
		public final static String INPUT_FILE = "in";
		public final static String OUTPUT_FILE = "out";

		int n;
		Homework[] hws;

		private void readInput() {
			try {
				Scanner sc = new Scanner(new File(INPUT_FILE));
				n = sc.nextInt();
				hws = new Homework[n];
				for (int i = 0; i < n; i++) {
					hws[i] = new Homework();
					hws[i].deadline = sc.nextInt();
					hws[i].score = sc.nextInt();
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
			// TODO: Aflati punctajul maxim pe care il puteti obtine
			// planificand optim temele.

			Arrays.sort(hws, new Comparator<Homework>(){
				public int compare (Homework h1, Homework h2){
					if (h1.deadline > h2.deadline){
						return 1;
					}
					if (h1.deadline == h2.deadline && h1.score > h2.score){
						return 1;
					}
					return -1;
				}
			});

			int week = hws[hws.length-1].deadline;
			int score = 0;
			int nrHw = hws.length-1;

			while (nrHw >=0){
				if (week==0)
					break;
				score += hws[nrHw].score;
				nrHw--;
				while((nrHw>=0)&&(week < hws[nrHw].deadline)) {
					nrHw--;
				}
				week--;
			}
			return score;
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
