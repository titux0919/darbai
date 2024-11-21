#include <iostream>
#include <string>
#include <stdexcept>
#include <cctype>

using namespace std;

string encryptAlphabet(const string& text, const string& key) {
    string result;
    int keyLength = key.size();
    if (keyLength == 0) {
        throw invalid_argument("Raktas negali būti tuščias!");
    }

    for (int i = 0; i < text.size(); i++) {
        char c = text[i];
        char k = toupper(key[i % keyLength]);
        if (isalpha(c)) {
            bool isLower = islower(c);
            char base = isLower ? 'a' : 'A';
            result += ((c - base + (k - 'A')) % 26) + base;
        } else {
            result += c;
        }
    }
    return result;
}

string decryptAlphabet(const string& text, const string& key) {
    string result;
    int keyLength = key.size();
    if (keyLength == 0) {
        throw invalid_argument("Raktas negali būti tuščias!");
    }

    for (int i = 0; i < text.size(); i++) {
        char c = text[i];
        char k = toupper(key[i % keyLength]);
        if (isalpha(c)) {
            bool isLower = islower(c);
            char base = isLower ? 'a' : 'A';
            result += ((c - base - (k - 'A') + 26) % 26) + base;
        } else {
            result += c;
        }
    }
    return result;
}

string encryptASCII(const string& text, const string& key) {
    string result;
    int keyLength = key.size();
    if (keyLength == 0) {
        throw invalid_argument("Raktas negali būti tuščias!");
    }

    for (int i = 0; i < text.size(); i++) {
        char c = text[i];
        char k = key[i % keyLength];
        result += (c + k) % 256;
    }
    return result;
}

string decryptASCII(const string& text, const string& key) {
    string result;
    int keyLength = key.size();
    if (keyLength == 0) {
        throw invalid_argument("Raktas negali būti tuščias!");
    }

    for (int i = 0; i < text.size(); i++) {
        char c = text[i];
        char k = key[i % keyLength];
        result += (c - k + 256) % 256;
    }
    return result;
}

void menu() {
    int choice, action;
    string text, key;

    while (true) {
        cout << "Pasirinkite šifravimo būdą:\n1. Abėcėlės šifravimas\n2. ASCII šifravimas\n0. Baigti\nJūsų pasirinkimas: ";
        cin >> choice;
        cin.ignore();  // Ignoruojame likusius simbolius po pasirinkimo

        if (choice == 0) {
            cout << "Programos pabaiga.\n";
            break; // Baigia ciklą ir programą
        }

        cout << "Įveskite tekstą: ";
        getline(cin, text);
        cout << "Įveskite slaptąjį raktą: ";
        getline(cin, key);

        cout << "Pasirinkite veiksmą:\n1. Užšifruoti\n2. Atšifruoti\nJūsų pasirinkimas: ";
        cin >> action;
        cin.ignore();  // Ignoruojame likusius simbolius po pasirinkimo

        try {
            if (choice == 1) { // Abėcėlės šifravimas
                if (action == 1) {
                    string encrypted = encryptAlphabet(text, key);
                    cout << "Užšifruotas tekstas (abėcėlė): " << encrypted << endl;
                } else if (action == 2) {
                    string decrypted = decryptAlphabet(text, key);
                    cout << "Atšifruotas tekstas: " << decrypted << endl;
                } else {
                    cout << "Neteisingas pasirinkimas!\n";
                }
            } else if (choice == 2) { // ASCII šifravimas
                if (action == 1) {
                    string encrypted = encryptASCII(text, key);
                    cout << "Užšifruotas tekstas (ASCII): ";
                    for (unsigned char c : encrypted) {
                        cout << (int)c << " ";
                    }
                    cout << endl;
                } else if (action == 2) {
                    string decrypted = decryptASCII(text, key);
                    cout << "Atšifruotas tekstas: " << decrypted << endl;
                } else {
                    cout << "Neteisingas pasirinkimas!\n";
                }
            } else {
                cout << "Neteisingas pasirinkimas!\n";
            }
        } catch (const exception& e) {
            cout << "Klaida: " << e.what() << endl;
        }
    }
}

int main() {
    menu();
    return 0;
}
