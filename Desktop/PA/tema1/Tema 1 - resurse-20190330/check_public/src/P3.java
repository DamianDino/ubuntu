import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class P3 {

  public static final String INPUT_FILE = "p3.in";
  public static final String OUTPUT_FILE = "p3.out";

  static class Task {
    int N;
    int[] arr;

    /**
     * Read from input file.
     */
    private void readInput() {
      try {
        Scanner scanner = new Scanner(new File(INPUT_FILE));
        /* size of the array */
        N = scanner.nextInt();
        arr = new int[N];
        /* populate array */
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
     *         the scores of the 2 players.
     */
    private void writeOutput(long result) {
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
    private long getResult() {

      /* dp[i][j] = maximum amount you can get if you move first */
      long[][] dp = new long[N][N];

      /* base case: if we have only one number (N=1), we will pick that number.
      the main diagonal is filled with the value of each number in the array arr.
      */
      for (int i = 0; i < N; i++) {
        dp[i][i] = arr[i];
      }

      /* 2 numbers case: if we have two numbers, we will pick the bigger one.
      the second diagonal is filled with the max between its neighbours.
      */
      for (int i = 0; i < N - 1; i++) {
        dp[i][i + 1] = Math.max(arr[i], arr[i + 1]);
      }

      /* the general case, compute dp[j][j+i] - see README for more info*/
      for (int i = 2; i < N; i++) {
        for (int j = 0; j < N - i; j++) {
          /* first number is chosen */
          long firstNumber = arr[j] + Math.min(dp[j + 1][j + i - 1], dp[j + 2][j + i]);
          /* last number is chosen */
          long lastNumber = arr[j + i] + Math.min(dp[j][j + i - 2], dp[j + 1][j + i - 1]);

          if (firstNumber > lastNumber) {
            dp[j][j + i] = firstNumber;
          } else {
            dp[j][j + i] = lastNumber;
          }
        }
      }

      /* compute sum of all numbers in the array */
      long sum = 0;
      for (int s = 0; s < N; s++) {
        sum += arr[s];
      }

      long a = sum - dp[0][N - 1];

      /* return the difference between the 2 scores. */
      return dp[0][N - 1] - a;

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
