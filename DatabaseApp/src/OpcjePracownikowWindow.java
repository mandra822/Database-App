import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpcjePracownikowWindow implements ActionListener {

    JFrame window = new JFrame("Opcje pracownikow");
    JButton dodajPracownikaButton, edytujPracownikaButton, wyszukajPracownikaPoNazwiskuButton, zwolnijPracownikaButton, powrotButton;
    OpcjePracownikowWindow(GUI gui){
        window.setSize(300,250);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setLocationRelativeTo(null); //makes sure that new window is on the screen centre

        dodajPracownikaButton = new JButton("Dodaj pracownika");
        dodajPracownikaButton.setBounds(60, 20, 180, 20);
        dodajPracownikaButton.addActionListener(this);
        window.add(dodajPracownikaButton);

        edytujPracownikaButton = new JButton("Edytuj pracownika");
        edytujPracownikaButton.setBounds(60, 45, 180, 20);
        edytujPracownikaButton.addActionListener(this);
        window.add(edytujPracownikaButton);

        wyszukajPracownikaPoNazwiskuButton = new JButton("Wyszukaj pracownika (po nazwisku)");
        wyszukajPracownikaPoNazwiskuButton.setBounds(20, 70, 250, 20);
        wyszukajPracownikaPoNazwiskuButton.addActionListener(this);
        window.add(wyszukajPracownikaPoNazwiskuButton);

        zwolnijPracownikaButton = new JButton("Zwolnij pracownika");
        zwolnijPracownikaButton.setBounds(60, 95, 180, 20);
        zwolnijPracownikaButton.addActionListener(this);
        window.add(zwolnijPracownikaButton);

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
