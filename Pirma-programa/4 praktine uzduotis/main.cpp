#include <iostream>
#include <string>
#include <iomanip>
#include <fstream>
using namespace std;

struct menuItemType {
    string menuItem;
    double menuPrice;
};
int const masyvo_dydis = 8;
menuItemType menuList[8] = {
    {"Kiausiniene", 1.45},
    {"Kiaulienos sonine su keptu kiausiniu", 2.45},
    {"Keksiukas su vysnia", 0.99},
    {"Prancuziskas skrebutis", 1.99},
    {"Vaisiu salotos", 2.49},
    {"Pusryčiu dribsniai", 0.69},
    {"Kava", 0.50},
    {"Arbata", 0.75},
};

struct menuItemOrder {
    string menuItem;
    double menuPrice;
    double amount;
};
menuItemOrder menuOrder[8] = {};

void clearConsole() {
    for (int i = 0; i < 100; i++) {
        cout << endl;
    }
}

void mainMenu() {
    cout << "1. Show menu" << endl;
    cout << "2. Uzsakuti patiekalus" << endl;
    cout << "3. Gauti saskaita" << endl;
}

void showMenu() {
    clearConsole();
    cout << "------------------------------------------------" << endl;
    cout << left << setw(3) << "#" << " | "
        << setw(35) << "Patiekalo pavadinimas" << " | "
        << "Kaina (EUR)" << endl;
    cout << "------------------------------------------------" << endl;
    for (int i = 0; i < masyvo_dydis; i++) {
        cout << left << setw(3) << i + 1 << " | "
            << setw(35) << menuList[i].menuItem << " | "
            << fixed << setprecision(2) << menuList[i].menuPrice << endl;
    }
    cout << "------------------------------------------------" << endl;
}

void showOrder() {
    clearConsole();
    cout << "------------------------------------------------" << endl;
    cout << left << setw(3) << "#" << " | "
        << setw(35) << "Patiekalo pavadinimas" << " | "
        << setw(10) << "Kaina (EUR)" << " | "
        << "Kiekis" << endl;
    cout << "------------------------------------------------" << endl;
    for (int i = 0; i < masyvo_dydis; i++) {
        if (menuOrder[i].amount > 0) {
            cout << left << setw(3) << i + 1 << " | "
                << setw(35) << menuOrder[i].menuItem << " | "
                << setw(10) << fixed << setprecision(2) << menuOrder[i].menuPrice << " | "
                << setw(6) << menuOrder[i].amount << endl;
        }
    }
    cout << "------------------------------------------------" << endl;
}