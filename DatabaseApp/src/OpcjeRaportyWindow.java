import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpcjeRaportyWindow implements ActionListener {

    JFrame window = new JFrame("Opcje raportow");
    JButton wyszukajRaportPoIdPracownikaButton, wyswietlRaportButton, dodajRaportButton, usunRaportButton, powrotButton;
    public OpcjeRaportyWindow(GUI gui){
        window.setSize(300,250);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setLocationRelativeTo(null); //makes sure that new window is on the screen centre

        wyszukajRaportPoIdPracownikaButton = new JButton("Wyszukaj raport po id pracownika");
        wyszukajRaportPoIdPracownikaButton.setBounds(30, 20, 230, 20);
        wyszukajRaportPoIdPracownikaButton.addActionListener(this);
        window.add(wyszukajRaportPoIdPracownikaButton);

        wyswietlRaportButton = new JButton("Wyswietl raport");
        wyswietlRaportButton.setBounds(60, 45, 180, 20);
        wyswietlRaportButton.addActionListener(this);
        window.add(wyswietlRaportButton);

        dodajRaportButton = new JButton("Dodaj raport");
        dodajRaportButton.setBounds(60, 70, 180, 20);
        dodajRaportButton.addActionListener(this);
        window.add(dodajRaportButton);

        usunRaportButton = new JButton("Usun raport");
        usunRaportButton.setBounds(60, 95, 180, 20);
        usunRaportButton.addActionListener(this);
        window.add(usunRaportButton);

        powrotButton = new JButton("Powrot do menu glownego");
        powrotButton.setBounds(50, 120, 200, 20);
        powrotButton.addActionListener(this);
        window.add(powrotButton);

        window.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
