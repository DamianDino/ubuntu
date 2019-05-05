import java.io.*;
import java.util.*;

public class Main {
	static class Task {
		public static final String INPUT_FILE = "1.in";
		public static final String OUTPUT_FILE = "out";
		public static final int NMAX = 100005; // 10^5

		int n;
		int m;

		@SuppressWarnings("unchecked")
		ArrayList<Integer> adj[] = new ArrayList[NMAX];

		private void readInput() {
			try {
				Scanner sc = new Scanner(new BufferedReader(new FileReader(
								INPUT_FILE)));
				n = sc.nextInt();
				m = sc.nextInt();

				for (int i = 1; i <= n; i++)
					adj[i] = new ArrayList<>();
				for (int i = 1; i <= m; i++) {
					int x, y;
					x = sc.nextInt();
					y = sc.nextInt();
					adj[x].add(y);
				}
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(ArrayList<Integer> result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(
								OUTPUT_FILE)));
				for (int i = 0; i < result.size(); i++) {
					pw.printf("%d ", result.get(i));
				}
				pw.printf("\n");
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void dfs (int node, Queue<Integer> q, int[] visited){
		  visited[node] = 1;
		  for(int i : adj[node]){
        System.out.println(adj[node]);
		    if(visited[i] == 0){
		      dfs(i, q, visited);
        }
      }
		  q.add(node);
    }

		private ArrayList<Integer> getResult() {
			// TODO: Faceti sortarea topologica a grafului stocat cu liste de
			// adiacenta in adj.
			// *******
			// ATENTIE: nodurile sunt indexate de la 1 la n.
			// *******
			ArrayList<Integer> topsort = new ArrayList<Integer>();
      int[] visited = new int[n+1];
			Queue<Integer> q = new LinkedList<Integer>();
			int i;
			int idx = n-1;
			for(i=n; i>0; i--){
			  if (visited[i] == 0) {
			    dfs(i, q, visited);
        }

			  while(!q.isEmpty()){
			    topsort.add(q.element());
			    q.remove();
			    idx--;

        }
      }

			ArrayList<Integer> copy = new ArrayList<Integer>();
			for(int j = topsort.size() - 1; j >= 0 ; j--){
			  copy.add(topsort.get(j));
      }

			return copy;
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
