#include <fstream>
#include <iomanip>
#include <vector>
using namespace std;

struct Object {
	int weight;
	int price;

	Object(int _weight, int _price) : weight(_weight), price(_price) {}
};

class Task {
 public:
	void solve() {
		read_input();
		print_output(get_result());
	}

 private:
	int n, w;
	vector<Object> objs;

	void read_input() {
		ifstream fin("in");
		fin >> n >> w;
		for (int i = 0, weight, price; i < n; i++) {
			fin >> weight >> price;
			objs.push_back(Object(weight, price));
		}
		fin.close();
	}

	// comparator dupa raportul pret/kg
	bool cmp (pair<int, double>& o1, pair<int, double>& o2){
		double r1 = (double) o1.second / o1.first;
		double r2 = (double) o2.second / o2.first;
		return r1>r2;
	}

	double get_result() {
		/*
		TODO: Aflati profitul maxim care se poate obtine cu obiectele date.
		*/
		sort(objs.begin(), objs.end(), cmp());

		int crtWeight = 0;
		double final_value = 0.0;

		for (auto object : Object){
			if (crtWeight + object.weight <= w){
				crtWeight += object.weight;
				final_value += object.value;
			} else {
				int remain = w - crtWeight;
				final_value += object.value * ((double) remain / object.weight);
			}
		}


		return final_value;
	}

	void print_output(double result) {
		ofstream fout("out");
		fout << setprecision(4) << fixed << result;
		fout.close();
	}
};

int main() {
	Task task;
	task.solve();
	return 0;
}