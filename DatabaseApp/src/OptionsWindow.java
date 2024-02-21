import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsWindow implements ActionListener {
    JFrame window = new JFrame("Opcje");
    JButton zarzadzajTowaremButton, zarzadzajPracownikamiButton, generujRaportyButton,
            zarzadzajKategoriamiProduktowButton, wylogujButton, zakonczProgramButton;

    public OptionsWindow(){
        window.setSize(300,250);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setLocationRelativeTo(null); //makes sure that new window is on the screen centre

        zarzadzajTowaremButton = new JButton("Zarzadzaj Towarami");
        zarzadzajTowaremButton.setBounds(60, 20, 180, 20);
        zarzadzajTowaremButton.addActionListener(this);
        window.add(zarzadzajTowaremButton);

        zarzadzajPracownikamiButton = new JButton("Zarzadzaj Pracownikami");
        zarzadzajPracownikamiButton.setBounds(60, 45, 180, 20);
        zarzadzajPracownikamiButton.addActionListener(this);
        window.add(zarzadzajPracownikamiButton);

        generujRaportyButton = new JButton("Generuj raporty");
        generujRaportyButton.setBounds(60, 70, 180, 20);
        generujRaportyButton.addActionListener(this);
        window.add(generujRaportyButton);

        zarzadzajKategoriamiProduktowButton = new JButton("Zarzadzaj kategoriami produktow");
        zarzadzajKategoriamiProduktowButton.setBounds(20, 95, 250, 20);
        zarzadzajKategoriamiProduktowButton.addActionListener(this);
        window.add(zarzadzajKategoriamiProduktowButton);

        wylogujButton = new JButton("Wyloguj sie");
        wylogujButton.setBounds(60, 120, 180, 20);
        wylogujButton.addActionListener(this);
        window.add(wylogujButton);

        zakonczProgramButton = new JButton("Zakoncz program");
        zakonczProgramButton.setBounds(60, 145, 180, 20);
        zakonczProgramButton.addActionListener(this);
        window.add(zakonczProgramButton);

        window.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
