import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class Main {
    static class Task {
        public final static String INPUT_FILE = "in";
        public final static String OUTPUT_FILE = "out";

        double n;

        private void readInput() {
            try {
                Scanner sc = new Scanner(new File(INPUT_FILE));
                n = sc.nextDouble();
                sc.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void writeOutput(double x) {
            try {
                PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
                pw.printf("%.4f\n", x);
                pw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private double computeSqrt() {
            // Daca n-ul este negativ, nu se poate calcula radicalul
            if (n < 0) {
                return 0.0;
            }

            double left, right, mid = 0;

            // Daca n-ul este subunitar, atunci radicalul va fi mai mare ca n
            if (n < 1) {
                left = n;
                right = 1;
            } else {
                left = 0;
                right = n;
            }

            while (abs(right - left) >= pow(10, -4)) {
                mid = (left + right) / 2;
                if (mid * mid == n) {
                    return mid;
                } else {
                    if (mid * mid < n) {
                        left = mid;
                    } else {
                        right = mid;
                    }
                }
            }

            // Precizie de 10^(-x) inseamna |valoarea_ta - valoarea_reala| < 10^(-x).
            return mid;
        }

        public void solve() {
            readInput();
            writeOutput(computeSqrt());
        }
    }

    public static void main(String[] args) {
        new Task().solve();
    }
}
