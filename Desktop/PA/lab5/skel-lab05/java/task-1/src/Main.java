import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static class Task {
        public final static String INPUT_FILE = "in";
        public final static String OUTPUT_FILE = "out";

        int n, k;

        private void readInput() {
            try {
                Scanner sc = new Scanner(new File(INPUT_FILE));
                n = sc.nextInt();
                k = sc.nextInt();
                sc.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void writeOutput(ArrayList<ArrayList<Integer>> result) {
            try {
                PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
                pw.printf("%d\n", result.size());
                for (ArrayList<Integer> arr : result) {
                    for (int i = 0; i < arr.size(); i++) {
                        pw.printf("%d%c", arr.get(i), i + 1 == arr.size() ?
                                '\n' : ' ');
                    }
                }
                pw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void bkt(int step, int stop, int first, int last,
                         ArrayList<Integer> aranjament,
                         ArrayList<ArrayList<Integer>> all,
                         boolean[] visited) {

            if(step == stop) {
                ArrayList<Integer> solution = new ArrayList<>(aranjament);
                all.add(solution);
                return;
            }

            for(int i = first; i <= last; i++) {
                if(!visited[i]){
                    visited[i] = true;
                    aranjament.add(i);
                    bkt(step + 1, stop, first, last, aranjament, all, visited);
                    aranjament.remove(aranjament.size() - 1);
                    visited[i] = false;
                }
            }
        }

        private ArrayList<ArrayList<Integer>> getResult() {
            ArrayList<ArrayList<Integer>> all = new ArrayList<>();

            // TODO: Construiti toate aranjamentele de N luate cate K ale
            // multimii {1, ..., N}.

            // Pentru a adauga un nou aranjament:
            //   ArrayList<Integer> aranjament//   all.add(aranjament);
            boolean[] visited = new boolean[n+1];
            for(int i = 0; i < n+1; i++)
                visited[i] = false;

            ArrayList<Integer> aranjament = new ArrayList<>();
            bkt(0, k, 1, n, aranjament, all, visited);

            return all;
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
