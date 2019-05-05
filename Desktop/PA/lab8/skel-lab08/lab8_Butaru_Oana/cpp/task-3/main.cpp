#include <fstream>
#include <vector>
#include <algorithm>
#include <queue>
using namespace std;

const int kNmax = 100005;

class Task {
 public:
	void solve() {
		read_input();
		print_output(get_result());
		//write();
	}

 private:
	int n;
	int m;
	int time = 0;

	struct Edge {
		int x;
		int y;
	};

	vector<Edge> sol;

	vector<int> adj[kNmax];
	vector<bool> visited;
	vector<int> low;
	vector<int> parent;
	vector<int> idx;
	

	

	void read_input() {
		ifstream fin("in");
		fin >> n >> m;
		for (int i = 1, x, y; i <= m; i++) {
			fin >> x >> y;
			adj[x].push_back(y);
			adj[y].push_back(x);
		}
		fin.close();
	}

	void print_graph() {
		printf("GRAF IN ORDINE\n");
		for(int i=1; i<=n; i++){
			printf("%d -> ", i);
			for(vector<int>::iterator it = adj[i].begin(); 
				it != adj[i].end(); ++it) {
				printf("%d ", *it);
			}
			printf("\n");
		}
	}

	void write(){
		print_graph();
	}

	void dfsB (int vertex) {
		/* initializations */
		time++; 
		visited[vertex] = true; //cum i-am aplicat dfs, cum devine vizitat
		/* primul low si timp de descoperire = time */
		idx[vertex] = time;
		low[vertex] = time;

		/* pt fiecare nod care ii este adiacent */
		for(auto it = adj[vertex].begin(); it != adj[vertex].end(); it++) {
			if (!visited[*it]) {
				// mark the parent
				parent[*it] = vertex;
				// apply dfs
				dfsB(*it);
				
				/* set the new low */
				if (low[vertex] <= low[*it]) {
					low[vertex] = low[vertex];
				} else {
					low[vertex] = low[*it];
				}
				
				if (low[*it] > idx[vertex]) {
					Edge edge;
					edge.x = vertex;
					edge.y = *it;
					sol.push_back(edge);
				}
			
			} else {
				if (parent[vertex] != *it) {
					if (low[vertex] <= idx[*it]) {
						low[vertex] = low[vertex];
					} else {
						low[vertex] = idx[*it];
					}
				}
			}
		}
	}

	vector<Edge> get_result() {
		/*
		TODO: Gasiti muchiile critice ale grafului neorientat stocat cu liste
		de adiacenta in adj.
		*/

		//vector<Edge> sol;
		visited.resize(n+1, false);
		parent.resize(n+1, -1);
		low.resize(n+1, 0);
		idx.resize(n+1, 0);

		for(int i=1;i<=n;i++){
			if (!visited[i])
				dfsB(i);
		}
		return sol;
	}

	void print_output(vector<Edge> result) {
		ofstream fout("out");
		fout << result.size() << '\n';
		for (int i = 0; i < int(result.size()); i++) {
			fout << result[i].x << ' ' << result[i].y << '\n';
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
