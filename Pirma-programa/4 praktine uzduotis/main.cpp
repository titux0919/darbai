#include <iostream>
#include <string>
#include <iomanip>
#include <fstream>
using namespace std;

struct menuItemType {
    string menuItem;
    double menuPrice;
};
int const masyvo_dydis = 8;
menuItemType menuList[8] = {
    {"Kiausiniene", 1.45},
    {"Kiaulienos sonine su keptu kiausiniu", 2.45},
    {"Keksiukas su vysnia", 0.99},
    {"Prancuziskas skrebutis", 1.99},
    {"Vaisiu salotos", 2.49},
    {"Pusryčiu dribsniai", 0.69},
    {"Kava", 0.50},
    {"Arbata", 0.75},
};
