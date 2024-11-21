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
    cout << "Pasirinkite sifravimo buda:\n1. Abeceles sifravimas\n2. ASCII sifravimas\nJusu pasirinkimas: ";
    cin >> choice;
    cin.ignore();

    cout << "Iveskite teksta: ";
    getline(cin, text);
    cout << "Iveskite slaptaji rakta: ";
    getline(cin, key);

    if (choice == 1) {
        string encrypted = encryptAlphabet(text, key);
        cout << "Uzsifruotas tekstas (abecele): " << encrypted << endl;
        cout << "Atsifruotas tekstas: " << decryptAlphabet(encrypted, key) << endl;
    } else if (choice == 2) {
        string encrypted = encryptASCII(text, key);
        cout << "Uzsifruotas tekstas (ASCII): " << encrypted << endl;
        cout << "Atsifruotas tekstas: " << decryptASCII(encrypted, key) << endl;
    } else {
        cout << "Neteisingas pasirinkimas!" << endl;
    }
}

int main() {
    menu();
    return 0;
}

