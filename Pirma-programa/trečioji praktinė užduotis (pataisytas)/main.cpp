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
        cin.ignore();

        if (choice == 0) {
            cout << "Programos pabaiga.\n";
            break;
        }

        cout << "Įveskite tekstą: ";
        getline(cin, text);
        cout << "Įveskite slaptąjį raktą: ";
        getline(cin, key);

        cout << "Pasirinkite veiksmą:\n1. Užšifruoti\n2. Atšifruoti\nJūsų pasirinkimas: ";
        cin >> action;
        cin.ignore();