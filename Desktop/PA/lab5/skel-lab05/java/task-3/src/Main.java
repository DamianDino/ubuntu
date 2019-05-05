import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static class Task {
        public final static String INPUT_FILE = "in";
        public final static String OUTPUT_FILE = "out";

        int n;

        private void readInput() {
            try {
                Scanner sc = new Scanner(new File(INPUT_FILE));
                n = sc.nextInt();
                sc.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void writeOutput(ArrayList<Integer> result) {
            try {
                PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
                for (int i = 1; i <= n; i++) {
                    pw.printf("%d%c", result.get(i), i == n ? '\n' : ' ');
                }
                pw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private boolean bkt(int step, int stop, int first, int last,
                         ArrayList<Integer> sol) {

            if (step == stop) {
                return true;
            }

            int currentStep = step + 1;

            for (int col = first; col <= last; col++) {
                boolean isValid = true;

                for (int i = 1; i < currentStep; i++) {
                    int colElem = sol.get(i);
                    int offset = currentStep - i;

                    if ((col == colElem) ||
                            (col == colElem - offset) ||
                            (col == colElem + offset)) {
                        isValid = false;
                    }
                }

                if(isValid) {
                    sol.set(currentStep, col);
                    boolean verify = bkt(currentStep, stop, first, last, sol);
                    if(verify) {
                        return true;
                    }
                }
            }

            return false;
        }

        private ArrayList<Integer> getResult() {
            ArrayList<Integer> sol = new ArrayList<Integer>();
            for (int i = 0; i <= n; i++)
                sol.add(0);


            // TODO: Gasiti o solutie pentru problema damelor pe o tabla de
            // dimensiune n x n.

            // Pentru a plasa o dama pe randul i, coloana j:
            //     sol[i] = j.
            // Randurile si coloanele se indexeaza de la 1 la n.

            // De exemplu, configuratia (n = 5)
            // X----
            // --X--
            // ----X
            // -X---
            // ---X-
            // se va reprezenta prin sol[1..5] = {1, 3, 5, 2, 4}.
            bkt(0, n, 1, n, sol);

            return sol;
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
