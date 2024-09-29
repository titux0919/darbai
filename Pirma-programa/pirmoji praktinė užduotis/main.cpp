#include <iostream>
#include <iomanip>

using namespace std;

// Valiutų kursai
const double GBP_Bendras = 0.8593;
const double GBP_Pirkti = 0.8450;
const double GBP_Parduoti = 0.9060;

const double USD_Bendras = 1.0713;
const double USD_Pirkti = 1.0547;
const double USD_Parduoti = 1.1309;

const double INR_Bendras = 88.8260;
const double INR_Pirkti = 85.2614;
const double INR_Parduoti = 92.8334;


void palygintiValiuta(const string &valiuta) {
    if (valiuta == "GBP") {
        cout << "Dabartinis GBP kursas: " << GBP_Bendras << " EUR" << endl;
    } else if (valiuta == "USD") {
        cout << "Dabartinis USD kursas: " << USD_Bendras << " EUR" << endl;
    } else if (valiuta == "INR") {
        cout << "Dabartinis INR kursas: " << INR_Bendras << " EUR" << endl;
    } else {
        cout << "Nežinoma valiuta!" << endl;
    }
}


void pirktiValiuta(const string &valiuta, double kiekis) {
    double rezultatas = 0.0;
    if (valiuta == "GBP") {
        rezultatas = kiekis / GBP_Pirkti;
    } else if (valiuta == "USD") {
        rezultatas = kiekis / USD_Pirkti;
    } else if (valiuta == "INR") {
        rezultatas = kiekis / INR_Pirkti;
    } else {
        cout << "Nežinoma valiuta!" << endl;
        return;
    }
    cout << "Jūs gausite " << fixed << setprecision(2) << rezultatas << " " << valiuta << endl;
}

// Funkcija valiutos pardavimui
void parduotiValiuta(const string &valiuta, double kiekis) {
    double rezultatas = 0.0;
    if (valiuta == "GBP") {
        rezultatas = kiekis * GBP_Parduoti;
    } else if (valiuta == "USD") {
        rezultatas = kiekis * USD_Parduoti;
    } else if (valiuta == "INR") {
        rezultatas = kiekis * INR_Parduoti;
    } else {
        cout << "Nežinoma valiuta!" << endl;
        return;
    }
    cout << "Jūs gausite " << fixed << setprecision(2) << rezultatas << " EUR" << endl;
}

int main() {
    int pasirinkimas;
    string valiuta;
    double kiekis;

    cout << "Valiutu keitimo programa" << endl;
    cout << "1. Palyginti valiutos kursa" << endl;
    cout << "2. Pirkti valiuta" << endl;
    cout << "3. Parduoti valiuta" << endl;
    cout << "Iveskite pasirinkima: ";
    cin >> pasirinkimas;

    switch (pasirinkimas) {
        case 1:
            cout << "Iveskite valiuta palyginimui (GBP, USD, INR): ";
        cin >> valiuta;
        palygintiValiuta(valiuta);
        break;
        case 2:
            cout << "Iveskite valiuta pirkimui (GBP, USD, INR): ";
        cin >> valiuta;
        cout << "Iveskite kieki eurais: ";
        cin >> kiekis;
        pirktiValiuta(valiuta, kiekis);
        break;
        case 3:
            cout << "Iveskite valiuta pardavimui (GBP, USD, INR): ";
        cin >> valiuta;
        cout << "Iveskite valiutos kiekį: ";
        cin >> kiekis;
        parduotiValiuta(valiuta, kiekis);
        break;
        default:
            cout << "Netinkamas pasirinkimas!" << endl;
    }

    return 0;
}