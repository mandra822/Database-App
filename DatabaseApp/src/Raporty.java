
import java.sql.Connection;
import oracle.jdbc.internal.OracleTypes;

import java.sql.*;
import java.util.Scanner;

public class Raporty {
    private Connection connection;

    public Raporty(Connection connection) {
        this.connection = connection;
    }

    public void wyswietl() {
        try {
            String query = "SELECT ID, DataWykonania, Nazwa, ID_Autora FROM Raporty ORDER BY ID ASC";//
            try (Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery(query)) {

                while (rs.next()) {
                    int raportID = rs.getInt("ID");
                    Date dataWykonania = rs.getDate("DataWykonania");
                    String raportNazwa = rs.getString("Nazwa");
                    int autorID = rs.getInt("ID_Autora");

                    System.out.println("ID: " + raportID + ", Data wykonania: " + dataWykonania + ", Nazwa: " + raportNazwa + ", Autor ID: " + autorID);
                    // Dodaj wyświetlanie lub przetwarzanie innych danych z pozostałych kolumn
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void wyszukajPoAutorze(int ID) {
        try {
            try (CallableStatement cs = connection.prepareCall("{call WyszukajRaportPoAutorze(?, ?)}")) {//
                cs.setInt(1, ID);
                cs.registerOutParameter(2, OracleTypes.CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    while (rs.next()) {
                        int raportID = rs.getInt("ID");
                        String nazwa = rs.getString("Nazwa");
                        // ... pobieranie innych kolumn ...

                        System.out.println("ID: " + raportID + ", Nazwa: " + nazwa);
                        // ... wyświetlanie lub przetwarzanie innych danych ...
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void wyszukajPoNazwie(String nazwa) {
        try {
            try (CallableStatement cs = connection.prepareCall("{call WyszukajRaportPoNazwie(?, ?)}")) {//
                cs.setString(1, nazwa);
                cs.registerOutParameter(2, OracleTypes.CURSOR);
                cs.execute();

                try (ResultSet rs = (ResultSet) cs.getObject(2)) {
                    while (rs.next()) {
                        int raportID = rs.getInt("ID");
                        String dataWykonania = rs.getString("DataWykonania");
                        String raportNazwa = rs.getString("Nazwa");
                        int autorID = rs.getInt("ID_Autora");
                        String tresc = rs.getString("Tresc");

                        System.out.println("ID: " + raportID + ", Data wykonania: " + dataWykonania + ", Nazwa: " + raportNazwa + ", Autor ID: " + autorID + ", Treść: " + tresc);

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodaj(String nazwa, int id_autora, String tresc) {
        try {
            try (CallableStatement cs = connection.prepareCall("{call DodajNowyRaport(?, ?, ?)}")) {//
                cs.setString(1, nazwa);
                cs.setInt(2, id_autora);
                cs.setString(3, tresc);
                cs.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void usun(int ID) {
        try {
            try (CallableStatement cs = connection.prepareCall("{call UsunRaport(?)}")) {//
                cs.setInt(1, ID);
                cs.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
