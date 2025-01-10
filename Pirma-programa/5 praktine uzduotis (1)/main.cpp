#include <iostream>

using namespace std;

int main() {
    system("chcp 1257 > nul");

    int eilute, stulpelis; //Saugo lentelės matmenis (eilutes ir stulpelius), kuriuos įveda vartotojas.

    cout << "Iveskite eiluciu skaiciu: ";
    cin >> eilute;
    cout << "Iveskite stulpeliu skaiciu: ";
    cin >> stulpelis;

    int** table = new int*[eilute]; //Sukuriamas dvimatis masyvas table naudojant dvimatės dinaminės atminties paskirstymą
    for (int i = 0; i < eilute; ++i) {
        table[i] = new int[stulpelis];
    }

    cout << "Iveskite lenteles reiksmes:\n"; //Vartotojas įveda kiekvieną reikšmę pagal nurodytą eilutę ir stulpelį, kuri saugoma lentelėje.
    for (int i = 0; i < eilute; ++i) {
        for (int j = 0; j < stulpelis; ++j) {
            cout << "Reiksme [" << i + 1 << "][" << j + 1 << "]: ";
            cin >> table[i][j];
        }
    }

    cout << "\nLentele:\n"; //Kodas atspausdina lentelę. Du ciklai eina per visą dvimatį masyvą ir išveda reikšmes eilutėmis
    for (int i = 0; i < eilute; ++i) {
        for (int j = 0; j < stulpelis; ++j) {
            cout << table[i][j] << " ";
        }
        cout << endl;
    }

    cout << "\nEiluciu sumos:\n";
    for (int i = 0; i < eilute; ++i) {
        int rowSum = 0; // naudojamas kiekvienos eilutės sumai suskaičiuoti
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

    int maxVal = table[0][0]; // skirtas rasti didžiausią reikšmę dvimatėje lentelėje
    for (int i = 0; i < eilute; ++i) { // i yra išorinis ciklas
        for (int j = 0; j < stulpelis; ++j) { // j yra vidinis ciklas
            if (table[i][j] > maxVal) {
                maxVal = table[i][j];
            }
        }
    }

    cout << "\nDidziausia reiksme lenteleje: " << maxVal << endl;

    for (int i = 0; i < eilute; ++i) {
        delete[] table[i]; // Kiekviena eilutė atlaisvinama atskirai
    }
    delete[] table; // išvalomas pagrindinis masyvas

    return 0;
}

