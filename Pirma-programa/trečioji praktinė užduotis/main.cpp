#include <iostream>
#include <string>
#include <cctype>
using namespace std;

const char abecele[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
const int abeceleIlgis = 26;

int simboliopozicija(char simbolis) {
    for (int i = 0; i < abeceleIlgis; i++) {
        if (abecele[i] == simbolis) {
            return i;
        }
    }
}


//ABECELE
char sifruotisimboli(char simbolis, char raktas) {
    return abecele[(simboliopozicija(simbolis) + simboliopozicija(raktas)) % abeceleIlgis];
}
char desifruotisimboli(char simbolis, char raktas) {
    return abecele[(simboliopozicija(simbolis) - simboliopozicija(raktas) + abeceleIlgis) % abeceleIlgis];
}
string sifruotitexsta(string tekstas, string raktas) {
    int tekstoilgis = tekstas.length();
    string rezultatas(tekstoilgis, ' ');
    for (int i = 0; i < tekstoilgis; i++) {
        rezultatas[i] = sifruotisimboli(tekstas[i], raktas[i % raktas.length()]);
    }
    return rezultatas;
}
string desifruotitexsta(string tekstas, string raktas) {
    int tekstoilgis = tekstas.length();
    string rezultatas(tekstoilgis, ' ');
    for (int i = 0; i < tekstoilgis; i++) {
        rezultatas[i] = desifruotisimboli(tekstas[i], raktas[i % raktas.length()]);
    }
    return rezultatas;
}

//ASCII
char Simbol_to_ASCII(char simbolis) {
    int number;
    number = int(simbolis);
    return number;
}
char ASCII_to_Simbol(int number) {
    char simbol = (char)number;
    return simbol;
}
string Sifruoti_to_ASCII(string tekstas, string raktas) {
    int tekstoilgis = tekstas.length();
    string rezultatas(tekstoilgis, ' ');
    for (int i = 0; i < tekstoilgis; i++) {
        int simbolisASCII = Simbol_to_ASCII(tekstas[i]);
        int raktasASCII = Simbol_to_ASCII(raktas[i % raktas.length()]);
        rezultatas[i] = ASCII_to_Simbol((simbolisASCII - 32 + raktasASCII) % 95 + 32);
    }
    return rezultatas;
}

string Desifruoti_is_ASCII(string tekstas, string raktas) {
    int tekstoilgis = tekstas.length();
    string rezultatas(tekstoilgis, ' ');
    for (int i = 0; i < tekstoilgis; i++) {
        int simbolisASCII = Simbol_to_ASCII(tekstas[i]);
        int raktasASCII = Simbol_to_ASCII(raktas[i % raktas.length()]);

        rezultatas[i] = ASCII_to_Simbol((simbolisASCII - 32 - raktasASCII + 95) % 95 + 32);
    }
    return rezultatas;
}

char testi;

int main() {
    while (true)
    {


        cout << "\nPasirinkite finkcija" << endl;
        cout << "1. Vigenere algoritmas" << endl;
        cout << "2. ASCII" << endl;
        cout << "3. Iseiti" << endl;
        int pasirinkimas;
        cin >> pasirinkimas;
        if (pasirinkimas==3)
        {
            cout << "Viso gero" << endl;
            return 0;
        }
        if (pasirinkimas == 1)
        {
            int metodas;
            cout << "Sifravimas ar desifravimas?" << endl;
            cout << "1. Sifraviamas" << endl;
            cout << "2. Desifravimas" << endl;
            cin >> metodas;
            if (metodas == 1)
            {
                string tekstas;
                string raktas;

                cout << "Ivesakite teksta: ";
                cin >> tekstas;
                cout << "Iveskite rakta: ";
                cin >> raktas;

                for (int i = 0; i < raktas.length(); i++)
                {
                    raktas[i] = toupper(raktas[i]);

                }
                for (int i = 0; i < tekstas.length(); i++)
                {
                    tekstas[i] = toupper(tekstas[i]);
                }

                string rezultatas = sifruotitexsta(tekstas, raktas);
                cout << "Sifruotas tekstas: " << rezultatas << endl;
            }
            if (metodas == 2)
            {
                string tekstas;
                string raktas;

                cout << "Ivesakite teksta: ";
                cin >> tekstas;
                cout << "Iveskite rakta: ";
                cin >> raktas;

                for (int i = 0; i < raktas.length(); i++)
                {
                    raktas[i] = toupper(raktas[i]);

                }
                for (int i = 0; i < tekstas.length(); i++)
                {
                    tekstas[i] = toupper(tekstas[i]);
                }

                string rezultatas = desifruotitexsta(tekstas, raktas);
                cout << "Disifruotas tekstas: " << rezultatas << endl;
            }
        }
        if (pasirinkimas == 2)
        {
            int metodas;
            cout << "Sifravimas ar desifravimas?" << endl;
            cout << "1. Sifraviamas" << endl;
            cout << "2. Desifravimas" << endl;
            cin >> metodas;
            if (metodas == 1)
            {
                string tekstas;
                string raktas;
                cout << "Iveskite teksta: ";
                cin >> tekstas;
                cout << "Iveskite rakta: ";
                cin >> raktas;
                cout << Sifruoti_to_ASCII(tekstas, raktas) << endl;
            }
            if (metodas == 2)
            {
                string tekstas;
                string raktas;
                cout << "Iveskite teksta: ";
                cin >> tekstas;
                cout << "Iveskite rakta: ";
                cin >> raktas;
                cout << Desifruoti_is_ASCII(tekstas, raktas) << endl;
            }
        }
    }

}