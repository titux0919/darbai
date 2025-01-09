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

    cout << "\nLentele:\n";
    for (int i = 0; i < eilute; ++i) {
        for (int j = 0; j < stulpelis; ++j) {
            cout << table[i][j] << " ";
        }
        cout << endl;
    }

    cout << "\nEiluciu sumos:\n";
    for (int i = 0; i < eilute; ++i) {
        int rowSum = 0;
        for (int j = 0; j < stulpelis; ++j) {
            rowSum += table[i][j];
        }
        cout << "Eilutes " << i + 1 << " suma: " << rowSum << endl;
    }

    cout << "\nStulpeliu sumos:\n";
    for (int j = 0; j < eilute; ++j) {
        int colSum = 0;
        for (int i = 0; i < stulpelis; ++i) {
            colSum += table[i][j];
        }
        cout << "Stulpelio " << j + 1 << " suma: " << colSum << endl;
    }

    int maxVal = table[0][0];
    for (int i = 0; i < eilute; ++i) {
        for (int j = 0; j < stulpelis; ++j) {
            if (table[i][j] > maxVal) {
                maxVal = table[i][j];
            }
        }
    }

    cout << "\nDidziausia reiksme lenteleje: " << maxVal << endl;

    for (int i = 0; i < eilute; ++i) {
        delete[] table[i];
    }
    delete[] table;

    return 0;
}

