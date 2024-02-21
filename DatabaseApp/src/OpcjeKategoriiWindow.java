import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpcjeKategoriiWindow implements ActionListener {

    JFrame window = new JFrame("Opcje kategorii produktow");
    JButton wyszukajKategorieButton, dodajKategorieButton, usunKategorieButton, powrotButton;
    public OpcjeKategoriiWindow(GUI gui){

        window.setSize(500,200);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setLocationRelativeTo(null); //makes sure that new window is on the screen centre

        wyszukajKategorieButton = new JButton("Wyszukaj kategorie produktow");
        wyszukajKategorieButton.setBounds(110, 20, 220, 20);
        wyszukajKategorieButton.addActionListener(this);
        window.add(wyszukajKategorieButton);

        dodajKategorieButton = new JButton("Dodaj kategorie produktow");
        dodajKategorieButton.setBounds(110, 45, 220, 20);
        dodajKategorieButton.addActionListener(this);
        window.add(dodajKategorieButton);

        usunKategorieButton = new JButton("Usun kategorie produktow");
        usunKategorieButton.setBounds(110, 70, 220, 20);
        usunKategorieButton.addActionListener(this);
        window.add(usunKategorieButton);


        powrotButton = new JButton("Powrot do menu glownego");
        powrotButton.setBounds(110, 95, 220, 20);
        powrotButton.addActionListener(this);
        window.add(powrotButton);

        window.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
