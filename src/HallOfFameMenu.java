import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A dicsőségtábláért felelős osztály
 */
public class HallOfFameMenu implements ActionListener {
    private final String fileName;
    private final MenuManager menuManager;
    private JButton backButton;
    private JFrame frame;

    /**
     * A HallOfFameMenu osztály konstuktora
     *
     * @param fileName String
     * @param menuManager MenuManager
     */
    public HallOfFameMenu(String fileName, MenuManager menuManager){
        this.fileName = fileName;
        this.menuManager = menuManager;
    }

    /**
     * Létrehozza az új framet beállítja az értékeit, majd a dicsőségtáblát tartalmazó fájlból beolvssa a korábbi eredményeket és megjeleníti
     *
     * @throws FileNotFoundException
     */
    public void createFrame() throws FileNotFoundException {
        frame = new JFrame("Dicsőségtábla");
        frame.setSize(300,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        backButton = new JButton("Vissza");
        backButton.addActionListener(this);
        frame.add(jScrollPane,BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);
        Scanner scanner = new Scanner(new File(fileName));
        while (scanner.hasNextLine()){
            jTextArea.append("\n" + scanner.nextLine());
        }

        frame.setVisible(true);
    }

    /**
     * A vissza gomb megnyomására eltűnteti, ezt a framet és visszahozza a fő menüt
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == backButton){
            frame.setVisible(false);
            menuManager.menu.setVisible(true);
        }
    }
}
