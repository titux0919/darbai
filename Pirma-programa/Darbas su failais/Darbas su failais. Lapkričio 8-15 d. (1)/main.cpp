#include <iostream>
#include <fstream>
#include <vector>

/*int main() {
    // Atidarome failą input.txt skaitymui
    std::ifstream inputFile("input.txt");

    // Patikriname, ar failas buvo atidarytas sėkmingai
    if (!inputFile) {
        std::cerr << "Nepavyko atidaryti input.txt failo." << std::endl;
        return 1;
    }

    int number, sum = 0;

    // Nuskaitome visus skaičius iš input.txt
    while (inputFile >> number) {
        sum += number;  // Susumuojame skaičius
    }

    // Uždarykite input failą po nuskaitymo
    inputFile.close();

    // Atidarome output.txt failą rašymui
    std::ofstream outputFile("output.txt");

    // Patikriname, ar failas buvo atidarytas sėkmingai
    if (!outputFile) {
        std::cerr << "Nepavyko atidaryti output.txt failo." << std::endl;
        return 1;
    }

    // Įrašome sumą į output.txt failą
    outputFile << "Suma: " << sum << std::endl;

    // Uždarykite output failą po rašymo
    outputFile.close();

    std::cout << "Suma buvo irasyta i output.txt." << std::endl;

    return 0;
}*/

/*int main() {
std::ifstream inputFile("input.txt");  // Atidarome input.txt failą
std::ofstream outputFile("output.txt");  // Atidarome output.txt failą, kad įrašytume rezultatus

if (!inputFile) {
std::cerr << "Nepavyko atidaryti input.txt failo." << std::endl;
return 1;
}

if (!outputFile) {
std::cerr << "Nepavyko atidaryti output.txt failo." << std::endl;
return 1;
}

int number;
while (inputFile >> number) {  // Kol failo pabaigoje nepasiekiame
if (number % 2 == 0) {  // Jei skaičius lyginis
outputFile << number << std::endl;  // Įrašome į output.txt failą
}
}

inputFile.close();
outputFile.close();

std::cout << "Lyginiai skaičiai buvo išsaugoti output.txt faile." << std::endl;
return 0;
}*/