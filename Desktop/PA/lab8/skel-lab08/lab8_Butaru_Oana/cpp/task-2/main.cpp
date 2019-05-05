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
	vector<int> adj[kNmax];
	int time = 0;
	vector<int> idx; //timpul de descoperire
	vector<int> low;
	vector<int> parent;
	vector<bool> visited;
	vector<int> articulations; // retinem pct de articulatie

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

	void dfsCV(int vertex) {
		/* initializations */
		int kids = 0;
		time++;
		visited[vertex] = true; //cum i-am aplicat dfs, cum devine vizitat
		/* primul low si timp de descoperire = time */
		low[vertex] = time;
		idx[vertex] = time;

		/* pt fiecare nod care ii este adiacent */
		for(auto it = adj[vertex].begin(); it != adj[vertex].end(); it++){
			if(!visited[*it]){
				// mark the parent
				parent[*it] = vertex; 
				//raise kids number
				kids++; 
				// apply dfs
				dfsCV(*it); 

				/* set the new low */
				if (low[vertex] <= low[*it]){
					low[vertex] = low[vertex];
				} else{
					low[vertex] = low[*it];
 				}

 				/* check for articulation */
 				/* root case */
 				if (parent[vertex] == -1 && kids > 1){ 
 					articulations[vertex] = true;
 				}
 				/* there is a back bone */
 				if (parent[vertex] != -1 && low[*it] >= idx[vertex]) {
 					articulations[vertex] = true;
 				}
			
			} else{
				if (*it != parent[vertex]){
					if (low[vertex] <= idx[*it]){
						low[vertex] = low[vertex];
					} else{
						low[vertex] = idx[*it];
					}
				}
			}
		}
	}


	vector<int> get_result() {
		/*
		TODO: Gasiti nodurile critice ale grafului neorientat stocat cu liste
		de adiacenta in adj.
		*/
		vector<int> sol;

		visited.resize(n+1, false);
		articulations.resize(n+1, false);
		parent.resize(n+1, -1);
		low.resize(n+1, 0);
		idx.resize(n+1, 0);

		for(int i=1;i<=n;i++){
			if (!visited[i])
				dfsCV(i);
		}

		for(int i=1;i<=n;i++){
			if(articulations[i])
				sol.push_back(i);
		}
		
		
		return sol;
	}

	void print_output(vector<int> result) {
		ofstream fout("out");
		for (int i = 0; i < int(result.size()); i++) {
			fout << result[i] << ' ';
		}
		fout << '\n';
		fout.close();
	}
};

int main() {
	Task *task = new Task();
	task->solve();
	delete task;
	return 0;
}
