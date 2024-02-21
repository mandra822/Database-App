import oracle.jdbc.internal.OracleTypes;

import java.sql.*;
import java.util.Scanner;

public class Produkty {

    private Connection connection;

    public Produkty(Connection connection) {
        this.connection = connection;
    }

    public void wyswietl() {
        String sql = "SELECT ID, Nazwa, Cena, Ilosc, ID_Kategorii FROM Produkt ORDER BY ID ASC";//
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Lista produktów:");
            System.out.println("ID | Nazwa | Cena | Ilość | ID Kategorii");

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String nazwa = resultSet.getString("Nazwa");
                double cena = resultSet.getDouble("Cena");
                int ilosc = resultSet.getInt("Ilosc");
                int idKategorii = resultSet.getInt("ID_Kategorii");

                System.out.printf("%d | %s | %.2f | %d | %d%n", id, nazwa, cena, ilosc, idKategorii);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void dodaj(String nazwa, String cena, int ilosc, int idKategorii) {
        // Wywołanie procedury
        try (CallableStatement callableStatement = connection.prepareCall("{call DodajNowyProdukt(?, ?, ?, ?)}")) {//
            // Parametry wejściowe
            callableStatement.setString(1, nazwa);
            callableStatement.setString(2, cena);
            callableStatement.setInt(3, ilosc);
            callableStatement.setInt(4, idKategorii);

            // Wykonanie procedury
            callableStatement.execute();

            System.out.println("Nowy produkt dodany: " + nazwa);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void edytuj(int id, String nazwa, String cena, int ilosc, int idKategorii) {
        try (CallableStatement callableStatement = connection.prepareCall("{call AktualizujProdukt(?, ?, ?, ?, ?)}")) {//
            callableStatement.setInt(1, id);
            callableStatement.setString(2, nazwa);
            callableStatement.setString(3, cena);
            callableStatement.setInt(4, ilosc);
            callableStatement.setInt(5, idKategorii);

            callableStatement.execute();
            System.out.println("Produkt został zaktualizowany.");
        } catch (SQLException e) {
            System.out.println("Wystąpił błąd przy aktualizacji produktu.");
            e.printStackTrace();
        }
    }




    public void usun(int id) {
        // Wywołanie procedury
        try (CallableStatement callableStatement = connection.prepareCall("{call UsunProdukt(?)}")) {//
            // Parametr wejściowy
            callableStatement.setInt(1, id);

            // Wykonanie procedury
            callableStatement.execute();

            System.out.println("Produkt o ID " + id + " został usunięty.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void szukajPoNazwie(String nazwa) {
        try (CallableStatement callableStatement = connection.prepareCall("{call WyszukajProduktPoNazwie(?, ?)}")) {//
            callableStatement.setString(1, nazwa);
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);

            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(2)) {
                System.out.println("Znalezione produkty:");
                System.out.println("ID | Nazwa | Cena | Ilość | ID Kategorii");

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String nazwaProduktu = resultSet.getString("Nazwa");
                    double cena = resultSet.getDouble("Cena");
                    int ilosc = resultSet.getInt("Ilosc");
                    int idKategorii = resultSet.getInt("ID_Kategorii");

                    System.out.printf("%d | %s | %.2f | %d | %d%n", id, nazwaProduktu, cena, ilosc, idKategorii);
                }
            }
        } catch (SQLException e) {
            System.out.println("Wystąpił błąd przy wyszukiwaniu produktu.");
            e.printStackTrace();
        }
    }


    public void szukajPoCenie(String minCena, String maxCena) {
        // Wywołanie procedury
        try (CallableStatement callableStatement = connection.prepareCall("{call WyszukajProduktPoCenie(?, ?, ?)}")) {//
            // Parametry wejściowe
            callableStatement.setString(1, minCena);
            callableStatement.setString(2, maxCena);

            // Parametr wyjściowy (kursor)
            callableStatement.registerOutParameter(3, OracleTypes.CURSOR);

            // Wykonanie procedury
            callableStatement.execute();

            // Pobranie wyników z kursora
            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(3)) {
                System.out.println("Znalezione produkty w przedziale cenowym od " + minCena + " do " + maxCena + ":");
                System.out.println("ID | Nazwa | Cena | Ilość | ID Kategorii");

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String nazwaProduktu = resultSet.getString("Nazwa");
                    double cena = resultSet.getDouble("Cena");
                    int ilosc = resultSet.getInt("Ilosc");
                    int idKategorii = resultSet.getInt("ID_Kategorii");

                    System.out.printf("%d | %s | %.2f | %d | %d%n", id, nazwaProduktu, cena, ilosc, idKategorii);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void szukajPoIlosci(int minIlosc, int maxIlosc) {
        // Wywołanie procedury
        try (CallableStatement callableStatement = connection.prepareCall("{call WyszukajProduktPoIlosci(?, ?, ?)}")) {//
            // Parametry wejściowe
            callableStatement.setInt(1, minIlosc);
            callableStatement.setInt(2, maxIlosc);

            // Parametr wyjściowy (kursor)
            callableStatement.registerOutParameter(3, OracleTypes.CURSOR);

            // Wykonanie procedury
            callableStatement.execute();

            // Pobranie wyników z kursora
            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(3)) {
                System.out.println("Znalezione produkty w przedziale ilościowym od " + minIlosc + " do " + maxIlosc + ":");
                System.out.println("ID | Nazwa | Cena | Ilość | ID Kategorii");

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String nazwaProduktu = resultSet.getString("Nazwa");
                    double cena = resultSet.getDouble("Cena");
                    int ilosc = resultSet.getInt("Ilosc");
                    int idKategorii = resultSet.getInt("ID_Kategorii");

                    System.out.printf("%d | %s | %.2f | %d | %d%n", id, nazwaProduktu, cena, ilosc, idKategorii);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
