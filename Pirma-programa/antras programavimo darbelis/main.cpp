#include <iostream>
#include <string>
using namespace std;
int main() {

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