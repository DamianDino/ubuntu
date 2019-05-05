import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class P2 {

  public static final String INPUT_FILE = "p2.in";
  public static final String OUTPUT_FILE = "p2.out";

  static class Task {
    int N;
    int k;
    Integer[] arr;

    /**
     * Read from input file.
     */
    private void readInput() {
      try {
        Scanner scanner = new Scanner(new File(INPUT_FILE));
        /* size of the array */
        N = scanner.nextInt();
        /* number of numbers we remove from the array */
        k = scanner.nextInt();
        arr = new Integer[N];
        /* populate the list */
        for (int i = 0; i < N; i++) {
          arr[i] = scanner.nextInt();
        }
        scanner.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    /**
     * Write the output.
     *
     * @param: the maximum difference between
     *          the scores of the 2 players.
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
      /* dynamic programming matrix
       * dp[i][j] = maximum difference between the 2 scores, by choosing
       * the i greatest numbers, and removing j out of them.
       * */
      int[][] dp = new int[N + 2][k + 2];

      /* base case: the first column is the first problem (P1)
      Store the results from the first problem in an array.
      */
      Integer[] diff = new Integer[N];
      int tuzgu = 0;
      int ritza = 0;

      Arrays.sort(arr, Collections.reverseOrder());

      for (int i = 0; i < arr.length; i++) {
        if (i % 2 == 0) {
          tuzgu += arr[i];
        } else {
          ritza += arr[i];
        }
        diff[i] = tuzgu - ritza;
      }

      /* populate the first column in the matrix */
      for (int i = 1; i <= N; i++) {
        dp[i][0] = diff[i - 1];
      }

      /* the general case - see README for more info */
      for (int i = 2; i <= N; i++) {
        for (int j = 1; j < Math.min(i, k + 1); j++) {
          if ((i - j) % 2 == 0) {
            dp[i][j] = Math.max(dp[i - 1][j - 1], dp[i - 1][j] - arr[i - 1]);
          } else {
            dp[i][j] = Math.max(dp[i - 1][j - 1], dp[i - 1][j] + arr[i - 1]);
          }
        }
      }

      return dp[N][k];
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
