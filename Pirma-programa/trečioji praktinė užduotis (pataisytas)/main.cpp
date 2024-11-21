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


