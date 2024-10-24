#include <iostream>
#include <cstdlib>
#include <ctime>
#include <cctype>
#include <limits>

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

int rastiDBD(int skaicius1, int skaicius2) {
    while (skaicius2 != 0) {
        int laikinas = skaicius2;
        skaicius2 = skaicius1 % skaicius2;
        skaicius1 = laikinas;
    }
    return skaicius1;
}

int sugeneruotiSkaiciu() {
    return rand() % 100 + 1;
}

void zaidimas() {
    int atsitiktinisSkaicius = sugeneruotiSkaiciu();
    int vartotojoAtsakymas = 0;

    cout << "Atspekite atsitiktini skaiciu nuo 1 iki 100:" << endl;

    while (vartotojoAtsakymas != atsitiktinisSkaicius) {
        cout << "Iveskite savo skaiciu: ";
        cout.flush();
        cin >> vartotojoAtsakymas;

        if (vartotojoAtsakymas > atsitiktinisSkaicius) {
            cout << "Jusu skaicius yra per didelis. Bandykite dar karta." << endl;
        } else if (vartotojoAtsakymas < atsitiktinisSkaicius) {
            cout << "Jusu skaicius yra per mažas. Bandykite dar karta." << endl;
        } else {
            cout << "Teisingai! Atspejote skaiciu: " << atsitiktinisSkaicius << "!" << endl;
        }
    }
}

void fizzbuzz(int n) {
    for (int i = 1; i <= n; i++) {
        if (i % 3 == 0 && i % 5 == 0) {
            cout << "FizzBuzz" << endl;
        } else if (i % 3 == 0) {
            cout << "Fizz" << endl;
        } else if (i % 5 == 0) {
            cout << "Buzz" << endl;
        } else {
            cout << i << endl;
        }
    }
}

void rodytiMeniu() {
    cout << "Pasirinkite funkcija:\n";
    cout << "1. Patikrinti, ar raide yra balse\n";
    cout << "2. Rasti didziausia bendra dalikli\n";
    cout << "3. Zaisti spejimo zaidima\n";
    cout << "4. FizzBuzz\n";
    cout << "5. Iseiti\n";
    cout.flush();
}

int main() {
    srand(static_cast<unsigned int>(time(0)));

    while (true) {
        rodytiMeniu();
        int pasirinkimas;
        cout << "Pasirinkite: ";
        cout.flush();
        cin >> pasirinkimas;

        if (pasirinkimas == 1) {
            char ivestis;
            cout << "Iveskite raide: ";
            cout.flush();
            cin >> ivestis;

            cin.ignore(numeric_limits<streamsize>::max(), '\n');

            if (yraBalse(ivestis)) {
                cout << "Tai balse." << endl;
            } else {
                cout << "Tai nera balse." << endl;
            }

        } else if (pasirinkimas == 2) {
            int skaicius1, skaicius2;
            cout << "Iveskite pirma skaiciu: ";
            cout.flush();
            cin >> skaicius1;

            cout << "Iveskite antra skaiciu: ";
            cout.flush();
            cin >> skaicius2;

            int dbd = rastiDBD(skaicius1, skaicius2);
            cout << "Didziausias bendras daliklis: " << dbd << endl;

        } else if (pasirinkimas == 3) {
            zaidimas();

        } else if (pasirinkimas == 4) {
            int n;
            cout << "Iveskite teigiama sveikaji skaiciu: ";
            cout.flush();
            cin >> n;

            if (n > 0) {
                fizzbuzz(n);
            } else {
                cout << "Prasome ivesti teigiama skaiciu." << endl;
            }

        } else if (pasirinkimas == 5) {
            cout << "Iseinama is programos..." << endl;
            break;

        } else {
            cout << "Netinkamas pasirinkimas, bandykite dar karta.\n";
        }
    }

    return 0;
}
