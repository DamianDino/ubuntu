import java.io.*;
import java.util.*;

public class Main {
	static class Task {
		public static final String INPUT_FILE = "in";
		public static final String OUTPUT_FILE = "out";
		public static final int NMAX = 100005; // 10^5

		int n;
		int m;

		@SuppressWarnings("unchecked")
		ArrayList<Integer> adj[] = new ArrayList[NMAX];

		int source;

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();
				source = sc.nextInt();

				for (int i = 1; i <= n; i++)
					adj[i] = new ArrayList<>();
				for (int i = 1; i <= m; i++) {
					int x, y;
					x = sc.nextInt();
					y = sc.nextInt();
					adj[x].add(y);
					adj[y].add(x);
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int[] result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				for (int i = 1; i <= n; i++) {
				  if(i != source && result[i] == 0){
				    result[i] = -1;
          }
					pw.printf("%d%c", result[i], i == n ? '\n' : ' ');
				}
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int[] getResult() {
			int d[] = new int[n + 1];
			for (int i = 0; i <= n; i++) d[i] = 0;
			// TODO: Faceti un BFS care sa construiasca in d valorile d[i] = numarul
			// minim de muchii de parcurs de la nodul source la nodul i.
			// d[source] = 0
			// d[x] = -1 daca nu exista drum de la source la x.
			// *******
			// ATENTIE: nodurile sunt indexate de la 1 la n.
			// *******

      int currentNode;
      Queue<Integer> q = new LinkedList<Integer>();
      q.add(source);
      int[] visited = new int[n+1];
      d[source] = 0;
      visited[source] = 1;

      while (!q.isEmpty()){
        currentNode = q.element();
        q.remove();

        for(Integer i : adj[currentNode]){
          if(visited[i] == 0){
            visited[i] = 1;
            q.add(i);
            d[i] = d[currentNode] + 1;
          }
        }
      }

			return d;
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
