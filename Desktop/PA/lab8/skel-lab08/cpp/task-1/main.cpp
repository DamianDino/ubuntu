#include <fstream>
#include <vector>
#include <algorithm>
#include <queue>
#include <cstdio>
#include <stack>
#include <cstring>
using namespace std;

const int kNmax = 100005;

class Task {
 public:
	void solve() {
		read_input();
		//get_result();
		//write();
		print_output(get_result());
	}

 private:
	int n;
	int m;
	// int components;
	vector<int> adj[kNmax];  // graful
	vector<int> adjt[kNmax]; // graful transpus
	vector<bool> visited;
	vector<int> aux;

	void read_input() {
		ifstream fin("in");
		fin >> n >> m;
		for (int i = 1, x, y; i <= m; i++) {
			fin >> x >> y;
			adj[x].push_back(y);
			adjt[y].push_back(x);
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

	void print_rev_graph() {
		printf("REVERSED\n");
		for(int i=1; i<=n; i++){
			printf("%d -> ", i);
			for(vector<int>::iterator it = adjt[i].begin(); 
				it != adjt[i].end(); ++it) {
				printf("%d ", *it);
			}
			printf("\n");
		}
	}

	void dfs (int node){
		visited[node] = true;
		for(vector<int>::iterator it = adj[node].begin();
			it != adj[node].end(); ++it){
			if (!visited[*it]){
				dfs(*it);
			}
		}	
		aux.push_back(node);
	}


	void dfs_reverse (int node, vector<int>& crt_ctc) {
	    visited[node] = false;
	    crt_ctc.push_back(node);

	    for(vector<int>::iterator it = adjt[node].begin(); 
	    	it != adjt[node].end(); ++it) {
	        if(visited[*it]) {
	            dfs_reverse(*it, crt_ctc);
	        } 
	    }
	}
	
	void kosaraju(vector<vector<int>>& sol) {
		visited.resize(n+1, false);
		for(int i=0; i<=n; i++){
			if(!visited[i])
				dfs(i);
		}

		for(int i=n; i>=1; i--) {
			if (visited[aux[i]]) {
				vector<int> crt_ctc;
				dfs_reverse(aux[i], crt_ctc);
				sol.push_back(crt_ctc);
			}
		}
		
	}



	vector<vector<int>> get_result() {
		/*
		TODO: Gasiti componentele tare conexe ale grafului orientat cu
		n noduri, stocat in adj. Rezultatul se va returna sub forma
		unui vector, ale carui elemente sunt componentele tare conexe
		detectate. Nodurile si componentele tare conexe pot fi puse in orice
		ordine in vector.
		
		Atentie: graful transpus este stocat in adjt.
		*/
		vector<vector<int>> sol;
		
		kosaraju(sol);
		
		return sol;
		
	}

	// void write(){
	// 	//printf("NR COMP = %d\n", components);
	// 	ofstream fout("out");
	// 	fout<<components<<"\n";
	// 	for(const auto& ctc : Results){
	// 		fout<<ctc<<' ';
	// 	}
	// 	fout<<"\n";
	// 	// for(int i=1; i<=components; i++){
	// 	// 	for(vector<int>::iterator it = Results[i].begin();
	// 	// 		it != Results[i].end(); ++it){
	// 	// 		printf("%d ", *it);
	// 	// 	}
	// 	// 	printf("\n");
	// 	// }
	// }

	void print_output(vector<vector<int>> result) {
		ofstream fout("out");
		//fout << "fmm " << result.size() << '\n';
		fout << result.size() << "\n";
		for (const auto& ctc : result) {
			for (int nod : ctc) {
				fout << nod << ' ';
			}
			fout << "\n";
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


