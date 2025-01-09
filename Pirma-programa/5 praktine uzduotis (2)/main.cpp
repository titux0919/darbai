#include <iostream>
#include <string>

using namespace std;

struct Contact {
    int id;
    string firstName;
    string lastName;
    string phoneNumber;
    string email;
};

void addContact(Contact*& contacts, int& size, int& nextId);
void viewContacts(Contact* contacts, int size);
void updateContact(Contact* contacts, int size);
void deleteContact(Contact*& contacts, int& size);

int main() {
    system("chcp 1257 > null");

    Contact* contacts = nullptr;
    int size = 0;
    int nextId = 1; // Unikalus ID kiekvienam kontaktui
    int choice;

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

        switch (choice) {
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
    } while (choice != 0);

    // Atlaisvinimui atminties
    delete[] contacts;

    return 0;
}

void addContact(Contact*& contacts, int& size, int& nextId) {
    Contact* newContact = new Contact;
    newContact->id = nextId++;

    cout << "Iveskite varda: ";
    getline(cin, newContact->firstName);
    cout << "Iveskite pavarde: ";
    getline(cin, newContact->lastName);
    cout << "Iveskite telefono numeri: ";
    getline(cin, newContact->phoneNumber);
    cout << "Iveskite el. pasta: ";
    getline(cin, newContact->email);

    Contact* newContacts = new Contact[size + 1];

    for (int i = 0; i < size; i++) {
        newContacts[i] = contacts[i];
    }


    // Prideti nauja kontakta
    newContacts[size] = *newContact;

    delete[] contacts;
    delete newContact;

    contacts = newContacts;
    size++;

    cout << "Kontaktas pridetas sekmingai!\n";
}

void viewContacts(Contact* contacts, int size) {
    if (size == 0) {
        cout << "Kontaktu sarasas tuscias.\n";
        return;
    }

    cout << "\n===== Kontaktu saraaas =====\n";
    for (int i = 0; i < size; i++) {
        cout << "ID: " << contacts[i].id << "\n";
        cout << "Vardas: " << contacts[i].firstName << "\n";
        cout << "Pavarde: " << contacts[i].lastName << "\n";
        cout << "Telefonas: " << contacts[i].phoneNumber << "\n";
        cout << "El. pastas: " << contacts[i].email << "\n";
        cout << "-----------------------------\n";
    }
}


