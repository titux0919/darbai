#include <iostream>
using namespace std;

/*void spausdintiAtvirksciai(int skaicius) {
    // Bazinė sąlyga: jei skaičius lygus 0, grįžtame
    if (skaicius == 0) {
        return;
    }

    // Spausdiname paskutinį skaitmenį
    cout << skaicius % 10;

    // Rekursija su skaičiumi be paskutinio skaitmens
    spausdintiAtvirksciai(skaicius / 10);
}

int main() {
    int skaicius;

    cout << "Iveskite skaiciu: ";
    cin >> skaicius;

    cout << "Atvirkstinis skaicius: ";

    // Jei skaičius yra neigiamas, spausdiname minusą ir dirbame su teigiama dalimi
    if (skaicius < 0) {
        cout << "-";
        skaicius = -skaicius;
    }

    spausdintiAtvirksciai(skaicius);
    cout << endl;

    return 0;
}
*/

/*
// Rekursinė funkcija skaičiui pakelti laipsniu
double pakeltiLaipsniu(double skaicius, int laipsnis) {
    if (laipsnis == 0)
        return 1; // Bet kuris skaičius pakeltas nuliu yra 1
    else if (laipsnis > 0)
        return skaicius * pakeltiLaipsniu(skaicius, laipsnis - 1);
    else
        return 1 / pakeltiLaipsniu(skaicius, -laipsnis); // Neigiamam laipsniui
}

int main() {
    double skaicius;
    int laipsnis;

    cout << "Iveskite skaiciu: ";
    cin >> skaicius;
    cout << "Iveskite laipsni: ";
    cin >> laipsnis;

    double rezultatas = pakeltiLaipsniu(skaicius, laipsnis);

    cout << skaicius << " pakeltas " << laipsnis << "-uoju laipsniu yra " << rezultatas << endl;

    return 0;
}
*/

/*
// Rekursinė funkcija dešimtainio skaičiaus konvertavimui į dvejetainį
void konvertuotiIDvejetaini(int skaicius) {
    if (skaicius == 0)
        return;
    konvertuotiIDvejetaini(skaicius / 2); // Daliname iš 2 ir kviečiame rekursiją
    cout << (skaicius % 2); // Spausdiname likutį
}

int main() {
    int skaicius;
    cout << "Iveskite desimtaini skaiciu: ";
    cin >> skaicius;

    if (skaicius == 0) {
        cout << "Dvejetainis skaicius: 0" << endl;
    } else {
        cout << "Dvejetainis skaicius: ";
        konvertuotiIDvejetaini(skaicius);
        cout << endl;
    }

    return 0;
}
*/