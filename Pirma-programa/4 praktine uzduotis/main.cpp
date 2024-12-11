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

    cout << "\nSveiki atvykæ á restoranà \"Pavadinimas\"!" << endl;

    showMenu(menuList);

    while (true) {
        cout << "\nPasirinkite patiekalo numerá (arba -1, kad baigtumëte uþsakymà): ";
        cin >> choice;

        if (choice == -1) {
            break;
        }

        if (choice < 1 || choice > menuList.size()) {
            cout << "Neteisingas pasirinkimas. Bandykite dar kartà." << endl;
            continue;
        }

        cout << "Áveskite porcijø skaièiø: ";
        cin >> quantity;

        if (quantity <= 0) {
            cout << "Netinkamas porcijø skaièius. Bandykite dar kartà." << endl;
            continue;
        }

        order[choice - 1] += quantity;
    }

    printCheck(menuList, order);

    return 0;
}

void getData(vector<menuItemType> &menuList) {
    menuList = {
        {"Kiauðinienë", 1.45},
        {"Kiaulienos ðoninë su keptu kiauðiniu", 2.45},
        {"Keksiukas su vyðnia", 0.99},
        {"Prancûziðkas skrebutis", 1.99},
        {"Vaisiø salotos", 2.49},
        {"Pusryèiø dribsniai", 0.69},
        {"Kava", 0.50},
        {"Arbata", 0.75}
    };
}

void showMenu(const vector<menuItemType> &menuList) {
    cout << "\nPusryèiø meniu:" << endl;
    cout << left << setw(3) << "Nr." << setw(40) << "Patiekalas" << "Kaina (EUR)" << endl;
    cout << string(50, '-') << endl;

    for (size_t i = 0; i < menuList.size(); ++i) {
        cout << left << setw(3) << i + 1 << setw(40) << menuList[i].menuItem << fixed << setprecision(2) << menuList[i].menuPrice << " €" << endl;
    }
}

void printCheck(const vector<menuItemType> &menuList, const vector<int> &order) {
    ofstream outFile("check.txt");
    double total = 0.0;
    double taxRate = 0.21;

    cout << "\nJûsø sàskaita:" << endl;
    cout << left << setw(40) << "Patiekalas" << right << setw(10) << "Kaina (EUR)" << endl;
    cout << string(50, '-') << endl;

    outFile << "Jûsø sàskaita:\n";
    outFile << left << setw(40) << "Patiekalas" << right << setw(10) << "Kaina (EUR)" << endl;
    outFile << string(50, '-') << endl;

    for (size_t i = 0; i < menuList.size(); ++i) {
        if (order[i] > 0) {
            double itemTotal = order[i] * menuList[i].menuPrice;
            total += itemTotal;

            cout << left << setw(3) << order[i] << setw(37) << menuList[i].menuItem << fixed << setprecision(2) << itemTotal << " €" << endl;
            outFile << left << setw(3) << order[i] << setw(37) << menuList[i].menuItem << fixed << setprecision(2) << itemTotal << " €" << endl;
        }
    }

    double tax = total * taxRate;
    double finalTotal = total + tax;

    cout << left << setw(40) << "Mokesèiai" << fixed << setprecision(2) << right << setw(10) << tax << " €" << endl;
    cout << left << setw(40) << "Galutinë suma" << fixed << setprecision(2) << right << setw(10) << finalTotal << " €" << endl;

    outFile << left << setw(40) << "Mokesèiai" << fixed << setprecision(2) << right << setw(10) << tax << " €" << endl;
    outFile << left << setw(40) << "Galutinë suma" << fixed << setprecision(2) << right << setw(10) << finalTotal << " €" << endl;

    outFile.close();
    cout << "\nSàskaita iðsaugota faile \"check.txt\"." << endl;
}