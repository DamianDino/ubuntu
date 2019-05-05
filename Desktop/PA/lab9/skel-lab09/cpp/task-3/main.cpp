#include <fstream>
#include <vector>
#include <algorithm>
#include <queue>
using namespace std;

const int kNmax = 105;
const int INF = (1<<30);

class Task {
 public:
	void solve() {
		read_input();
		compute();
		print_output();
	}

 private:
	int n;
	int d[kNmax][kNmax];
	int a[kNmax][kNmax];

	void read_input() {
		ifstream fin("in");
		fin >> n;
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				fin >> a[i][j];
			}
		}
		fin.close();
	}

	void compute() {
		/*
		TODO: Gasiti distantele minime intre oricare doua noduri, folosind RoyFloyd
		pe graful orientat cu n noduri, m arce stocat in matricea ponderilor a
		(declarata mai sus).

		Atentie:
		O muchie (x, y, w) este reprezentata astfel in matricea ponderilor:
			a[x][y] = w;
		Daca nu exista o muchie intre doua noduri x si y, in matricea ponderilor
		se va afla valoarea 0:
			a[x][y] = 0;

		Trebuie sa populati matricea d[][] (declarata mai sus):
			d[x][y] = distanta minima intre nodurile x si y, daca exista drum.
			d[x][y] = 0 daca nu exista drum intre x si y.
			d[x][x] = 0.
		*/

		for(int i=1; i<=n; i++) {
			for(int j=1; j<=n; j++) {
				d[i][j] = a[i][j];
			}
		}
		
		
		for(int k=1; k<=n; k++) {
			for(int i=1; i<=n; i++) {
				for (int j=1; j<=n; j++) {
					if (d[k][j] !=0 && d[i][k] != 0) {
						if (i!=k && k!=j && j!=i) {
							if (d[i][j] == 0) {
								d[i][j] = d[k][j] + d[i][k];
							} else {
								d[i][j] = min(d[i][j], d[k][j] + d[i][k]);
							}
						} else {
							// nothing
						}
					}
				}
			}
		}

	}

	void print_output() {
		ofstream fout("out");
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				fout << d[i][j] << ' ';
			}
			fout << '\n';
		}
		fout.close();
	}
};

int main() {
	Task *task = new Task();
	task->solve();
	delete task;
	return 0;
}
