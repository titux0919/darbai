#include <iostream>
#include <fstream>
#include <vector>

/*int main() {
    std::ifstream inputFile("input.txt");

    if (!inputFile) {
        std::cerr << "Nepavyko atidaryti input.txt failo." << std::endl;
        return 1;
    }

    int number, sum = 0;

    while (inputFile >> number) {
        sum += number;  // Susumuojame skaičius
    }

    inputFile.close();

    std::ofstream outputFile("output.txt");

    if (!outputFile) {
        std::cerr << "Nepavyko atidaryti output.txt failo." << std::endl;
        return 1;
    }

    outputFile << "Suma: " << sum << std::endl;

    outputFile.close();

    std::cout << "Suma buvo irasyta i output.txt." << std::endl;

    return 0;
}*/

/*int main() {
std::ifstream inputFile("input.txt");
std::ofstream outputFile("output.txt");

if (!inputFile) {
std::cerr << "Nepavyko atidaryti input.txt failo." << std::endl;
return 1;
}

if (!outputFile) {
std::cerr << "Nepavyko atidaryti output.txt failo." << std::endl;
return 1;
}

int number;
while (inputFile >> number) {
if (number % 2 == 0) {
outputFile << number << std::endl;
}
}

inputFile.close();
outputFile.close();

std::cout << "Lyginiai skaičiai buvo išsaugoti output.txt faile." << std::endl;
return 0;
}*/