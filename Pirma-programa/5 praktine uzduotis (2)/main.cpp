#include <iostream>
#include <string>

using namespace std;

struct Contact { //aprašo kiekvieną kontaktą
    int id;
    string firstName;
    string lastName;
    string phoneNumber;
    string email;
};

void addContact(Contact*& contacts, int& size, int& nextId); // naudojamas kaip funkcijų grąžinimo tipas, nes funkcijos skirtos atlikti tam tikrus veiksmus
void viewContacts(Contact* contacts, int size);
void updateContact(Contact* contacts, int size);
void deleteContact(Contact*& contacts, int& size);

int main() {
    system("chcp 1257 > null");

    Contact* contacts = nullptr; // nurodo masyvą, kuris saugos kontaktus. Dėl to,nes jis iš pradžių tuščias, nustatyta nullptr
    int size = 0; // dabartinis kontaktų sk. masyve
    int nextId = 1; // Unikalus ID kiekvienam kontaktui
    int choice; // atvaizduoja vartotojui meniu su pasirinkimais

    do {
        cout << "\n===== Kontaktu valdymo sistema =====\n";
        cout << "1. Prideti kontakta\n";
        cout << "2. Perziureti kontaktus\n";
        cout << "3. Redaguoti kontakta\n";
        cout << "4. Pasalinti kontakta\n";
        cout << "0. Iseiti\n";
        cout << "Pasirinkite veiksma: ";
        cin >> choice;

        cin.ignore();

        switch (choice) { // patikrina vartotojo pasirinkimą ir pagal jo reikšmę atlieką atitinkamus veiksmus
            case 1:
                addContact(contacts, size, nextId);
                break;
            case 2:
                viewContacts(contacts, size);
                break;
            case 3:
                updateContact(contacts, size);
                break;
            case 4:
                deleteContact(contacts, size);
                break;
            case 0:
                cout << "Iseinama is programos...\n";
                break;
            default:
                cout << "Neteisingas pasirinkimas. Bandykite dar karta.\n";
        }
    } while (choice != 0); //kartosis tol, kol pasirinkimas nėra 0

    // Atlaisvinimui atminties
    delete[] contacts;

    return 0;
}

void addContact(Contact*& contacts, int& size, int& nextId) { // atsakinga už naujo kontakto pridėjimą prie dinaminio kontaktų masyvo
    Contact* newContact = new Contact; //sukuriamas naujas kontaktas dinaminiu būdu
    newContact->id = nextId++;
// įvedama kontaktinė informacija
    cout << "Iveskite varda: ";
    getline(cin, newContact->firstName);
    cout << "Iveskite pavarde: ";
    getline(cin, newContact->lastName);
    cout << "Iveskite telefono numeri: ";
    getline(cin, newContact->phoneNumber);
    cout << "Iveskite el. pasta: ";
    getline(cin, newContact->email);

    Contact* newContacts = new Contact[size + 1]; // sukuriamas naujas dinaminis masyvas, kuris yra vienu elementu didesnis už esamą

    for (int i = 0; i < size; i++) { //perkopijuomi seni kontaktai
        newContacts[i] = contacts[i];
    }


    // Prideti nauja kontakta
    newContacts[size] = *newContact;
    // senas kontaktų masyvas ištrinamas, naujas masyvas tampa pagrindiniu tuo metu
    delete[] contacts;
    delete newContact;

    contacts = newContacts;
    size++;

    cout << "Kontaktas pridetas sekmingai!\n";
}

void viewContacts(Contact* contacts, int size) { // patikrina, ar sąrašas tuščias
    if (size == 0) {
        cout << "Kontaktu sarasas tuscias.\n";
        return;
    }

    cout << "\n===== Kontaktu sarasas =====\n"; // spausdina kontaktų sąrašą
    for (int i = 0; i < size; i++) {
        cout << "ID: " << contacts[i].id << "\n";
        cout << "Vardas: " << contacts[i].firstName << "\n";
        cout << "Pavarde: " << contacts[i].lastName << "\n";
        cout << "Telefonas: " << contacts[i].phoneNumber << "\n";
        cout << "El. pastas: " << contacts[i].email << "\n";
        cout << "-----------------------------\n"; // įterpiama linija, kad kie būtų atskirti
    }
}

void updateContact(Contact* contacts, int size) { // Contact* rodyklė į masyvą, kuriame saugomi kontaktai. Int size: Skaičius, nurodantis,kiek kontaktų yra masyve.
    int id; //vartotojas įveda kontakto ID, kuri nori keisti
    cout << "Iveskite kontakta ID redagavimui: ";
    cin >> id;
    cin.ignore(); // išvalomas likęs simbolių srautas, kad getline veiktų tinkamai

    for (int i = 0; i < size; i++) { // ciklas pereina per visus kontaktus, kad rastų kontaktą su nurodytu ID.
        if (contacts[i].id == id) {
            cout << "Redaguojamas kontaktas: \n";
            cout << "Dabartinis vardas: " << contacts[i].firstName << "\n";
            cout << "Iveskite nauja varda (arba palikite tuscia): ";
            string input;
            getline(cin, input);
            if (!input.empty()) contacts[i].firstName = input; // tikrina ar vartotojas įvedė naują vardą, jei taip jį atnaujina

            cout << "Dabartine pavarde: " << contacts[i].lastName << "\n";
            cout << "Iveskite nauja pavarde (arba palikite tuscia): ";
            getline(cin, input);
            if (!input.empty()) contacts[i].lastName = input;

            cout << "Dabartinis telefonas: " << contacts[i].phoneNumber << "\n";
            cout << "Iveskite nauja telefona (arba palikite tuscia): ";
            getline(cin, input);
            if (!input.empty()) contacts[i].phoneNumber = input;

            cout << "Dabartinis el. pastas: " << contacts[i].email << "\n";
            cout << "Iveskite nauja el. pasta (arba palikite tuscia): ";
            getline(cin, input);
            if (!input.empty()) contacts[i].email = input;

            cout << "Kontaktas atnaujintas sekmingai!\n";
            return;
        }
    }

    cout << "Kontaktas su ID " << id << " nerastas.\n";
}

void deleteContact(Contact*& contacts, int& size) { // skirta pašalinti kontaktą iš sąrašo pagal jo unikalų ID.
    if (size == 0) {
        cout << "Kontaktu sarasas tuscias.\n";
        return;
    }

    int id;
    cout << "Iveskite kontakta ID pasalinimui: ";
    cin >> id;

    int indexToDelete = -1; // saugo paieškos rezultatus, jei taip jis įrašomas į indexToDelete ir jis yra nutraukiamas
    for (int i = 0; i < size; i++) {
        if (contacts[i].id == id) {
            indexToDelete = i;
            break;
        }
    }

    if (indexToDelete == -1) { // jei nerandamas lieka su reikšme -1, išvedama klaidos žinutė ir funkcija baigiama
        cout << "Kontaktas su ID " << id << " nerastas.\n";
        return;
    }

    Contact* newContacts = new Contact[size - 1]; // sukuriama nauja masyvo vieta, kurioje saugomi bus kontaktai, bet jau be pašalinto kontakto.

    for (int i = 0, j = 0; i < size; i++) { // cikle visi kontaktai iš seno masyvo, perkeliami į naują, išskyrus tas kuris buvo pasirinktas pašalinti
        if (i != indexToDelete) {
            newContacts[j++] = contacts[i];
        }
    }

    delete[] contacts; // sename masyve esanti atmintis atlaisvinama, kad būtų išvengta atminties nuotėkio
    contacts = newContacts; // nukreipiamas į naują masyvą, kuris neturi pašalinto kontakto
    size--; //sumažinimas vienu, kad atspindėtų sumažėjusį kontaktų sk.

    cout << "Kontaktas pasalintas sekmingai!\n";
}
