#include <iostream>
#include <string>
using namespace std;
int pirmas() {

    //pirma uzduotis
    cout<<"----------1 uzduotis----------"<<endl;
    float pazymys1, pazymys2, pazymys3;


    cout << " Iveskite pirma pazymi: ";
    cin >> pazymys1;
    cout << " Iveskite antra pazymi: ";
    cin >> pazymys2;
    cout << " Iveskite trecia pazymi: ";
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


        cout << "Iveskite pazymiu vidurki: ";
        cin >> average;


        if (average < 0) {
            cout << "Vidurkis yra neigiamas" << std::endl;
        } else {
            cout << "Vidurkis yra teigiamas" << std::endl;
        }

        return 0;
    }

//trecia uzduotis
int trecias () {
    cout<<"----------3 uzduotis----------"<<endl;

        int pazymys;
        cout << "Įveskite egzamino pažymį: ";
        cin >> pazymys;

        if (pazymys == 10) {
            cout << "Puiku" << std::endl;
        } else if (pazymys >= 9) {
            cout << "Labai gerai" << std::endl;
        } else if (pazymys >= 7) {
            cout << "Gerai" << std::endl;
        } else if (pazymys >= 5) {
            cout << "Patenkinamai" << std::endl;
        } else {
            cout << "Egzaminas neišlaikytas" << std::endl;
        }

        return 0;
}

//ketvirta uzduotis
int ketvirtas() {
    cout<<"----------4 uzduotis----------"<<endl;

    int pazymis;
    cout << "Įveskite egzamino pažymį: ";
    cin >> pazymis;

    switch ("pazymis") {
        case 10:
            cout << "Puiku" << std::endl;
        break;
        case 9:
            cout << "Labai gerai" << std::endl;
        break;
        case 8:
        case 7:
            cout << "Gerai" << std::endl;
        break;
        case 6:
        case 5:
            cout << "Patenkinamai" << std::endl;
        break;
        default:
            cout << "Egzaminas neišlaikytas" << std::endl;
        break;
    }

    return 0;
}

//penkta uzduotis
int penktas() {
    cout<<"----------5 uzduotis----------"<<endl;


        int suma = 0;

        for (int i = 1; i <= 20; i += 2) {
            suma += i;
        }

        std::cout << "Nelyginių skaičių suma intervale [1; 20] yra: " << suma << std::endl;

        return 0;
    }

//sesta uzduotis
int sesta() {
    cout<<"----------6 uzduotis----------"<<endl;


        double pradiniSuma = 1000.0;
        double palukanuNorma = 5.0;
        int metai = 10;


        for (int i = 1; i <= metai; i++) {
            pradiniSuma += pradiniSuma * (palukanuNorma / 100);


            switch (i) {
                case 5:
                    cout << "Po 5 metų suma: " << pradiniSuma << "€" << endl;
                break;
                case 10:
                    cout << "Po 10 metų suma: " << pradiniSuma << "€" << endl;
                break;
                default:
                    break;
            }
        }

        return 0;
    }

//septinta uzduotis
int septinta() {
    cout<<"----------7 uzduotis----------"<<endl;


    for (int i = 1; i <= 20; i++) {

        if (i % 4 == 0) {

            cout << i << " dalinasi iš 4" << endl;
        }
    }

    return 0;
}
int main () {
    //iskviesti pirmaja uzduoti
    pirmas();
    //iskviesti antraja uzduoti
    antras();
    //iskviesti treciaja uzduoti
    trecias();
    //iskviesti ketvirtaja uzduoti
    ketvirtas();
    //iskviesti penktaja uzduoti
    penktas();
    //iskviesti sestaja uzduoti
    sesta();
    //iskviesti sestaja uzduoti
    septinta();
}