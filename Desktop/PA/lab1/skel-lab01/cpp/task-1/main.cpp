#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

class Task {
public:
	void solve() {
		read_input();
		print_output(get_result());
	}

private:
	int n, x;
	vector<int> v;

	void read_input() {
		ifstream fin("in");
		fin >> n;
		for (int i = 0, e; i < n; i++) {
			fin >> e;
			v.push_back(e);
		}
		fin >> x;
		fin.close();
	}

	int find_first() {
		/*
		TODO: Cautati prima pozitie pe care se afla valoarea x
		In cazul in care x nu apare in sir, returnati -1
		*/
		return 0;
	}

	int find_last() {
		/*
		TODO: Cautati ultima pozitie pe care se afla valoarea x
		In cazul in care x nu apare in sir, returnati -1
		*/
		return 0;
	}

	int get_result() {
		// TODO: Calculati numarul de aparitii ale lui x in vectorul v
		return 0;
	}

	void print_output(int result) {
		ofstream fout("out");
		fout << result;
		fout.close();
	}
};

int main() {
	Task task;
	task.solve();
	return 0;
}
