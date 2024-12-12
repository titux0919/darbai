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
    {"Pusryciu dribsniai", 0.69},
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
    cout << "1. Rodyti meniu" << endl;
    cout << "2. Uzsakyti patiekalus" << endl;
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

void getDataToOrderMenu() {
    int choice = 1;
    int i = 0;
    while (choice != 0) {
        showMenu();
        cout << "Iveskite patiekalo numeri ( '0' kad iseiti ): ";
        cin >> choice;
        if (choice == 0) break;

        cout << "\nIveskite kiek vienetu noresite: ";
        int amount;
        cin >> amount;

        menuOrder[i].menuItem = menuList[choice - 1].menuItem;
        menuOrder[i].menuPrice = menuList[choice - 1].menuPrice;
        menuOrder[i].amount = amount;

        clearConsole();
        cout << "Sekmingai prideta\n";
        showOrder();

        i++;
        clearConsole();
    }
}

void howMuch() {
    cout << "Jusu uzsakimas:\n";
    showOrder();
    cout << "------------------------------------------------" << endl;
    double mainPrice = 0;
    for (int i = 0; i < masyvo_dydis; i++) {
        mainPrice += menuOrder[i].menuPrice * menuOrder[i].amount;
    }
    cout << fixed << setprecision(2);
    cout << "Kaina be mokesciu - " << mainPrice << " EUR" << endl;
    cout << "Mokesciai - " << mainPrice * 0.21 << " EUR" << endl;
    cout << "Galutine kaina - " << mainPrice + mainPrice * 0.21 << " EUR" << endl;
    cout << "------------------------------------------------" << endl;

    ofstream Failas("Saskaita.txt");
    Failas << "=============== RESTORANAS ===========" << endl;
    Failas << left << setw(3) << "#" << " | "
        << setw(35) << "Patiekalo pavadinimas" << " | "
        << setw(10) << "Kaina (EUR)" << " | "
        << "Kiekis" << endl;
    Failas << "------------------------------------------------" << endl;
    for (int i = 0; i < masyvo_dydis; i++) {
        if (menuOrder[i].amount > 0) {
            Failas << left << setw(3) << i + 1 << " | "
                << setw(35) << menuOrder[i].menuItem << " | "
                << setw(10) << fixed << setprecision(2) << menuOrder[i].menuPrice << " | "
                << setw(6) << menuOrder[i].amount << endl;
        }
    }
    Failas << "------------------------------------------------" << endl;
    Failas << "Kaina be mokesciu - " << mainPrice << " EUR" << endl;
    Failas << "Mokesciai - " << mainPrice * 0.21 << " EUR" << endl;
    Failas << "Galutine kaina - " << mainPrice + mainPrice * 0.21 << " EUR" << endl;
    Failas << "------------------------------------------------" << endl;
    Failas.close();

    cout << "Failas 'Saskaita.txt' paruostas" << endl;
}

int main() {
    cout << "======= RESTORANAS ===" << endl;
    while (true) {
        mainMenu();
        int operacija;
        cin >> operacija;

        if (operacija == 1) {
            showMenu();
        }
        else if (operacija == 2) {
            getDataToOrderMenu();
            showOrder();
        }
        else if (operacija == 3) {
            clearConsole();
            howMuch();
            return 0;
        }
    }
}