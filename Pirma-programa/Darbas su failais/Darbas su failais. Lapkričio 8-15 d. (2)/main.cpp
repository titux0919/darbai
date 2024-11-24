#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <map>
#include <cctype>

int main() {
    std::ifstream inputFile("input.txt");
    std::ofstream outputFile("output.txt");

    if (!inputFile.is_open()) {
        std::cerr << "Nepavyko atidaryti input.txt failo." << std::endl;
        return 1;
    }

    std::string word;
    std::map<std::string, int> wordCount;


    while (inputFile >> word) {
        std::string cleanedWord = "";
        for (char &ch : word) {
            if (std::isalnum(ch)) {
                cleanedWord += std::tolower(ch);
            }
        }

        if (!cleanedWord.empty()) {
            wordCount[cleanedWord]++;
        }
    }

    for (const auto &entry : wordCount) {
        outputFile << entry.first << " pasikartoja " << entry.second << " kartus." << std::endl;
    }

    inputFile.close();
    outputFile.close();

    std::cout << "Rezultatai išsaugoti į output.txt." << std::endl;

    return 0;
}
