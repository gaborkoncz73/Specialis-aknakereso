import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * A program indításakor megnyíló menüért felelős osztály.
 */
public class Menu extends JFrame implements ActionListener {
    JMenuBar mainMenuBar;
    JMenu fileMenu;
    JMenu gameMenu;
    JMenu hofMenu;
    JMenuItem showItem;
    JMenuItem loadItem;
    JMenuItem newGame;
    transient MenuManager menuManager;

    /**
     * A Menu konstruktora, létrehozza a menüt és inicializálja a tagokat.
     */
    public Menu(){
        menuManager = new MenuManager();
        menuManager.setMenu(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainMenuBar = new JMenuBar();
        fileMenu = new JMenu("Fájl");
        gameMenu = new JMenu("Játék");
        hofMenu = new JMenu("Dicsőségtábla");
        loadItem = new JMenuItem("Betölt");
        newGame = new JMenuItem("Új játék");
        showItem = new JMenuItem("Megnyit");
        hofMenu.add(showItem);
        gameMenu.add(newGame);
        fileMenu.add(loadItem);
        loadItem.addActionListener(this);
        File file  = new File("saved.txt");
        if(file.exists() && file.length() != 0){
            mainMenuBar.add(fileMenu);
        }
        mainMenuBar.add(gameMenu);
        mainMenuBar.add(hofMenu);
        this.setSize(500,500);
        this.setLayout(new FlowLayout());
        this.setJMenuBar(mainMenuBar);
        this.setVisible(true);
        newGame.addActionListener(this);
        showItem.addActionListener(this);
    }

    /**
     * main függvény mely elindítja a programot.
     *
     * @param args String[]
     */
    public static void main(String[] args){
        new Menu();
    }

    /**
     * A menüben szereplő különböző menüpontokon belüli elemek megnyomásakor történtekért felelős ActionListener
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newGame){
            new NewGame(menuManager);
        }else if(e.getSource() == showItem){
            this.setVisible(false);
            try {
                HallOfFameMenu hof = new HallOfFameMenu("hof.txt", menuManager);
                hof.createFrame();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }else if(e.getSource() == loadItem){
            try {
                this.setVisible(false);
                new Load();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
