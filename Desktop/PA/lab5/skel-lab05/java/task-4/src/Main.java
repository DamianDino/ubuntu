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
        char[] caractere;
        int[] freq;

        private void readInput() {
            try {
                Scanner sc = new Scanner(new File(INPUT_FILE));
                n = sc.nextInt();
                k = sc.nextInt();
                String s = sc.next().trim();
                s = " " + s;
                caractere = s.toCharArray();
                freq = new int[n + 1];
                for (int i = 1; i <= n; i++) {
                    freq[i] = sc.nextInt();
                }
                sc.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void writeOutput(ArrayList<ArrayList<Character>> result) {
            try {
                PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
                pw.printf("%d\n", result.size());
                for (ArrayList<Character> arr : result) {
                    for (int i = 0; i < arr.size(); i++) {
                        pw.printf("%c", arr.get(i));
                    }
                    pw.printf("\n");
                }
                pw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void bkt(int step,
                         int stop,
                         char[] domain,
                         int[] remainingFreq,
                         int streak,
                         char lastChar,
                         ArrayList<Character> sir,
                         ArrayList<ArrayList<Character>> all) {

            if (step == stop) {
                ArrayList<Character> sol = new ArrayList<>(sir);
                all.add(sol);
                return;
            }

            for (int i = 0; i < caractere.length; i++) {
                char c = caractere[i];
                if (remainingFreq[i] > 0) {
                    if (lastChar == c) {
                        if (streak < k) {
                            sir.add(c);
                            remainingFreq[i]--;
                            bkt(step + 1, stop, domain, remainingFreq,
                                    streak + 1, c, sir, all);
                            sir.remove(sir.size() - 1);
                            remainingFreq[i]++;
                        }
                    } else {
                        sir.add(c);
                        remainingFreq[i]--;
                        bkt(step + 1, stop, domain, remainingFreq,
                                1, c, sir, all);
                        sir.remove(sir.size() - 1);
                        remainingFreq[i]++;
                    }
                }
            }

        }

        private ArrayList<ArrayList<Character>> getResult() {
            ArrayList<ArrayList<Character>> all = new ArrayList<>();

            // TODO: Construiti toate sirurile cu caracterele in stringul
            // caractere (indexat de la 1 la n), si frecventele in vectorul freq
            // (indexat de la 1 la n), stiind ca nu pot fi mai mult de K
            // aparitii consecutive ale aceluiasi caracter.

            // Pentru a adauga un nou sir:
            //   ArrayList<Character> sir;
            //   all.add(sir);
            int size = 0;
            for(int f : freq) {
                size += f;
            }
            ArrayList<Character> sir = new ArrayList<>();
            bkt(0, size, caractere, freq, 0, ' ', sir, all);
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
