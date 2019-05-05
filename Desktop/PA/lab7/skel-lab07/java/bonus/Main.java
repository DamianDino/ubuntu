import java.io.*;
import java.util.*;
public class Main {

  static class Task {

    public static final int NMAX = 100005; // 10^5

    int n;

    @SuppressWarnings("unchecked")
    ArrayList<Integer> adj[] = new ArrayList[NMAX];

    private void readInput() {
      try {
        Scanner sc = new Scanner(new BufferedReader(new FileReader(
            INPUT_FILE)));
        n = sc.nextInt();

        for (int i = 1; i <= n; i++)
          adj[i] = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
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

//    private void dfs (int node, Queue<Integer> q, int[] visited){
//      visited[node] = 1;
//      for(int i : adj[node]){
//        if(visited[i] == 0){
//          dfs(i, q, visited);
//        }
//      }
//      q.add(node);
//    }

    private ArrayList<Integer> getResult() {

    }

    public void solve() {
      readInput();
      System.out.println(getResult());
    }
  }

  public static void main(String[] args) {
    new Task().solve();
  }
}
