
import java.sql.Connection;
import oracle.jdbc.internal.OracleTypes;

import java.sql.*;
import java.util.Scanner;

public class Kategoria {
    private Connection connection;

    public Kategoria(Connection connection) {
        this.connection = connection;
    }

    public void wyswietl() {
        try {
            String query = "SELECT ID, Nazwa FROM KategoriaProduktow ORDER BY ID ASC"; //
            try (Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery(query)) {

                while (rs.next()) {
                    int kategoriaID = rs.getInt("ID");
                    String nazwa = rs.getString("Nazwa");

                    System.out.println("ID: " + kategoriaID + ", Nazwa: " + nazwa);
                    // Dodaj wyświetlanie lub przetwarzanie innych danych z pozostałych kolumn
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void dodaj(String nazwa) {
        try {
            try (CallableStatement cs = connection.prepareCall("{call DodajNowaKategorieProduktu(?)}")) {//
                cs.setString(1, nazwa);
                cs.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void wyszukaj(String nazwa) {
        try {
            try (CallableStatement cs = connection.prepareCall("{call WyszKatProduktPoNazwa(?, ?)}")) {//
                cs.setString(1, nazwa);
                cs.registerOutParameter(2, OracleTypes.CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    while (rs.next()) {
                        int kategoriaID = rs.getInt("ID");
                        String kategoriaNazwa = rs.getString("Nazwa");

                        System.out.println("ID: " + kategoriaID + ", Nazwa: " + kategoriaNazwa);
                        // Dodaj wyświetlanie lub przetwarzanie innych danych z pozostałych kolumn
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void usun(int ID) {
        try {
            try (CallableStatement cs = connection.prepareCall("{call UsunKategorieProduktu(?)}")) {//
                cs.setInt(1, ID);
                cs.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
