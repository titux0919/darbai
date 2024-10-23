/*#include <iostream>

using namespace std;

void funkcija1() {
    cout << "Atlikta funkcija 1.\n";
}

void funkcija2() {
    cout << "Atlikta funkcija 2.\n";
}

void funkcija3() {
    cout << "Atlikta funkcija 3.\n";
}

void funkcija4() {
    cout << "Atlikta funkcija 4.\n";
}

int main() {
    int pasirinkimas;
    bool programaVeikia = true;

    while (programaVeikia) {
        cout << "\n--- Meniu ---\n";
        cout << "1. Vykdyti funkcija 1\n";
        cout << "2. Vykdyti funkcija 2\n";
        cout << "3. Vykdyti funkcija 3\n";
        cout << "4. Vykdyti funkcija 4\n";
        cout << "0. Iseiti is programos\n";
        cout << "Pasirinkite funkcija: ";
        cin >> pasirinkimas;

        switch (pasirinkimas) {
            case 1:
                funkcija1();
                break;
            case 2:
                funkcija2();
                break;
            case 3:
                funkcija3();
                break;
            case 4:
                funkcija4();
                break;
            case 0:
                programaVeikia = false;
                cout << "Programa baigė darbą.\n";
                break;
            default:
                cout << "Neteisingas pasirinkimas. Bandykite dar kartą.\n";
        }
    }

    return 0;
}
*/

/*#include <iostream>
using namespace std;

bool yraBalse(char raide) {
    char mazosios[] = {'a', 'e', 'i', 'o', 'u'};
    char didziosios[] = {'A', 'E', 'I', 'O', 'U'};

    for (int i = 0; i < 5; i++) {
        if (raide == mazosios[i] || raide == didziosios[i]) {
            return true;
        }
    }

    return false;
}

int main() {
    char ivestis;
    cout << "Įveskite raidę: ";
    cin >> ivestis;

    if (yraBalse(ivestis)) {
        cout << "Tai balsė." << endl;
    } else {
        cout << "Tai nėra balsė." << endl;
    }

    return 0;
}
*/

/*#include <iostream>
using namespace std;

int rastiDBD(int skaicius1, int skaicius2) {
    while (skaicius2 != 0) {
        int laikinas = skaicius2;
        skaicius2 = skaicius1 % skaicius2;
        skaicius1 = laikinas;
    }
    return skaicius1;
}

int main() {
    int skaicius1, skaicius2;

    cout << "Įveskite pirmą skaičių: ";
    cin >> skaicius1;
    cout << "Įveskite antrą skaičių: ";
    cin >> skaicius2;

    int dbd = rastiDBD(skaicius1, skaicius2);
    cout << "Didžiausias bendras daliklis: " << dbd << endl;

    return 0;
}
*/

