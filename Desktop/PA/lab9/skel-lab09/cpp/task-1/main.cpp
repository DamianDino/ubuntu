#include <fstream>
#include <vector>
#include <algorithm>
#include <queue>
#include <utility>
#include <set>
#include <climits>
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
	int source;
	vector<pair<int, int> > adj[kNmax];
	priority_queue<pair<int, int>, vector<pair<int, int>>,
                  std::greater<pair<int, int>>> priq;
	vector<int> distance;
	vector<int> parent;

	void read_input() {
		ifstream fin("in");
		fin >> n >> m >> source;
		distance.resize(n+1);
		parent.resize(n+1);
		for (int i = 1, x, y, w; i <= m; i++) {
			fin >> x >> y >> w;
			adj[x].push_back(make_pair(y, w));
		}
		fin.close();
	}


	vector<int> get_result() {
		/*
		TODO: Gasiti distantele minime de la nodul source la celelalte noduri
		folosind Dijkstra pe graful orientat cu n noduri, m arce stocat in adj.
			d[node] = costul minim / lungimea minima a unui drum de la source la nodul
		node;
			d[source] = 0;
			d[node] = -1, daca nu se poate ajunge de la source la node.

		Atentie:
		O muchie este tinuta ca o pereche (nod adiacent, cost muchie):
			adj[x][i].first = nodul adiacent lui x,
			adj[x][i].second = costul.
		*/

		/* initializations */
		for(int i = 1;i <= n; i++) {
			distance[i] = INF;
			parent[i] = -1;
		}

        distance[source] = 0;
        parent[source] = 0;

		priq.push({distance[source], source});
        while (!priq.empty()) {
            auto entry = priq.top();
            priq.pop();
            int cost = entry.first;
            int node = entry.second;
            if (cost <= distance[node]) {
                for (auto &edge : adj[node]) {
                    int neighbour = edge.first;
                    int cost_edge = edge.second;

                    if (distance[node] + cost_edge < distance[neighbour]) {
                        distance[neighbour] = distance[node] + cost_edge;
                        parent[neighbour] = node;
                        priq.push({distance[neighbour], neighbour});
                     }
                }
            }
        }

		for(int i=1; i<=n; i++) {
			if (distance[i] == INF) {
				distance[i] = -1; //de fapt nu sunt conectate
			}
		}


		return distance;
	}

	void print_output(vector<int> result) {
		ofstream fout("out");
		for (int i = 1; i <= n; i++) {
			fout << result[i] << " ";
		}
		fout << "\n";
		fout.close();
	}
};

int main() {
	Task *task = new Task();
	task->solve();
	delete task;
	return 0;
}
