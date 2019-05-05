import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;

public class P1 {

  public static final String INPUT_FILE = "p1.in";
  public static final String OUTPUT_FILE = "p1.out";

  static class Task {
    int N;
    Integer[] list;

    /**
     * Read from input file.
     */
    private void readInput() {
      try {
        MyScanner scanner = new MyScanner(new FileInputStream(INPUT_FILE));

        /* size of the list */
        N = scanner.nextInt();

        /* array of numbers */
        list = new Integer[N];

        /* populate the array */
        for (int i = 0; i < N; i++) {
          list[i] = scanner.nextInt();
        }
        scanner.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    /**
     * Write the output.
     *
     * @param: the maximum difference between
     *         the scores of the 2 players.
     */
    private void writeOutput(int result) {
      try {
        PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
        pw.println(result);
        pw.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    /**
    * Solve the problem.
    *
    * @return: maximum difference between the scores
    *          of the 2 players.
    */
    private int getResult() {
      /* initial scores */
      int tuzgu = 0;
      int ritza = 0;

      /* sort in descending order */
      Arrays.sort(list, Collections.reverseOrder());
      for (int i = 0; i < list.length; i++) {
        /* Tuzgu's turn */
        if (i % 2 == 0) {
          tuzgu += list[i];
        } else { /* Ritza's turn */
          ritza += list[i];
        }
      }
      return tuzgu - ritza;
    }

    public void solve() {
      readInput();
      writeOutput(getResult());
    }
  }

  public static void main(String[] args) {
    Task task = new Task();
    task.solve();
  }
}
