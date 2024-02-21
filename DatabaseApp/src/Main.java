import javax.lang.model.util.Elements;
import javax.sound.midi.Soundbank;
import java.sql.*;
import java.util.Scanner;


public class Main {

    public static final String url = "jdbc:oracle:thin:@localhost:1521/XE";

    private static Connection connection = null;
    private static Pracownik pracownik = null;
    private static Produkty produkty = null;
    private static Raporty raporty = null;
    public static Kategoria kategoria = null;


    public static void main(String[] args) {
        //GUI gui = new GUI();
        //Original();
        ShowLoginWindow();
    }

    public static void ShowLoginWindow()
    {
        new LoginWindow();
    }

    public static boolean TryLogin(String login, String password)
    {
        try
        {
            connection = DriverManager.getConnection(url, login, password);
            kategoria = new Kategoria(connection);
            pracownik = new Pracownik(connection);
            produkty = new Produkty(connection);
            raporty = new Raporty(connection);
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public static void ShowOptionWindow()
    {
        new OptionsWindow();
    }

    public static void ShowProductsWindow()
    {
        new OpcjeProduktowWindow();
    }

    public static void AddProduct(String name, String price, int amount, int categoryID)
    {
        produkty.dodaj(name, price, amount, categoryID);
    }












    public static void Original()
    {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Scanner scanner = new Scanner(System.in);
            boolean isLoggedIn = false;

            while (!isLoggedIn) {
                // Pobierz dane do logowania od użytkownika
                System.out.print("Podaj stanowisko: ");
                String user = scanner.nextLine();

                System.out.print("Podaj hasło: ");
                String password = scanner.nextLine();

                try (Connection connection = DriverManager.getConnection(url, user, password)) {
                    // Logowanie użytkownika
                    System.out.println("Witaj na koncie stanowiska " + user);
                    isLoggedIn = true;



                    // Menu użytkownika
                    boolean isUserLoggedIn = true;
                    while (isUserLoggedIn) {
                        System.out.println("\nWybierz opcję:");
                        System.out.println("1. Zarządzaj towarami");
                        System.out.println("2. Zarządzaj pracownikami");
                        System.out.println("3. Generuj raporty");
                        System.out.println("4. Zarządzaj kategoriami produktów");
                        System.out.println("5. Wyloguj się");
                        System.out.println("6. Zakończ program");
                        System.out.println("Wybór: ");
                        int choice = scanner.nextInt();
                        scanner.nextLine(); // Konsumowanie znaku nowej linii

                        switch (choice) {
                            case 1:
                                Produkty produkty=new Produkty(connection);
                                boolean backToMenu = true;
                                while(backToMenu==true){
                                    produkty.wyswietl(); //odkomentować jeśli ma się każdorazowo wszystko wyświetlić
                                    //ja bym to w gui zrobił żeprzy zarządzaniu towarami jest lista wszystkiego i np
                                    //na górze panel z wyszukiwaniem, dodawaniem itd ale to tylko moja sugestia


                                    System.out.println("Wybierz opcje: ");
                                    System.out.println("1. Dodaj produkt");
                                    System.out.println("2. Edytuj produkt");
                                    System.out.println("3. Usuń produkt");
                                    System.out.println("4. Wyszukaj produkt po nazwie");
                                    System.out.println("5. Wyszukaj produkt po cenie");
                                    System.out.println("6. Wyszukaj produkt po ilości");
                                    System.out.println("7. Powrót");
                                    int wyb1 = scanner.nextInt();
                                    scanner.nextLine();

                                    switch (wyb1) {

                                        case 1:
                                            System.out.println("Podaj nazwe: ");
                                            String nazwa = scanner.nextLine();
                                            System.out.println("Podaje cenę : ");
                                            String cena = scanner.nextLine(); //cena jest Stringiem w bazie, uwaga na edycję typów, trzeba
                                            //też edytować kod bazy, dałem string żeby można było se np zł na eur zmienić
                                            System.out.println("Podaj ilość: ");
                                            int ilosc = scanner.nextInt();
                                            scanner.nextLine();
                                            System.out.println(" Podaj ID kategorii: ");
                                            int id = scanner.nextInt();
                                            scanner.nextLine();

                                            produkty.dodaj(nazwa, cena, ilosc, id);

                                            break;

                                        case 2:

                                            System.out.println("Podaj ID produktu który chcesz edytować: ");
                                            int IDGlowne = scanner.nextInt();
                                            scanner.nextLine();
                                            System.out.println("Podaj nową nazwe: ");
                                            nazwa = scanner.nextLine();
                                            System.out.println("Podaj nową  cenę : ");
                                            cena = scanner.nextLine(); //cena jest Stringiem, uwaga na to, nie polecam
                                            //zmieniać typów bo się chlew z niezgodnością separatorów robi. wpisujemy z przecinkim nie kropką.
                                            System.out.println("Podaj ilość: ");
                                            ilosc = scanner.nextInt();
                                            scanner.nextLine();
                                            System.out.println(" Podaj ID kategorii: ");
                                            id = scanner.nextInt();
                                            scanner.nextLine();
                                            produkty.edytuj(IDGlowne, nazwa, cena, ilosc, id);
                                            break;

                                        case 3:
                                            System.out.println("Podaj ID produktu do usunięcia: ");
                                            id = scanner.nextInt();
                                            scanner.nextLine();
                                            produkty.usun(id);
                                            break;

                                        case 4:
                                            System.out.println(" Podaj nazwe produktu: ");
                                            String nazwap = scanner.nextLine();
                                            produkty.szukajPoNazwie(nazwap);
                                            break;


                                        case 5:
                                            System.out.println("Podaj minimalną cenę: ");
                                            String cenaMin = scanner.nextLine();
                                            System.out.println("Podaj maksymalną cenę: ");
                                            String cenaMax = scanner.nextLine();
                                            produkty.szukajPoCenie(cenaMin, cenaMax);
                                            break;

                                        case 6:
                                            System.out.println("Podaj minimalną ilość: ");
                                            int iloscMin = scanner.nextInt();
                                            scanner.nextLine();
                                            System.out.println("Podaj maksymalną ilość: ");
                                            int iloscMax = scanner.nextInt();
                                            scanner.nextLine();
                                            produkty.szukajPoIlosci(iloscMin, iloscMax);
                                            break;

                                        case 7:
                                            backToMenu=false;
                                            break;

                                        default:
                                            System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");


                                    }}

                                break;

                            case 2:
                                Pracownik pracownik=new Pracownik(connection);

                                boolean backToMenu1 = true;
                                while(backToMenu1==true){

                                    pracownik.wyswietl();
                                    System.out.println("Wybierz opcje: ");
                                    System.out.println("1. Dodaj pracownika");
                                    System.out.println("2. Edytuj pracownika");
                                    System.out.println("3. Wyszukaj pracownika (po nazwisku)");
                                    System.out.println("4. Zwolnij pracownika");
                                    System.out.println("5. Powrót");
                                    int wyb1 = scanner.nextInt();
                                    scanner.nextLine();

                                    switch (wyb1) {

                                        case 1:
                                            System.out.println("Podaj Imie: ");
                                            String imie = scanner.nextLine();
                                            System.out.println("Podaj nazwisko : ");
                                            String nazwisko = scanner.nextLine();
                                            System.out.println("Podaj email: ");
                                            String email = scanner.nextLine();
                                            System.out.println("Podaj Stanowisko: ");
                                            String stanowisko=scanner.nextLine();
                                            System.out.println("Podaj wynagrodzenie: ");
                                            String wynagrodzenie=scanner.nextLine();
                                            pracownik.dodaj(imie, nazwisko, email, stanowisko, wynagrodzenie);
                                            break;

                                        case 2:
                                            System.out.println("Podaj ID pracownika do edycji: ");
                                            int ID=scanner.nextInt();
                                            scanner.nextLine();
                                            System.out.println("Podaj Imie: ");
                                            imie = scanner.nextLine();
                                            System.out.println("Podaj nazwisko : ");
                                            nazwisko = scanner.nextLine();
                                            System.out.println("Podaj email: ");
                                            email = scanner.nextLine();
                                            System.out.println("Podaj Stanowisko: ");
                                            stanowisko=scanner.nextLine();
                                            System.out.println("Podaj wynagrodzenie: ");
                                            wynagrodzenie=scanner.nextLine();
                                            pracownik.edytuj( ID, imie, nazwisko, email, stanowisko, wynagrodzenie);
                                            break;

                                        case 3:
                                            System.out.println("Podaj nazwisko: ");
                                            nazwisko=scanner.nextLine();
                                            pracownik.wyszukaj(nazwisko);
                                            break;

                                        case 4:
                                            System.out.println("Podaj ID pracownika: ");
                                            ID=scanner.nextInt();
                                            scanner.nextLine();
                                            pracownik.usun(ID);
                                            break;

                                        case 5:
                                            backToMenu1=false;
                                            break;

                                        default:
                                            System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");


                                    }
                                }

                                break;
                            case 3:
                                Raporty raporty=new Raporty(connection);
                                boolean backToMenu2 = true;
                                while(backToMenu2==true) {
                                    raporty.wyswietl();
                                    System.out.println("Wybierz opcje: ");
                                    System.out.println("1. Wyszukaj raport po id pracownika");
                                    System.out.println("2. Wyświetl raport");
                                    System.out.println("3. Dodaj raport (do 2000 znaków):");
                                    System.out.println("4. Usun raport;");
                                    System.out.println("5. Powrót");
                                    int wyb1 = scanner.nextInt();
                                    scanner.nextLine();

                                    switch (wyb1) {

                                        case 1:
                                            System.out.println("Podaj ID pracownika: ");
                                            int ID = scanner.nextInt();
                                            scanner.nextLine();
                                            raporty.wyszukajPoAutorze(ID);
                                            break;

                                        case 2:
                                            System.out.println("Podaj nazwe: ");
                                            String nazwa = scanner.nextLine();
                                            raporty.wyszukajPoNazwie(nazwa);
                                            break;

                                        case 3:
                                            System.out.println("Podaj nazwe: ");
                                            nazwa = scanner.nextLine();
                                            System.out.println("Podaj ID autora: ");
                                            ID = scanner.nextInt();
                                            scanner.nextLine();
                                            System.out.println("Wpisz treść raportu: ");
                                            String tresc = scanner.nextLine();
                                            raporty.dodaj(nazwa, ID, tresc);
                                            break;

                                        case 4:
                                            System.out.println("Podaj ID raportu który chcesz usunąć: ");
                                            ID = scanner.nextInt();
                                            scanner.nextLine();
                                            raporty.usun(ID);
                                            break;

                                        case 5:
                                            backToMenu2=false;
                                            break;

                                        default:
                                            System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");

                                    }
                                }
                                break;

                            case 4:
                                Kategoria kategoria=new Kategoria(connection);
                                boolean backToMenu3 = true;
                                while(backToMenu3==true) {
                                    kategoria.wyswietl();
                                    System.out.println("Wybierz opcje: ");
                                    System.out.println("1. Wyszukaj kategorie produktu");
                                    System.out.println("2. Dodaj kategorie");
                                    System.out.println("3. Usun kategorie");
                                    System.out.println("4. Powrót");
                                    int wyb1 = scanner.nextInt();
                                    scanner.nextLine();

                                    switch (wyb1) {

                                        case 1:
                                            System.out.println("Podaj nazwe kategorii: ");
                                            String nazwa = scanner.nextLine();
                                            kategoria.wyszukaj(nazwa);
                                            break;

                                        case 2:
                                            System.out.println("Podaj nazwe kategorii: ");
                                            nazwa = scanner.nextLine();
                                            kategoria.dodaj(nazwa);
                                            break;

                                        case 3:
                                            System.out.println("Podaj ID kategorii którą chcesz usunąć: ");
                                            int ID=scanner.nextInt();
                                            scanner.nextLine();
                                            kategoria.usun(ID);
                                            break;


                                        case 4:
                                            backToMenu3 = false;
                                            break;

                                        default:
                                            System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
                                    }}
                                break;

                            case 5:
                                isUserLoggedIn = false;
                                System.out.println("Wylogowano.");

                                // Ponowne logowanie
                                isLoggedIn = false;
                                break;
                            case 6:
                                System.out.println("Zakończono program.");
                                System.exit(0);
                                break;


                            default:
                                System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
                        }
                    }

                } catch (SQLException e) {
                    System.out.println("Błąd logowania. Spróbuj ponownie.");
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
