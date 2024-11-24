#include <iostream>
#include <string>
#include <vector>

using namespace std;

string encryptWithAlphabet(const string& text, const string& key) {
    string alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    string result;
    int keyIndex = 0;
    for (char c : text) {
        if (isalpha(c)) {
            bool isLower = islower(c);
            char base = isLower ? 'a' : 'A';
            int textIndex = c - base;
            int keyChar = tolower(key[keyIndex % key.size()]) - 'a';
            result += alphabet[(textIndex + keyChar) % 26];
            keyIndex++;
        } else {
            result += c;
        }
    }
    return result;
}

string decryptWithAlphabet(const string& text, const string& key) {
    string alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    string result;
    int keyIndex = 0;
    for (char c : text) {
        if (isalpha(c)) {
            bool isLower = islower(c);
            char base = isLower ? 'a' : 'A';
            int textIndex = c - base;
            int keyChar = tolower(key[keyIndex % key.size()]) - 'a';
            result += alphabet[(textIndex - keyChar + 26) % 26];
            keyIndex++;
        } else {
            result += c;
        }
    }
    return result;
}

string encryptWithASCII(const string& text, const string& key) {
    string result;
    int keyIndex = 0;
    for (char c : text) {
        result += char((c + key[keyIndex % key.size()]) % 256);
        keyIndex++;
    }
    return result;
}

string decryptWithASCII(const string& text, const string& key) {
    string result;
    int keyIndex = 0;
    for (char c : text) {
        result += char((c - key[keyIndex % key.size()] + 256) % 256);
        keyIndex++;
    }
    return result;
}

int main() {
    int choice;
    string text, key;

    do {
        cout << "\nPasirinkite:\n";
        cout << "1. Sifruoti/Desifruoti naudojant abecele\n";
        cout << "2. Sifruoti/Desifruoti naudojant ASCII koduote\n";
        cout << "3. Iseiti\n";
        cout << "Jusu pasirinkimas: ";
        cin >> choice;
        cin.ignore();

        if (choice == 1 || choice == 2) {
            cout << "Iveskite teksta: ";
            getline(cin, text);
            cout << "Iveskite slapta rakta: ";
            getline(cin, key);

            if (choice == 1) {
                string encryptedText = encryptWithAlphabet(text, key);
                cout << "Uzsifruotas tekstas: " << encryptedText << endl;
                cout << "Desifruotas tekstas: " << decryptWithAlphabet(encryptedText, key) << endl;
            } else {
                string encryptedText = encryptWithASCII(text, key);
                cout << "Uzsifruotas tekstas: " << encryptedText << endl;
                cout << "Desifruotas tekstas: " << decryptWithASCII(encryptedText, key) << endl;
            }
        } else if (choice == 3) {
            cout << "Programa baigta.\n";
            break;
        } else {
            cout << "Neteisingas pasirinkimas. Bandykite dar karta.\n";
        }
    } while (choice != 3);

    return 0;
}
