#include <iostream>
#include <string>
#include <cctype>

using namespace std;


string encryptAlphabet(const string& text, const string& key) {
    string result;
    int keyLength = key.size();
    for (int i = 0; i < text.size(); i++) {
        char c = toupper(text[i]);
        char k = toupper(key[i % keyLength]);
        if (isalpha(c)) {
            result += ((c - 'A' + (k - 'A')) % 26) + 'A';
        } else {
            result += c;
        }
    }
    return result;
}


string decryptAlphabet(const string& text, const string& key) {
    string result;
    int keyLength = key.size();
    for (int i = 0; i < text.size(); i++) {
        char c = toupper(text[i]);
        char k = toupper(key[i % keyLength]);
        if (isalpha(c)) {
            result += ((c - 'A' - (k - 'A') + 26) % 26) + 'A';
        } else {
            result += c;
        }
    }
    return result;
}


string encryptASCII(const string& text, const string& key) {
    string result;
    int keyLength = key.size();
    for (int i = 0; i < text.size(); i++) {
        result += text[i] + key[i % keyLength];
    }
    return result;
}


string decryptASCII(const string& text, const string& key) {
    string result;
    int keyLength = key.size();
    for (int i = 0; i < text.size(); i++) {
        result += text[i] - key[i % keyLength];
    }
    return result;
}


void menu() {
    int choice;
    string text, key;
    cout << "Pasirinkite šifravimo būdą:\n1. Abėcėlės šifravimas\n2. ASCII šifravimas\nJūsų pasirinkimas: ";
    cin >> choice;
    cin.ignore();

    cout << "Įveskite tekstą: ";
    getline(cin, text);
    cout << "Įveskite slaptąjį raktą: ";
    getline(cin, key);

    if (choice == 1) {
        string encrypted = encryptAlphabet(text, key);
        cout << "Užšifruotas tekstas (abėcėlė): " << encrypted << endl;
        cout << "Atšifruotas tekstas: " << decryptAlphabet(encrypted, key) << endl;
    } else if (choice == 2) {
        string encrypted = encryptASCII(text, key);
        cout << "Užšifruotas tekstas (ASCII): " << encrypted << endl;
        cout << "Atšifruotas tekstas: " << decryptASCII(encrypted, key) << endl;
    } else {
        cout << "Neteisingas pasirinkimas!" << endl;
    }
}

int main() {
    menu();
    return 0;
}

