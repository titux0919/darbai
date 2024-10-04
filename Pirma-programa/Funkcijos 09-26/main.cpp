/*#pirma uzduotis
include <iostream>
using namespace std;
void ManoasmenineInformacija () {
    cout << "Vardas: Titas" << endl;
    cout << "Pasirinkau programu sistemu programa, nes domiuosi technologijomis ir man idomu suzinoti daugiau apie tai" << endl;
}
int main () {
    ManoasmenineInformacija();
    ManoasmenineInformacija();
    return 0;
}*/

/*#antra uzduotis
include <iostream>
using namespace std;

void isvestiKolegija() {
    cout << "Vilniaus kolegija" << endl;
}
void isvestiFakulteta() {
    cout << "Elektronikos ir informatikos fakultetas" << endl;
}
int main() {
    isvestiKolegija();
    isvestiFakulteta();
    return 0;
}*/

/*#trecia uzduotis
include <iostream>
#include <cstdlib>
using namespace std;

void isvestiAtsitiktiniSkaitli() {
    cout << "Atsitiktinis skaičius: " << rand() % 100 + 1 << endl;
}

int main() {
    for (int i = 0; i < 10; i++) {
        isvestiAtsitiktiniSkaitli(); // Funkcijos iškvietimas cikle
    }
    return 0;
}*/

/*#ketvirta uzduotis
include <iostream>
using namespace std;

int suma(int a, int b) {
    return a + b;
}

int skirtumas(int a, int b) {
    return a - b;
}

int sandauga(int a, int b) {
    return a * b;
}

float dalmuo(int a, int b) {
    if (b != 0) {
        return static_cast<float>(a) / b; // Vykdome dalybą
    } else {
        cout << "Dalyba iš nulio negalima!" << endl;
        return 0;
    }
}

int main() {
    int skaitmuo1, skaitmuo2;

    cout << "Iveskite pirma skaitmeni: ";
    cin >> skaitmuo1;
    cout << "Iveskite antra skaitmeni: ";
    cin >> skaitmuo2;

    cout << "Suma: " << suma(skaitmuo1, skaitmuo2) << endl;
    cout << "Skirtumas: " << skirtumas(skaitmuo1, skaitmuo2) << endl;
    cout << "Sandauga: " << sandauga(skaitmuo1, skaitmuo2) << endl;
    cout << "Dalyba: " << dalmuo(skaitmuo1, skaitmuo2) << endl;

    return 0;
}*/

/*#penkta uzduotis
include <iostream>
using namespace std;

void rastiDidziausia(int a, int b) {
    if (a > b) {
        cout << "Didziausias skaicius: " << a << endl;
    } else if (b > a) {
        cout << "Didziausias skaicius: " << b << endl;
    } else {
        cout << "Skaiciai lygus: " << a << " ir " << b << endl;
    }
}

int main() {
    rastiDidziausia(5, 10);
    rastiDidziausia(20, 15);
    rastiDidziausia(8, 8);
    rastiDidziausia(-3, 2);
    rastiDidziausia(0, -1);

    return 0;
}*/


