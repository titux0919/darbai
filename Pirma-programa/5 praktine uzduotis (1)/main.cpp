#include <iostream>

using namespace std;

int main() {
    system("chcp 1257 > nul");

    int eilute, stulpelis;

    cout << "Iveskite eiluciu skaiciu: ";
    cin >> eilute;
    cout << "Iveskite stulpeliu skaiciu: ";
    cin >> stulpelis;

    int** table = new int*[eilute];
    for (int i = 0; i < eilute; ++i) {
        table[i] = new int[stulpelis];
    }

    cout << "Iveskite lenteles reiksmes:\n";
    for (int i = 0; i < eilute; ++i) {
        for (int j = 0; j < stulpelis; ++j) {
            cout << "Reiksme [" << i + 1 << "][" << j + 1 << "]: ";
            cin >> table[i][j];
        }
    }

    

