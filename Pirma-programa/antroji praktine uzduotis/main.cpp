#include <iostream>

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

