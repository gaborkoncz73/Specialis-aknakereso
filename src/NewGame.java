import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Az új játék létrehozásáért felelős osztály.
 */
public class NewGame extends JFrame implements ActionListener {
    JComboBox<String> shapeComboBox;
    JComboBox<String> difficultyComboBox;
    JButton startButton;
    JCheckBox[] helpButtons;
    int maxHelp = 2;
    int actualHelp=0;
    private final transient MenuManager menuManager;

    /**
     * A NewGame osztály konstuktora, amely a létrehozza a NewGame frame-t és az ott beállított értékek alapján legenerálja az IngameMenut.
     *
     * @param menuManager MenuManagaer
     */
        public NewGame(MenuManager menuManager){
            menuManager.menu.setVisible(false);
            this.menuManager = menuManager;
            menuManager.setNewGame(this);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setSize(400,300);
            this.setResizable(false);
            this.setTitle("Új játék");
            this.setLayout(new BorderLayout());
            this.setVisible(true);
            this.helpButtons = new JCheckBox[3];
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


            JLabel shapeLabel= new JLabel("Válassza ki a mezők alakját:");
            String[] polygons = {"Háromszög", "Négyzet", "Hatszög"};
            shapeComboBox = new JComboBox<>(polygons);
            shapeComboBox.addActionListener(this);
            shapeComboBox.setPreferredSize(new Dimension(100,50));

            JPanel shapePanel = new JPanel(new FlowLayout());
            shapePanel.add(shapeLabel);
            shapePanel.add(shapeComboBox);


            JLabel difficultyLabel = new JLabel("Válassza ki a nehézségi fokozatot:");
            String[] difficulties = {"Könnyű", "Közepes", "Nehéz"};
            difficultyComboBox = new JComboBox<>(difficulties);
            difficultyComboBox.addActionListener(this);
            difficultyComboBox.setPreferredSize(new Dimension(100,50));

            helpButtons[0] = new JCheckBox("Potenciális aknamezők");
            helpButtons[1] = new JCheckBox("Random biztonságos mező");
            helpButtons[2] = new JCheckBox("Random akna a felfedettek körül");

            JPanel helpPanel = new JPanel();
            helpPanel.setLayout(new BoxLayout(helpPanel,BoxLayout.Y_AXIS));

            for(int i = 0; i < 3; i++){
                helpButtons[i].addActionListener(this);
                helpButtons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
                helpPanel.add(helpButtons[i]);
            }

            JPanel difficultyPanel = new JPanel(new FlowLayout());
            difficultyPanel.add(difficultyLabel);
            difficultyPanel.add(difficultyComboBox);


            startButton = new JButton("Start");
            startButton.addActionListener(this);
            startButton.setPreferredSize(new Dimension(200,50));

            JPanel startPanel = new JPanel(new FlowLayout());
            startPanel.add(startButton);

            JPanel innerSouth = new JPanel(new BorderLayout());
            innerSouth.setSize(new Dimension(500,600));
            innerSouth.add(helpPanel,BorderLayout.NORTH);
            innerSouth.add(startPanel,BorderLayout.CENTER);


            this.add(shapePanel,BorderLayout.NORTH);
            this.add(difficultyPanel,BorderLayout.CENTER);
            this.add(innerSouth,BorderLayout.SOUTH);

        }

    /**
     * A menüben szereplő különböző mezők megnyomásakor történő dolgokért felelős függvény.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(e.getSource()==startButton){
            startGame();
        }else if(source instanceof JCheckBox selectedCheckBox){
            boxChecked(selectedCheckBox);
        }
        else if(e.getSource() == difficultyComboBox){
            helpCounter();
        }
    }

    /**
     * A beállított értékek alapján kiszámolja a különböző értékeket és létrehozza az objektumokat, amelyek az InGameMenu számára fontosak majd létrehozza azt is.
     */
    public void startGame(){
        int difficulty = difficultyComboBox.getSelectedIndex();
        int shapeIndex = shapeComboBox.getSelectedIndex();
        int vertices = 0;
        int size=0;
        int row=0;
        int col=0;
        int font = 0;
        int specials = 0;
        vertices = switch (shapeIndex) {
            case 0 -> 3;
            case 1 -> 4;
            case 2 -> 6;
            default -> vertices;
        };
        switch (difficulty){
            case 0:
                row=8;
                col=10;
                size = 80;
                font = 40;
                specials = 3;
                break;
            case 1:
                row = 10;
                col = 26;
                size = 48;
                font = 24;
                specials = 2;
                break;
            case 2:
                row = 16;
                col = 30;
                size=40;
                font = 10;
                specials = 1;
                break;
            default:
                break;
        }
        MineSweeperFrame msf = new MineSweeperFrame(vertices,size,size*col,size*row, font, specials);
        List<HelpContainer> helpContainers = new ArrayList<>();
        if(helpButtons[0].isSelected()){
            helpContainers.add(new PotentialBomb(msf.getButtonGrid(),new NeighbourManager(msf.buttonGrid,msf.getVertices())));
        }
        if(helpButtons[1].isSelected()){
            helpContainers.add(new SafeSquare(msf.getButtonGrid(),new NeighbourManager(msf.buttonGrid,msf.getVertices())));
        }
        if(helpButtons[2].isSelected()){
            helpContainers.add(new SurroundingBomb(msf.getButtonGrid(),new NeighbourManager(msf.buttonGrid,msf.getVertices())));
        }
        msf.getMenuManager().setMenu(menuManager.menu);
        msf.getMenuManager().setNewGame(this);
        this.setVisible(false);
        new InGameMenu(msf,helpContainers);
    }
    public void helpCounter(){
        maxHelp = switch (difficultyComboBox.getSelectedIndex()) {
            case 0 -> 2;
            case 1 -> 1;
            case 2 -> 0;
            default -> maxHelp;
        };
        if(actualHelp > maxHelp){
            helpButtons[0].setSelected(false);
            helpButtons[1].setSelected(false);
            helpButtons[2].setSelected(false);
            actualHelp=0;
        }
        for (JCheckBox checkBox : helpButtons) {
            if(maxHelp == 0){
                checkBox.setEnabled(false);
            }else checkBox.setEnabled(maxHelp != 1 || actualHelp != 1 || checkBox.isSelected());
        }
    }

    /**
     * Egy CheckBox értékének módosításakor meghívandó függvény, amely kezeli hogy a nehézségtől függően hány segítség érhető el.
     *
     * @param selectedCheckBox JCheckBox
     */
    public void boxChecked(JCheckBox selectedCheckBox){
        if (selectedCheckBox.isSelected()) {
            actualHelp++;
            if (maxHelp <= actualHelp) {
                for (JCheckBox checkBox : helpButtons) {
                    if (!checkBox.isSelected() && checkBox != selectedCheckBox) {
                        checkBox.setEnabled(false);
                    }
                }
            }
        } else {
            for (JCheckBox checkBox : helpButtons) {
                if (!checkBox.isSelected() && checkBox != selectedCheckBox) {
                    checkBox.setEnabled(true);
                }
            }
            actualHelp--;
        }
    }
}
