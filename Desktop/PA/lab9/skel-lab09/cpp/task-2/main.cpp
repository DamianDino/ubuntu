#include <fstream>
#include <vector>
#include <algorithm>
#include <queue>
#include <cassert>
using namespace std;

const int kNmax = 50005;
const int INF = (1<<30); // sau (1<<31)-1

class Task {
 public:
	void solve() {
		read_input();
		print_output(get_result());
	}

 private:
	int n;
	int m;
	int ok = 0;
	int source;
	vector<pair<int, int> > adj[kNmax];
	vector<int> d,p;
	queue<int> q;

	void read_input() {
		ifstream fin("in");
		fin >> n >> m >> source;
		d.resize(n+1);
		p.resize(n+1);
		for (int i = 1, x, y, w; i <= m; i++) {
			fin >> x >> y >> w;
			adj[x].push_back(make_pair(y, w));
		}
		fin.close();
	}

	void BellmanFord() {
		vector<int> visited(n+1, 0);
		/* initializations */
		d = vector<int>(n+1, INF);
		p = std::vector<int>(n+1, -1);
		d[source] = 0;
		p[source] = 0;

		q.push(source);

		while (!q.empty()) {
           int node = q.front();
           q.pop();

           visited[node]++;
           if (visited[node] == n) {
           		ok=1; /* out */
               return;
           }

           for(auto &edge : adj[node]) {
           		if (d[edge.first] >= d[node] + edge.second) {
                   p[edge.first] = node;
                   d[edge.first] = d[node] + edge.second;
                   q.push(edge.first);
               }
           }
		}
		
	}

	vector<int> get_result() {
		/*
		TODO: Gasiti distantele minime de la nodul source la celelalte noduri
		folosind BellmanFord pe graful orientat cu n noduri, m arce stocat in adj.
			d[node] = costul minim / lungimea minima a unui drum de la source la nodul
		node;
			d[source] = 0;
			d[node] = -1, daca nu se poate ajunge de la source la node.

		Atentie:
		O muchie este tinuta ca o pereche (nod adiacent, cost muchie):
			adj[x][i].first = nodul adiacent lui x,
			adj[x][i].second = costul.

		In cazul in care exista ciclu de cost negativ, returnati un vector gol:
			return vector<int>();
		*/

		BellmanFord();
		if(ok == 1) {
			vector<int> nothing;
			return nothing;
		}
		else return d;
	}

	void print_output(vector<int> result) {
		ofstream fout("out");
		if (result.size() == 0) {
			fout << "Ciclu negativ!\n";
		} else {
			for (int i = 1; i <= n; i++) {
				fout << result[i] << ' ';
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
