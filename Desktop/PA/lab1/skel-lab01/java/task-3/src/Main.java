import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.*;
import java.lang.*;

public class Main {
	static class Task {
		public final static String INPUT_FILE = "in";
		public final static String OUTPUT_FILE = "out";

		int n, x, y;

		private void readInput() {
			try {
				Scanner sc = new Scanner(new File(INPUT_FILE));
				n = sc.nextInt();
				x = sc.nextInt();
				y = sc.nextInt();
				sc.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int answer) {
			try {
				PrintWriter pw = new PrintWriter(new File(OUTPUT_FILE));
				pw.printf("%d\n", answer);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		private static int power(int x, int y) {
	        	if (y == 0)
	            		return 1;
	        	else if (y % 2 == 0)
	            		return power(x, y / 2) * power(x, y / 2);
	        	else
	           		 return x * power(x, y / 2) * power(x, y / 2);
	    	}

		public static int getAnswer(int n, int x, int y) {
			// TODO: Calculati valoarea de pe pozitia (x, y) din matricea de dimensiune
			// 2^N * 2^N.
			
			if (n == 0){
				return 1;
			}

			if((y <= power(2,n-1)) && (x <= power(2,n-1))) {
				return getAnswer(n-1, x, y);
			}
			
			if((y > power(2,n-1)) && (x <= power(2,n-1))) {
				return power(2,2*n-2) + getAnswer(n-1, x, y - power(2,n-1));
			}
			
			if((y <= power(2,n-1)) && (x > power(2,n-1))) {
				return power(2,2*n-1)+getAnswer(n-1, x - power(2,n-1), y );
			}
			
			if((y > power(2,n-1)) && (x > power(2,n-1))) {
				return 3*power(2,2*n-2)+getAnswer(n-1, x - power(2,n-1), y - power(2,n-1));
			}
			
			return -1;
			
		}

		public void solve() {
			readInput();
			writeOutput(getAnswer(n, x, y));
		}
	}

	public static void main(String[] args) {
		new Task().solve();
		
		
	}
}
