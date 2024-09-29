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

