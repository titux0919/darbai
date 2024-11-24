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

    // Nuskaityti tekstą ir suskaičiuoti žodžius
    while (inputFile >> word) {
        // Pašalinti skyrybos ženklus ir paversti žodį į mažąsias raides
        std::string cleanedWord = "";
        for (char &ch : word) {
            if (std::isalnum(ch)) {  // Patikrinti, ar simbolis yra raidė arba skaitmuo
                cleanedWord += std::tolower(ch);  // Paversti į mažąsias raides
            }
        }

        if (!cleanedWord.empty()) {
            wordCount[cleanedWord]++;
        }
    }

    // Suskaičiuoti ir įrašyti rezultatus į output.txt
    for (const auto &entry : wordCount) {
        outputFile << entry.first << " pasikartoja " << entry.second << " kartus." << std::endl;
    }

    inputFile.close();
    outputFile.close();

    std::cout << "Rezultatai išsaugoti į output.txt." << std::endl;

    return 0;
}
