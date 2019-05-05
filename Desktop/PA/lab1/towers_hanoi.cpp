#include <iostream>
using namespace std;

void towerOfHanoi(int n, char from, char to, char aux) 
{ 
    if (n == 1) 
    { 
    	cout << "Move disk 1 from " << from << " to " << to << endl;
        //printf("\n Move disk 1 from %c to %c", from, to); 
        return; 
    } 
    towerOfHanoi(n-1, from, aux, to); 
    cout << "Move disk " << n << " from " << from << " to " << to << endl;
    //printf("\n Move disk %d from %c to %c", n, from, to); 
    towerOfHanoi(n-1, aux, to, from); 
} 
  
int main() 
{ 
    int n = 3; // Number of disks 
    towerOfHanoi(n, 'A', 'C', 'B');  // A, B and C are names of rods 
    return 0; 
} 