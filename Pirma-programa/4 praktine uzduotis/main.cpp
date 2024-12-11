#include <iostream>
#include <iomanip>
#include <string>
#include <vector>
#include <fstream>

using namespace std;

struct menuItemType {
    string menuItem;
    double menuPrice;
};

void getData(vector<menuItemType> &menuList);
void showMenu(const vector<menuItemType> &menuList);
void printCheck(const vector<menuItemType> &menuList, const vector<int> &order);

int main() {

    system("chcp 1257 >nul");

    vector<menuItemType> menuList;
    getData(menuList);

    vector<int> order(menuList.size(), 0);
    int choice;
    int quantity;

    cout << "\nSveiki atvyk� � restoran� \"Pavadinimas\"!" << endl;

    showMenu(menuList);

    while (true) {
        cout << "\nPasirinkite patiekalo numer� (arba -1, kad baigtum�te u�sakym�): ";
        cin >> choice;

        if (choice == -1) {
            break;
        }

        if (choice < 1 || choice > menuList.size()) {
            cout << "Neteisingas pasirinkimas. Bandykite dar kart�." << endl;
            continue;
        }

        cout << "�veskite porcij� skai�i�: ";
        cin >> quantity;

        if (quantity <= 0) {
            cout << "Netinkamas porcij� skai�ius. Bandykite dar kart�." << endl;
            continue;
        }

        order[choice - 1] += quantity;
    }

    printCheck(menuList, order);

    return 0;
}

void getData(vector<menuItemType> &menuList) {
    menuList = {
        {"Kiau�inien�", 1.45},
        {"Kiaulienos �onin� su keptu kiau�iniu", 2.45},
        {"Keksiukas su vy�nia", 0.99},
        {"Pranc�zi�kas skrebutis", 1.99},
        {"Vaisi� salotos", 2.49},
        {"Pusry�i� dribsniai", 0.69},
        {"Kava", 0.50},
        {"Arbata", 0.75}
    };
}

void showMenu(const vector<menuItemType> &menuList) {
    cout << "\nPusry�i� meniu:" << endl;
    cout << left << setw(3) << "Nr." << setw(40) << "Patiekalas" << "Kaina (EUR)" << endl;
    cout << string(50, '-') << endl;

    for (size_t i = 0; i < menuList.size(); ++i) {
        cout << left << setw(3) << i + 1 << setw(40) << menuList[i].menuItem << fixed << setprecision(2) << menuList[i].menuPrice << " �" << endl;
    }
}

void printCheck(const vector<menuItemType> &menuList, const vector<int> &order) {
    ofstream outFile("check.txt");
    double total = 0.0;
    double taxRate = 0.21;

    cout << "\nJ�s� s�skaita:" << endl;
    cout << left << setw(40) << "Patiekalas" << right << setw(10) << "Kaina (EUR)" << endl;
    cout << string(50, '-') << endl;

    outFile << "J�s� s�skaita:\n";
    outFile << left << setw(40) << "Patiekalas" << right << setw(10) << "Kaina (EUR)" << endl;
    outFile << string(50, '-') << endl;

    for (size_t i = 0; i < menuList.size(); ++i) {
        if (order[i] > 0) {
            double itemTotal = order[i] * menuList[i].menuPrice;
            total += itemTotal;

            cout << left << setw(3) << order[i] << setw(37) << menuList[i].menuItem << fixed << setprecision(2) << itemTotal << " �" << endl;
            outFile << left << setw(3) << order[i] << setw(37) << menuList[i].menuItem << fixed << setprecision(2) << itemTotal << " �" << endl;
        }
    }

    double tax = total * taxRate;
    double finalTotal = total + tax;

    cout << left << setw(40) << "Mokes�iai" << fixed << setprecision(2) << right << setw(10) << tax << " �" << endl;
    cout << left << setw(40) << "Galutin� suma" << fixed << setprecision(2) << right << setw(10) << finalTotal << " �" << endl;

    outFile << left << setw(40) << "Mokes�iai" << fixed << setprecision(2) << right << setw(10) << tax << " �" << endl;
    outFile << left << setw(40) << "Galutin� suma" << fixed << setprecision(2) << right << setw(10) << finalTotal << " �" << endl;

    outFile.close();
    cout << "\nS�skaita i�saugota faile \"check.txt\"." << endl;
}