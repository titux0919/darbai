Atlikti pakeitimai:

1. Pridėta ketvirta rolė: registratorius / receptionist.
   - Prisijungus su role `registratorius` arba `receptionist`, atidaroma registratoriaus panelė.
   - Programa starto metu automatiškai užtikrina, kad DB būtų rolė `registratorius`.
   - Jeigu nėra vartotojo `registratorius`, sukuriamas testinis prisijungimas:
     vartotojas: registratorius
     el. paštas: registratorius@hotel.local
     slaptažodis: Registratorius123

2. Registratoriaus funkcionalumas:
   - Rezervacijų statusų peržiūra / patvirtinimas.
   - Laisvumo valdymas.
   - Rezervacijų CRUD.
   - Klientų CRUD.
   - Kambarių CRUD.
   - Mokėjimų CRUD.

3. Pilnas CRUD administratoriui:
   - Admin panelėje pridėtas modulis „Pilnas CRUD“.
   - CRUD palaikomos pagrindinės lentelės:
     role, app_user, customer, country, hotel, room_type, room, facility,
     hotel_facility, holiday, season, room_price, availability,
     competitor_price, booking, payment, price_prediction.
   - Kiekvienoje lentelėje galima:
     C - pridėti įrašą,
     R - peržiūrėti įrašus,
     U - pasirinkti įrašą ir koreguoti,
     D - ištrinti įrašą su patvirtinimu.

4. Esamas funkcionalumas nepanaikintas:
   - Palikti seni admin, buhalterio ir user ekranai.
   - Pridėtas papildomas CRUD modulis ir registratoriaus rolės maršrutizavimas.

Pastaba:
Šioje aplinkoje Maven negalėjo atsisiųsti priklausomybių, nes nėra interneto ryšio su repo.maven.apache.org, todėl galutinis kompiliavimas čia nebuvo įvykdytas. Šaltinio kodas ir projekto struktūra atnaujinti.
