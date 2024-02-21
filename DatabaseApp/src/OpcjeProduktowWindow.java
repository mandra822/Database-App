import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpcjeProduktowWindow implements ActionListener {

    JFrame window = new JFrame("Opcje produktow");
    JButton dodajProduktButton, edytujProduktButton, usunProduktButton, wyszukajProduktPoNazwieButton,
            wyszukajProduktPoCenieButton, wyszukajProduktPoIlosciButton, powrotButton;


    public OpcjeProduktowWindow(){
        window.setSize(300,250);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setLocationRelativeTo(null); //makes sure that new window is on the screen centre

        dodajProduktButton = new JButton("Dodaj produkt");
        dodajProduktButton.setBounds(60, 20, 180, 20);
        dodajProduktButton.addActionListener(this);
        window.add(dodajProduktButton);

        edytujProduktButton = new JButton("Edytuj produkt");
        edytujProduktButton.setBounds(60, 45, 180, 20);
        edytujProduktButton.addActionListener(this);
        window.add(edytujProduktButton);

        usunProduktButton = new JButton("Usun produkt");
        usunProduktButton.setBounds(60, 70, 180, 20);
        usunProduktButton.addActionListener(this);
        window.add(usunProduktButton);

        wyszukajProduktPoNazwieButton = new JButton("Wyszukaj produkt po nazwie");
        wyszukajProduktPoNazwieButton.setBounds(50, 95, 200, 20);
        wyszukajProduktPoNazwieButton.addActionListener(this);
        window.add(wyszukajProduktPoNazwieButton);

        wyszukajProduktPoCenieButton = new JButton("Wyszukaj produkt po cenie");
        wyszukajProduktPoCenieButton.setBounds(50, 120, 200, 20);
        wyszukajProduktPoCenieButton.addActionListener(this);
        window.add(wyszukajProduktPoCenieButton);

        wyszukajProduktPoIlosciButton = new JButton("Wyszukaj produkt po ilosci");
        wyszukajProduktPoIlosciButton.setBounds(50, 145, 200, 20);
        wyszukajProduktPoIlosciButton.addActionListener(this);
        window.add(wyszukajProduktPoIlosciButton);

        powrotButton = new JButton("Powrot do menu glownego");
        powrotButton.setBounds(50, 170, 200, 20);
        powrotButton.addActionListener(this);
        window.add(powrotButton);


        window.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
