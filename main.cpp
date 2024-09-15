#include <iostream>
#include <string>
using namespace std;
int pirmas() {
    //pirma uzduotis
    cout<<"----------1 uzduotis----------"<<endl;
    string vardas = "Titas";
    string pavarde = "Saviscevas";
    int amzius = 18;
    string grupe = "PI24";
    int kursas = 2;
    string studijuPrograma = "Programu sistemos";

    cout << "Studento vardas: " << vardas << endl;
    cout << "Studento pavarde: " << pavarde << endl;
    cout << "Studento amzius: " << amzius << endl;
    cout << "Studento grupe: " << grupe << endl;
    cout << "Studento kursas: " << kursas << endl;
    cout << "Studento studiju programa: " << studijuPrograma << endl;
    return 0;
}
    //antra uzduotis
int antras () {
    cout<<"----------2 uzduotis----------"<<endl;


    string pavadinimas = "Vilniaus Rytas";
    int ikurimoMetai = 1998;
    string savininkas = "Darius Gudelis";
    string arena1 = "Twinsbet Arena";
    string arena2 = "Jeep Arena";
    int vietuSkaicius1 = 10200;
    float vietuSkaicius2 = 2.741;

    cout << "Sporto klubo informacija: " << endl;
    cout << "Pavadinimas: " << pavadinimas << endl;
    cout << "Ikurimo metai: " << ikurimoMetai << endl;
    cout << "Savininkas: " << savininkas << endl;
    cout << "Arena 1: " << arena1 << endl;
    cout << "Arena 2: " << arena2 << endl;
    cout << "Vietu skaicius 1 arenos: " << vietuSkaicius1 << endl;
    cout << "Vietu skaicius 2 arenos: " << vietuSkaicius2 << endl;
    return 0;
}
    //trecia uzduotis
int trecias () {
    cout<<"---------3 uzduotis----------"<<endl;
    string marke = "OPEL";
    string modelis = "ZAFIRA";
    int pagaminimoMetai = 2010;
    float litrazas = 2.0;
    string spalva = "sidabrine";

    cout << "Automobilis " << marke << " " << modelis
    << " yra pagamintas " << pagaminimoMetai  << " metais. "
    << "Jo motoras " << litrazas << " litrazo. "
    << "Automobilis yra " << spalva <<" spalvos."<<endl;
    return 0;
 }
    //ketvirta uzduotis
int ketvirtas () {
    cout<<"---------4 uzduotis----------"<<endl;
    int a = 13;
    int b = 5;
    double c = 17.5;

    cout << a + b - c << endl; // 13 + 5 - 17.5 = 0.5
    cout << 15 / 2 + c << endl; // 15 / 2 = 7 + 17.5 = 24.5
    cout << a / static_cast<double>(b) + 2 * c << endl; // 13 / 5.0 = 2.6 + 2 * 17.5 = 37.6
    cout << 14 % 3 + 6.3 + b / a << endl; // 14 % 3 = 2 + 6.3 + 5 / 13 = 2 + 6.3 + 0 = 8.3
    cout << static_cast<int>(c) % 5 + a - b << endl; // static_cast<int>(17.5) = 17; 17 % 5 = 2 + 13 - 5 = 10
    cout << 13.5 / 2 + 4.0 * 3.5 + 18 << endl; // 13.5 / 2 = 6.75 + 4.0 * 3.5 = 14 + 18 = 38.75
    return 0;
}
    //penkta uzduotis
int penktas () {
    cout<<"---------5 uzduotis----------"<<endl;
    double pirmasSK, antrasSK, treciasSK, ketvirtasSK, penktasSK;

    cout << "Ivedamas pirmas skaicius: ";
    cin >> pirmasSK;
    cout << "Ivedamas antras skaicius: ";
    cin >> antrasSK;
    cout << "Ivedamas trecias skaicius: ";
    cin >> treciasSK;
    cout << "Ivedamas ketvirtas skaicius: ";
    cin >> ketvirtasSK;
    cout << "Ivedamas penktas skaicius: ";
    cin >> penktasSK;

    double vidurkis = (pirmasSK + antrasSK+treciasSK+ketvirtasSK+penktasSK) / 5;
    cout << "skaiciu vidurkis" << endl;
    return 0;
}
    //sesta uzduotis
int sestas () {
    int n;
    cout<<"---------6 uzduotis----------"<<endl;
    cout << "Iveskite dvizenkli skaiciu: ";
    cin >> n;

    int desimtys = n / 10;
    int vienetai = n % 10;
    int suma = desimtys + vienetai;
    cout << "skaitmenu suma" << suma << endl;
    return 0;
}
int main () {
    // iskviesti pirmaja uzduoti
    pirmas();
    // iskviesti antraja uzduoti
    antras ();
    // iskviesti treciaja uzduoti
    trecias ();
    // iskvesti ketvirtaja uzduoti
    ketvirtas();
    // iskviesti penktaja uzduoti
    penktas();
    // iskviesti sestaja uzduoti
    sestas();

    return 0;
}