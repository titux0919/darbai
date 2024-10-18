#include <iostream>
using namespace std;

int main() {
    int numbers[5];
    int sum = 0;
    int min, max;

    // Vartotojo įvedimas
    cout << "Iveskite 5 sveikuosius skaiCius:\n";
    for(int i = 0; i < 5; i++) {
        cin >> numbers[i];
        sum += numbers[i];

        if(i == 0) {
            min = max = numbers[i];
        } else {
            if(numbers[i] < min) {
                min = numbers[i];
            }
            if(numbers[i] > max) {
                max = numbers[i];
            }
        }
    }

    cout << "Suma: " << sum << endl;
    cout << "Didžiausias elementas: " << max << endl;
    cout << "Mažiausias elementas: " << min << endl;

    return 0;
}

