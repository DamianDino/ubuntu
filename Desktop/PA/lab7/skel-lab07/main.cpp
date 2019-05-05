#include <fstream>
#include <vector>
#include <algorithm>
#include <queue>
#include <iostream>
#include <climits>
using namespace std;

const int N = 100;
ofstream fout("labirint.out");

class Task {
 public:
	void solve() {
		read_input();
		get_result();
	}

 private:
	int n;
	int start_row;
	int start_col;
	int labirint[N][N];
	int d[N][N];
	const int dx[4] = {-1, 0, 1, 0};
	const int dy[4] = {0, 1, 0, -1};
	const int finalRow = 5;
	const int finalCol = 3;

	void read_input() {
		ifstream fin("labirint.in");
		fin >> n >> start_row >> start_col;
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) {
				fin >> labirint[i][j];
			}
		}

		fin.close();
	}

	typedef struct Poz {
		int x;
		int y;
	}Poz;

	void path(Poz node) {

		if (d[node.x][node.y] == 0) {
		printf("(%d %d) ", node.x, node.y);

			return;
		}

		Poz vecin;
		int i;

		for (i = 0; i < 4; i++) {
			vecin.x = node.x + dx[i];
			vecin.y = node.y + dy[i];

			if (vecin.x > 0 && vecin.x <= n && vecin.y > 0 && vecin.y <= n &&
				d[vecin.x][vecin.y] + 1 == d[node.x][node.y]) {

				path(vecin);
			}
		}

		printf("(%d %d) ", node.x, node.y);


	}

	void get_result() {

		int i, j, inf = INT_MAX;
		queue<Poz> q;
		

		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				d[i][j] = inf;
			}
		}

		d[start_row][start_col] = 0;
		Poz poz;
		poz.x = start_row;
		poz.y = start_col;
		q.push(poz);

		while (!q.empty()) {

			Poz node = q.front();
			q.pop();

			// parcurgere vecini node
			Poz vecin;

			for (i = 0; i < 4; i++) {
				vecin.x = node.x + dx[i];
				vecin.y = node.y + dy[i];

				if (vecin.x > 0 && vecin.x <= n && vecin.y > 0 && vecin.y <= n &&
					labirint[vecin.x][vecin.y] != 1 &&
					d[vecin.x][vecin.y] > 1 + d[node.x][node.y]) {

					d[vecin.x][vecin.y] = 1 + d[node.x][node.y];
					q.push(vecin);
				}
			}
		}

		int min_length = d[1][1];

		for (i = 1; i <= n; i++) {
			for (j = 1; j <= n; j++) {
				if (d[i][j] == inf)
					d[i][j] = -1;

				if(d[i][j] != -1 && labirint[i][j] == 2) {
					if (d[i][j] < min_length) {
						min_length = d[i][j];

					}
				}
			} 
		}

		cout << min_length << endl;

		Poz final;
		final.x = finalRow;
		final.y = finalCol;
		path(final);
		cout << endl;
	}
};

int main() {

	Task task;
	task.solve();
	return 0;
}