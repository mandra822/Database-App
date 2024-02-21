import java.sql.Connection;
import oracle.jdbc.internal.OracleTypes;

import java.sql.*;
import java.util.Scanner;

public class Pracownik {
    private Connection connection;

    public Pracownik(Connection connection) {
        this.connection = connection;
    }

    public void wyswietl() {
        String sql = "SELECT ID, Imie, Nazwisko, Email, Stanowisko, Wynagrodzenie FROM Pracownik ORDER BY ID ASC";//
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Lista pracowników:");
            System.out.println("ID | Imię | Nazwisko | Email | Stanowisko | Wynagrodzenie");

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String imie = resultSet.getString("Imie");
                String nazwisko = resultSet.getString("Nazwisko");
                String email = resultSet.getString("Email");
                String stanowisko = resultSet.getString("Stanowisko");
                double wynagrodzenie = resultSet.getDouble("Wynagrodzenie");

                System.out.printf("%d | %s | %s | %s | %s | %.2f%n", id, imie, nazwisko, email, stanowisko, wynagrodzenie);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodaj(String imie, String nazwisko, String email, String stanowisko, String wynagrodzenie) {
        try {
            // Wywołanie procedury DodajNowegoPracownika
            String query = "{CALL DodajNowegoPracownika(?, ?, ?, ?, ?)}";//
            try (CallableStatement statement = connection.prepareCall(query)) {
                statement.setString(1, imie);
                statement.setString(2, nazwisko);
                statement.setString(3, email);
                statement.setString(4, stanowisko);
                statement.setString(5, wynagrodzenie);

                // Wykonanie procedury
                statement.execute();
                System.out.println("Dodano nowego pracownika.");
            }
        } catch (SQLException e) {
            System.out.println("Wystąpił błąd przy dodawaniu pracownika.");
            e.printStackTrace();
            System.out.println(" Coś poszło nie tak, prawodpodobnie twoje stanowisko nie posiada uprawnień do tej operacji.");
        }
    }

    public void wyszukaj(String nazwisko) {
        try {
            // Wywołaj procedurę
            try (CallableStatement cs = connection.prepareCall("{callWyszukajPracownikaPoNazwisku(?, ?)}")) {//
                cs.setString(1, nazwisko);
                cs.registerOutParameter(2, OracleTypes.CURSOR);
                cs.execute();

                // Pobierz wyniki
                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    if (!rs.isBeforeFirst()) {
                        System.out.println("Brak wyników dla nazwiska: " + nazwisko);
                    } else {
                        while (rs.next()) {
                            // Przetwarzaj wyniki
                            int id = rs.getInt("ID");
                            String imie = rs.getString("Imie");
                            String nazwiskoResult = rs.getString("Nazwisko");
                            String email = rs.getString("Email");
                            String stanowisko = rs.getString("Stanowisko");
                            String wynagrodzenie = rs.getString("Wynagrodzenie");

                            // Wyświetl lub przetwórz dane
                            System.out.println("ID: " + id + ", Imię: " + imie + ", Nazwisko: " + nazwiskoResult
                                    + ", Email: " + email + ", Stanowisko: " + stanowisko + ", Wynagrodzenie: " + wynagrodzenie);
                            // ... wyświetlanie lub przetwarzanie innych danych ...
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Coś poszło nie tak, prawodpodobnie twoje stanowisko nie posiada uprawnień do tej operacji.");
        }
    }

    public void edytuj(int ID, String imie, String nazwisko, String email, String stanowisko, String wynagrodzenie) {
        try {
            // Wywołaj procedurę
            try (CallableStatement cs = connection.prepareCall("{call AktuInfoOPracowniku(?, ?, ?, ?, ?, ?)}")) {//
                cs.setInt(1, ID);
                cs.setString(2, imie);
                cs.setString(3, nazwisko);
                cs.setString(4, email);
                cs.setString(5, stanowisko);
                cs.setString(6, wynagrodzenie);
                cs.execute();
            }

            System.out.println("Zaktualizowano informacje o pracowniku o ID: " + ID);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Coś poszło nie tak, prawodpodobnie twoje stanowisko nie posiada uprawnień do tej operacji. Albo pracownik jest autorem raportu, więc usuń najpierw jego raporty.");
        }
    }

    public void usun(int ID) {
        try {
            // Wywołaj procedurę
            try (CallableStatement cs = connection.prepareCall("{call UsunPracownika(?)}")) {//
                cs.setInt(1, ID);
                cs.execute();
            }

            System.out.println("Usunięto pracownika o ID: " + ID);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Coś poszło nie tak, prawodpodobnie twoje stanowisko nie posiada uprawnień do tej operacji.");
        }
    }



}
