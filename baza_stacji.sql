------------------------------------------------------------------
--ROLE I UPRAWNIENIA
------------------------------------------------------------------

CREATE USER Pracownik IDENTIFIED BY hp;
CREATE USER Manager IDENTIFIED BY hm;
CREATE USER Kierownik IDENTIFIED BY hk;

CREATE ROLE PracownikStacji;
CREATE ROLE KierownikZmiany;
CREATE ROLE ManagerStacji;

GRANT CREATE SESSION TO ManagerStacji, KierownikZmiany, PracownikStacji;

--Pracownik
GRANT SELECT, INSERT, UPDATE, DELETE ON baza_stacji.Pracownik TO ManagerStacji;
GRANT SELECT ON baza_stacji.Pracownik to KierownikZmiany;

-- Produkt
GRANT SELECT, INSERT, UPDATE, DELETE ON baza_stacji.Produkt TO PracownikStacji, KierownikZmiany, ManagerStacji;

-- KategoriaProduktow
GRANT SELECT ON baza_stacji.KategoriaProduktow TO PracownikStacji, KierownikZmiany, ManagerStacji;
GRANT INSERT, DELETE ON baza_stacji.KategoriaProduktow TO ManagerStacji;

-- Raporty
GRANT SELECT, INSERT, UPDATE, DELETE ON baza_stacji.Raporty TO PracownikStacji, KierownikZmiany, ManagerStacji;

GRANT ManagerStacji to Manager;
GRANT PracownikStacji to Pracownik;
GRANT KierownikZmiany to Kierownik;


GRANT EXECUTE ON baza_stacji.WyszukajProduktPoNazwie TO PracownikStacji, KierownikZmiany, ManagerStacji;
GRANT EXECUTE ON baza_stacji.WyszukajProduktPoCenie TO PracownikStacji, KierownikZmiany, ManagerStacji;
GRANT EXECUTE ON baza_stacji.WyszukajProduktPoIlosci TO PracownikStacji, KierownikZmiany, ManagerStacji;
GRANT EXECUTE ON baza_stacji.AktualizujProdukt TO PracownikStacji, KierownikZmiany, ManagerStacji;
GRANT EXECUTE ON baza_stacji.DodajNowyProdukt TO PracownikStacji, KierownikZmiany, ManagerStacji;
GRANT EXECUTE ON baza_stacji.UsunProdukt TO PracownikStacji, KierownikZmiany, ManagerStacji;

GRANT EXECUTE ON baza_stacji.DodajNowegoPracownika TO ManagerStacji;
GRANT EXECUTE ON baza_stacji.AktualizujInformacjeOPracowniku TO ManagerStacji;
GRANT EXECUTE ON baza_stacji.UsunPracownika TO ManagerStacji;
GRANT EXECUTE ON baza_stacji.WyszukajPracownikaPoNazwisku TO ManagerStacji, KierownikZmiany;

GRANT EXECUTE ON baza_stacji.DodajNowaKategorieProduktu TO PracownikStacji, KierownikZmiany, ManagerStacji;
GRANT EXECUTE ON baza_stacji.UsunKategorieProduktu TO PracownikStacji, KierownikZmiany, ManagerStacji;
GRANT EXECUTE ON baza_stacji.WyszukajKategorieProduktuPoNazwie TO PracownikStacji, KierownikZmiany, ManagerStacji;

GRANT EXECUTE ON baza_stacji.DodajNowyRaport TO PracownikStacji, KierownikZmiany, ManagerStacji;
GRANT EXECUTE ON baza_stacji.UsunRaport TO PracownikStacji, KierownikZmiany, ManagerStacji;
GRANT EXECUTE ON baza_stacji.WyszukajRaportPoNazwie TO PracownikStacji, KierownikZmiany, ManagerStacji;
GRANT EXECUTE ON baza_stacji.WyszukajRaportPoAutorze TO PracownikStacji, KierownikZmiany, ManagerStacji;


------------------------------------------------------------------------
--TABELE
------------------------------------------------------------------------

CREATE TABLE KategoriaProduktow (
    ID INT PRIMARY KEY,
    Nazwa VARCHAR(50) UNIQUE
);

CREATE TABLE Pracownik (
    ID INT PRIMARY KEY,
    Imie VARCHAR(50),
    Nazwisko VARCHAR(50),
    Email VARCHAR(100) UNIQUE,
    Stanowisko VARCHAR(100),
    Wynagrodzenie NUMBER (10, 2) 
);

CREATE TABLE Produkt (
    ID INT PRIMARY KEY,
    Nazwa VARCHAR(100) ,
    Cena NUMBER(10, 2),
    Ilosc INT,
    ID_Kategorii INT,
    FOREIGN KEY (ID_Kategorii) REFERENCES KategoriaProduktow(ID)
);

CREATE TABLE Raporty (
    ID INT PRIMARY KEY,
    DataWykonania DATE,
    Nazwa VARCHAR(100) UNIQUE,
    ID_Autora INT,
    Tresc VARCHAR(2000),
    FOREIGN KEY (ID_Autora) REFERENCES Pracownik(ID)
);



---------------------------------------------------------------------------
--TRIGGERY
---------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER prevent_delete_category
BEFORE DELETE ON KategoriaProduktow
FOR EACH ROW
DECLARE
    v_product_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_product_count FROM Produkt WHERE ID_Kategorii = :OLD.ID;

    IF v_product_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Nie mo¿na usun¹æ kategorii z przypisanymi produktami.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER prevent_delete_author
BEFORE DELETE ON Pracownik
FOR EACH ROW
DECLARE
    v_report_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_report_count FROM Raporty WHERE ID_Autora = :OLD.ID;

    IF v_report_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Nie mo¿na usun¹æ pracownika, który jest autorem raportu.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER prevent_negative_quantity
BEFORE UPDATE ON Produkt
FOR EACH ROW
BEGIN
    IF :NEW.Ilosc < 0 THEN
        RAISE_APPLICATION_ERROR(-20004, 'Nie mo¿na ustawiæ ujemnej iloœci produktu.');
    END IF;
END;
/


CREATE OR REPLACE TRIGGER prevent_negative_price
BEFORE UPDATE ON Produkt
FOR EACH ROW
BEGIN
    IF :NEW.Cena<0 THEN
        RAISE_APPLICATION_ERROR(-20005, 'Nie mo¿na ustawiæ ujemnej ceny.');
    END IF;
END;
/


-----------------------------------------------------------------
--PROCEDURY
-----------------------------------------------------------------

--DLA TABELI PRODUKT
CREATE OR REPLACE PROCEDURE WyszukajProduktPoNazwie(
    p_Nazwa IN VARCHAR2,
    p_Cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_Cursor FOR
    SELECT * FROM Produkt WHERE Nazwa = p_Nazwa;
END WyszukajProduktPoNazwie;
/


CREATE OR REPLACE PROCEDURE WyszukajProduktPoCenie(
    p_MinCena VARCHAR2,
    p_MaxCena VARCHAR2,
    p_Cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_Cursor FOR
    SELECT * FROM Produkt WHERE Cena BETWEEN TO_NUMBER(p_MinCena) AND TO_NUMBER(p_MaxCena);
END WyszukajProduktPoCenie;
/


CREATE OR REPLACE PROCEDURE WyszukajProduktPoIlosci(
    p_MinIlosc INT,
    p_MaxIlosc INT,
    p_Cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_Cursor FOR
    SELECT * FROM Produkt WHERE Ilosc BETWEEN p_MinIlosc AND p_MaxIlosc;
END WyszukajProduktPoIlosci;
/

CREATE OR REPLACE PROCEDURE DodajNowyProdukt(
    p_Nazwa VARCHAR2,
    p_Cena VARCHAR2,
    p_Ilosc INT,
    p_ID_Kategorii INT
)
IS
v_NextID INT;
BEGIN
    SELECT MIN(ID) INTO v_NextID
    FROM (
        SELECT LEVEL AS ID
        FROM dual
        CONNECT BY LEVEL <= (SELECT MAX(ID) FROM Produkt) + 2
    ) ids
    WHERE NOT EXISTS (
        SELECT 1
        FROM Produkt
        WHERE ID = ids.ID
    );


    INSERT INTO Produkt (ID, Nazwa, Cena, Ilosc, ID_Kategorii)
    VALUES (v_NextID, p_Nazwa, TO_NUMBER(p_Cena), p_Ilosc, p_ID_Kategorii);

    COMMIT;
END DodajNowyProdukt;
/



CREATE OR REPLACE PROCEDURE AktualizujProdukt(
    p_ID INT,
    p_Nazwa VARCHAR2,
    p_Cena VARCHAR2,
    p_Ilosc INT,
    p_ID_Kategorii INT
)
IS
BEGIN
    UPDATE Produkt
    SET Nazwa = p_Nazwa,
        Cena = TO_NUMBER(p_Cena),
        Ilosc = p_Ilosc,
        ID_Kategorii = p_ID_Kategorii
    WHERE ID = p_ID;
END AktualizujProdukt;
/


CREATE OR REPLACE PROCEDURE UsunProdukt(p_ID INT)
IS
BEGIN
    DELETE FROM Produkt WHERE ID = p_ID;
    COMMIT;
END UsunProdukt;
/

--DLA TABELI PRACOWNIK
CREATE OR REPLACE PROCEDURE DodajNowegoPracownika(
    p_Imie VARCHAR2,
    p_Nazwisko VARCHAR2,
    p_Email VARCHAR2,
    p_Stanowisko VARCHAR2,
    p_Wynagrodzenie VARCHAR2
)
IS
    v_NextID INT;
BEGIN
    SELECT MIN(ID) INTO v_NextID
    FROM (
        SELECT LEVEL AS ID
        FROM dual
        CONNECT BY LEVEL <= (SELECT MAX(ID) FROM Pracownik) + 2
    ) ids
    WHERE NOT EXISTS (
        SELECT 1
        FROM Pracownik
        WHERE ID = ids.ID
    );

    -- Wstawianie danych do tabeli Pracownik z brakuj¹cym numerem ID
    INSERT INTO Pracownik (ID, Imie, Nazwisko, Email, Stanowisko, Wynagrodzenie)
    VALUES (v_NextID, p_Imie, p_Nazwisko, p_Email, p_Stanowisko, TO_NUMBER(p_Wynagrodzenie));
  

    COMMIT;
END DodajNowegoPracownika;
/


CREATE OR REPLACE PROCEDURE AktualizujInformacjeOPracowniku(
    p_ID INT,
    p_Imie VARCHAR2,
    p_Nazwisko VARCHAR2,
    p_Email VARCHAR2,
    p_Stanowisko VARCHAR2,
    p_Wynagrodzenie VARCHAR2
)
IS
BEGIN
    UPDATE Pracownik
    SET Imie = p_Imie,
        Nazwisko = p_Nazwisko,
        Email = p_Email,
        Stanowisko = p_Stanowisko,
        Wynagrodzenie = TO_NUMBER(p_Wynagrodzenie)
    WHERE ID = p_ID;
    COMMIT;
END AktualizujInformacjeOPracowniku;
/

CREATE OR REPLACE PROCEDURE UsunPracownika(p_ID INT)
IS
BEGIN
    DELETE FROM Pracownik WHERE ID = p_ID;
    COMMIT;
END UsunPracownika;
/
//wyszukiwanie pracownika po nazwisku
CREATE OR REPLACE PROCEDURE WyszukajPracownikaPoNazwisku(
    p_Nazwisko VARCHAR2,
    p_Cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_Cursor FOR
    SELECT * FROM Pracownik WHERE Nazwisko = p_Nazwisko;
END WyszukajPracownikaPoNazwisku;
/




--DLA TABELI KATEGORIA PRODUKTU
CREATE OR REPLACE PROCEDURE DodajNowaKategorieProduktu(
    p_Nazwa VARCHAR2
)
IS
    v_NextID INT;
BEGIN
    -- Szukanie pierwszego brakuj¹cego ID
    SELECT MIN(ID) INTO v_NextID
    FROM (
        SELECT LEVEL AS ID
        FROM dual
        CONNECT BY LEVEL <= (SELECT MAX(ID) FROM KategoriaProduktow) + 2
    ) ids
    WHERE NOT EXISTS (
        SELECT 1
        FROM KategoriaProduktow
        WHERE ID = ids.ID
    );

    -- Wstawianie danych do tabeli KategoriaProduktow
    INSERT INTO KategoriaProduktow (ID, Nazwa) VALUES (v_NextID, p_Nazwa);
    COMMIT;
END DodajNowaKategorieProduktu;
/





CREATE OR REPLACE PROCEDURE UsunKategorieProduktu(p_ID INT)
IS
BEGIN
    DELETE FROM KategoriaProduktow WHERE ID = p_ID;
    COMMIT;
END UsunKategorieProduktu;
/



CREATE OR REPLACE PROCEDURE WyszukajKategorieProduktuPoNazwie(
    p_Nazwa VARCHAR2,
    p_Cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_Cursor FOR
    SELECT * FROM KategoriaProduktow WHERE Nazwa = p_Nazwa;
END WyszukajKategorieProduktuPoNazwie;
/



--DLA TABELI RAPORT
CREATE OR REPLACE PROCEDURE UsunRaport(
    p_ID INT
)
IS
BEGIN
    DELETE FROM Raporty WHERE ID = p_ID;
    COMMIT;
END UsunRaport;
/


CREATE OR REPLACE PROCEDURE DodajNowyRaport(
    p_Nazwa VARCHAR2,
    p_ID_Autora INT,
    p_tresc VARCHAR2
)
IS
    v_NextID INT;
BEGIN
    -- Szukanie pierwszego brakuj¹cego ID
    SELECT MIN(ID) INTO v_NextID
    FROM (
        SELECT LEVEL AS ID
        FROM dual
        CONNECT BY LEVEL <= (SELECT MAX(ID) FROM Raporty) + 2
    ) ids
    WHERE NOT EXISTS (
        SELECT 1
        FROM Raporty
        WHERE ID = ids.ID
    );

    -- Wstawianie danych do tabeli Raporty
    INSERT INTO Raporty (ID, DataWykonania, Nazwa, ID_Autora, Tresc)
    VALUES (v_NextID, SYSDATE, p_Nazwa, p_ID_Autora, p_tresc);

    COMMIT;
END DodajNowyRaport;
/


CREATE OR REPLACE PROCEDURE WyszukajRaportPoNazwie(
    p_Nazwa VARCHAR2,
    p_Cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_Cursor FOR
    SELECT * FROM Raporty WHERE Nazwa = p_Nazwa;
END WyszukajRaportPoNazwie;
/

CREATE OR REPLACE PROCEDURE WyszukajRaportPoAutorze(   
    p_ID_Autora INT,
    p_Cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_Cursor FOR
    SELECT * FROM Raporty WHERE ID_Autora=p_ID_Autora;
END WyszukajRaportPoAutorze;
/


--------------------------------------------------------------------
--WYPE£NIANIE LOSOWYMI DANYMI
--------------------------------------------------------------------

BEGIN
    DodajNowaKategorieProduktu('Elektronika');
    DodajNowaKategorieProduktu('Odzie¿');
    DodajNowaKategorieProduktu('Dom i Ogród');
    DodajNowaKategorieProduktu('Ksi¹¿ki');
    DodajNowaKategorieProduktu('Sport i Fitness');
    DodajNowaKategorieProduktu('Motoryzacja');
    DodajNowaKategorieProduktu('Zdrowie i Uroda');
    DodajNowaKategorieProduktu('Dla Dzieci');
    DodajNowaKategorieProduktu('Artyku³y Spo¿ywcze');
END;
/


BEGIN
    DodajNowegoPracownika('Adam', 'Nowak', 'adam.nowak@example.com', 'Manager', '5500,00');
    

    
    DodajNowegoPracownika('Katarzyna', 'Kowalska', 'katarzyna.kowalska@example.com',  'Kierownik', '5000,00');
    DodajNowegoPracownika('Marcin', 'Wiœniewski', 'marcin.wisniewski@example.com',  'Kierownik', '5000,00');
    DodajNowegoPracownika('Agnieszka', 'Lewandowska', 'agnieszka.lewandowska@example.com', 'Kierownik', '5000,00');
    
   
    DodajNowegoPracownika('Jan', 'Nowak', 'jan.nowak@example.com',  'Pracownik', '4000,00');
    DodajNowegoPracownika('Anna', 'Kowalska', 'anna.kowalska@example.com',  'Pracownik', '4000,00');
    DodajNowegoPracownika('Piotr', 'D¹browski', 'piotr.dabrowski@example.com', 'Pracownik', '4000,00');
    DodajNowegoPracownika('Marta', 'Lewandowska', 'marta.lewandowska@example.com', 'Pracownik', '4000,66');
    DodajNowegoPracownika('Tomasz', 'Kaczmarek', 'tomasz.kaczmarek@example.com',  'Pracownik', '4000,00');
    DodajNowegoPracownika('Karolina', 'Szymañska', 'karolina.szymanska@example.com',  'Pracownik', '4000,00');
    DodajNowegoPracownika('£ukasz', 'Wojciechowski', 'lukasz.wojciechowski@example.com',  'Pracownik', '4000,00');
    DodajNowegoPracownika('Magdalena', 'Kowalczyk', 'magdalena.kowalczyk@example.com',  'Pracownik', '4000,99');
    DodajNowegoPracownika('Kamil', 'Zaj¹c', 'kamil.zajac@example.com',  'Pracownik', '4000,00');
    DodajNowegoPracownika('Monika', 'Witkowska', 'monika.witkowska@example.com',  'Pracownik', '4000,00');
    DodajNowegoPracownika('Bartosz', 'Sikorski', 'bartosz.sikorski@example.com',  'Pracownik', '4000,00');
    DodajNowegoPracownika('Natalia', 'Mazur', 'natalia.mazur@example.com', 'Pracownik', '4000,00');
    
    COMMIT;

END;
/

execute wyszukajproduktponazwie('Telewizor');

BEGIN
    DodajNowyProdukt('Telewizor', '3000,00', 8, 1);
    DodajNowyProdukt('Laptop', '5000,00', 12, 1);
    DodajNowyProdukt('Smartfon', '1500,00', 20, 1);
    DodajNowyProdukt('Kurtka zimowa', '200,00', 15, 2);
    DodajNowyProdukt('Spodnie jeansowe', '120,00', 25, 2);
    DodajNowyProdukt('Koszula', '50,00', 30, 2);
    DodajNowyProdukt('Meble ogrodowe', '800,00', 10, 3);
    DodajNowyProdukt('Doniczka', '20,00', 50, 3);
    DodajNowyProdukt('Ksi¹¿ka "W³adca Pierœcieni"', '40,00', 30, 4);
    DodajNowyProdukt('Ksi¹¿ka "Z³odziejka ksi¹¿ek"', '35,00', 25, 4);
    DodajNowyProdukt('Pi³ka do koszykówki', '30,00', 40, 5);
    DodajNowyProdukt('Rower górski', '1000,00', 10, 5);
    DodajNowyProdukt('Opony letnie', '200,00', 15, 6);
    DodajNowyProdukt('Kosmetyki do pielêgnacji twarzy', '50,00', 30, 7);
    DodajNowyProdukt('Zestaw do manicure', '15,00', 50, 7);
    DodajNowyProdukt('Zestaw klocków LEGO', '80,00', 20, 8);
    DodajNowyProdukt('Pluszowa zabawka', '10,00', 100, 8);
    DodajNowyProdukt('Mleko', '2,50', 50, 9);
    DodajNowyProdukt('Chleb pszenny', '3,00', 30, 9);
    DodajNowyProdukt('Telewizor', '3000,00', 8, 1);
    DodajNowyProdukt('Laptop', '5000,00', 12, 1);
    DodajNowyProdukt('Smartfon', '1500,00', 20, 1);
    DodajNowyProdukt('Kurtka zimowa', '200,00', 15, 2);
    DodajNowyProdukt('Spodnie jeansowe', '120,00', 25, 2);
    DodajNowyProdukt('Koszula', '50,00', 30, 2);
    DodajNowyProdukt('Meble ogrodowe', '800,00', 10, 3);
    DodajNowyProdukt('Doniczka', '20,00', 50, 3);
    DodajNowyProdukt('Ksi¹¿ka "W³adca Pierœcieni"', '40,00', 30, 4);
    DodajNowyProdukt('Ksi¹¿ka "Z³odziejka ksi¹¿ek"', '35,00', 25, 4);
    DodajNowyProdukt('Pi³ka do koszykówki', '30,00', 40, 5);
    DodajNowyProdukt('Rower górski', '1000,00', 10, 5);
    DodajNowyProdukt('Opony letnie', '200,00', 15, 6);
    DodajNowyProdukt('Kosmetyki do pielêgnacji twarzy', '50,00', 30, 7);
    DodajNowyProdukt('Zestaw do manicure', '15,00', 50, 7);
    DodajNowyProdukt('Zestaw klocków LEGO', '80,00', 20, 8);
    DodajNowyProdukt('Pluszowa zabawka', '10,00', 100, 8);
    DodajNowyProdukt('Mleko', '2,50', 50, 9);
    DodajNowyProdukt('Chleb pszenny', '3,00', 30, 9);
    DodajNowyProdukt('¯elazko', '60,00', 12, 1);
    DodajNowyProdukt('Odkurzacz', '120,00', 8, 1);
    DodajNowyProdukt('Blu-ray Player', '150,00', 15, 1);
    DodajNowyProdukt('Sukienka letnia', '80,00', 20, 2);
    DodajNowyProdukt('Buty sportowe', '70,00', 25, 2);
    DodajNowyProdukt('Okulary przeciws³oneczne', '30,00', 30, 2);
    DodajNowyProdukt('Namiot turystyczny', '150,00', 5, 3);
    DodajNowyProdukt('Grill ogrodowy', '250,00', 8, 3);
    DodajNowyProdukt('Zestaw farb akrylowych', '25,00', 15, 4);
    DodajNowyProdukt('Pêdzle artystyczne', '15,00', 25, 4);
 

    COMMIT;

END;
/

exec dodajnowyprodukt('test', '333', 8, 3);

BEGIN
    DodajNowyRaport('2023-01-15', 'Raport 1', 1, 'Tresc raportu'); 
    DodajNowyRaport('2023-02-10', 'Raport 2', 2, 'Tresc raportu'); 
    DodajNowyRaport('2023-03-05', 'Raport 3', 3, 'Tresc raportu'); 
    DodajNowyRaport('2023-04-20', 'Raport 4', 4, 'Tresc raportu'); 
    DodajNowyRaport('2023-05-12', 'Raport 5', 5, 'Tresc raportu'); 
    DodajNowyRaport('2023-06-25', 'Raport 6', 6, 'Tresc raportu'); 
    DodajNowyRaport('2023-07-08', 'Raport 7', 7, 'Tresc raportu'); 
    DodajNowyRaport('2023-08-19', 'Raport 8', 8, 'Tresc raportu'); 
    DodajNowyRaport('2023-09-03', 'Raport 9', 9, 'Tresc raportu'); 
    DodajNowyRaport('2023-10-22', 'Raport 10', 10, 'Tresc raportu');
     COMMIT;

END;
/




select * from produkt;
select * from pracownik;
select * from kategoriaproduktow;
select * from raporty;


----------------------------------------------------------------------
--testy
----------------------------------------------------------------------

--integralnoœc semantyczna

--1)
UPDATE Produkt
SET Cena = -100.50
WHERE ID = 2;
--trigger blokuje ustawienie ujemnej ceny, tak jak powinno byæ

--2)
UPDATE Pracownik
SET Wynagrodzenie = 'Nieliczba'
WHERE ID = 1;
--wynagrodzenie ustawione jest jako number wiêc napis powoduje b³¹d i uniemo¿liwia jego wstawienie

--integralnoœæ encji

--1)
INSERT INTO Produkt (Nazwa, Cena, Ilosc, ID_Kategorii)
VALUES ('Nowy Produkt', 50.00, 10, 1);
--nie mo¿na wstawiæ produktu bez klucza g³ównego
--uwaga stosuj¹c procedurê jak wyzej przy wypelnianiu losowymi danymi na pierwszy rzut oka mo¿na dodaæ produkt bez klucza g³ównego,
--ale procedura go samoczynnie dodaje wiêc w rzeczywistoœci klucz g³ówny i tak musi byæ


--2)
INSERT INTO Pracownik (Imie, Nazwisko, Email, Haslo, Stanowisko, Wynagrodzenie)
VALUES ('Nowy', 'Pracownik', 'nowy.pracownik@example.com', 'haslo123', 'Pracownik', 4000.00);
--Jak wy¿ej ale dla tabeli pracownik


--integralnoœæ referencji

--1)
UPDATE Produkt
SET ID_Kategorii = 100
WHERE ID = 1;
--próba zmiany kategorii na nieistniej¹ca

--2)
UPDATE Raporty
SET ID_Autora = 100
WHERE ID = 5;
--jw dla raportów








