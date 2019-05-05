import java.util.*;
import java.io.*;

class MyScanner {
	BufferedReader br;
	StringTokenizer st;
		 
	public MyScanner(FileInputStream f) {
		br = new BufferedReader(new InputStreamReader(f));
	}

	String next() throws IOException {
		while (st == null || !st.hasMoreElements())
			st = new StringTokenizer(br.readLine());
		return st.nextToken();
	}
		 
	int nextInt() throws IOException {
		return Integer.parseInt(next());
	}

	void close() throws IOException {
		br.close();
	}
}
