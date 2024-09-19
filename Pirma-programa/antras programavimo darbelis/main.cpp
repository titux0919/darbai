#include <iostream>
#include <string>
using namespace std;
int pirmas() {
    //pirma uzduotis
    cout<<"----------1 uzduotis----------"<<endl;
    float pazymys1, pazymys2, pazymys3;


    cout << "Iveskite pirma pazymi: ";
    cin >> pazymys1;
    cout << "Iveskite antra pazymi: ";
    cin >> pazymys2;
    cout << "Iveskite trecia pazymi: ";
    cin >> pazymys3;


    float vidurkis = (pazymys1 + pazymys2 + pazymys3) / 3;


    if (vidurkis >= 5) {
        cout << "Vidurkis teigiamas" << std::endl;
    }

    return 0;
}

//antra uzduotis
int antras () {
    cout<<"----------2 uzduotis----------"<<endl;



        double average;


        cout << "Įveskite pažymių vidurkį: ";
        cin >> average;


        if (average < 0) {
            cout << "Vidurkis yra neigiamas" << std::endl;
        } else {
            cout << "Vidurkis yra neigiamas" << std::endl;  // Čia galite pridėti kitą pranešimą, jei vidurkis nėra neigiamas
        }

        return 0;
    }
    int main () {
        // iskviesti pirmaja uzduoti
        pirmas();
        // iskviesti antraja uzduoti
        antras();
        return 0;
    }

