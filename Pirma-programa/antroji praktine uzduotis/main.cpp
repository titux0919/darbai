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
                cout << "Programa baige savo darba.\n";
                break;
            default:
                cout << "Neteisingas pasirinkimas. Bandykite dar karta.\n";
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
    cout << "Iveskite raide: ";
    cin >> ivestis;

    if (yraBalse(ivestis)) {
        cout << "Tai balse." << endl;
    } else {
        cout << "Tai nera balse." << endl;
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

    cout << "Iveskite pirma skaiciu: ";
    cin >> skaicius1;
    cout << "Iveskite antra skaičiu: ";
    cin >> skaicius2;

    int dbd = rastiDBD(skaicius1, skaicius2);
    cout << "Didziausias bendras daliklis: " << dbd << endl;

    return 0;
}
*/

/*#include <iostream>
#include <cstdlib>
#include <ctime>
using namespace std;

int sugeneruotiSkaičių() {
    return rand() % 100 + 1;
}

void zaidimas() {
    int atsitiktinisSkaicius = sugeneruotiSkaicių();
    int vartotojoAtsakymas = 0;

    cout << "Atspekite atsitiktini skaičiu nuo 1 iki 100:" << endl;

    while (vartotojoAtsakymas != atsitiktinisSkaicius) {
        cout << "Iveskite savo skaiciu: ";
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

int main() {
    srand(static_cast<unsigned int>(time(0)));

    zaidimas();

    return 0;
}
*/

/*#include <iostream>
using namespace std;

void fizzbuzz(int n) {
    for (int i = 1; i <= n; i++) {
        if (i % 3 == 0 && i % 5 == 0) {
            cout << "FizzBuzz" << endl;
        }
        else if (i % 3 == 0) {
            cout << "Fizz" << endl;
        }
        else if (i % 5 == 0) {
            cout << "Buzz" << endl;
        }
        else {
            cout << i << endl;
        }
    }
}

int main() {
    int n;

    cout << "Iveskite teigiama sveikaji skaicių n: ";
    cin >> n;

    if (n > 0) {
        fizzbuzz(n);
    } else {
        cout << "Prasome ivesti teigiama sveikaji skaicių." << endl;
    }

    return 0;
}
*/