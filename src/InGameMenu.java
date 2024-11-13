import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * A játék közbeni menüért felelős osztály.
 */
public class InGameMenu extends JFrame implements ActionListener {
    private final MineSweeperFrame msf;
    private final List<HelpContainer> helpContainer;
    private final JMenuBar inGameMenuBar;
    private final JMenu special;
    private final JMenuItem saveItem;
    private final JMenuItem use;
    private int amountOfSpecial = 0;
    private JButton buttonOk;
    JFrame specialFrame;
    private String time;
    private final transient TimerState timerState;

    /**
     * Az InGameMenu konstruktora, mely inicializálja a tagokat a megadott paraméterek felhasználásával
     * Létrehozza a framet hozzáadja a menüpontokat, időzítőt, speciális mezőt, fájlba mentést és a pályát.
     *
     * @param msf MineSweeperFrame
     * @param hlpc HelpContainer
     */
    public InGameMenu(MineSweeperFrame msf, List<HelpContainer> hlpc){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        msf.getMenuManager().setInGameMenu(this);
        this.msf = msf;
        this.helpContainer = hlpc;
        JMenu fileMenu = new JMenu("Fájl");
        saveItem = new JMenuItem("Mentés");
        fileMenu.add(saveItem);
        saveItem.addActionListener(this);
        inGameMenuBar = new JMenuBar();
        inGameMenuBar.add(fileMenu);
        JMenu help = new JMenu("Help");
        special = new JMenu("Speciális mező");
        use = new JMenuItem("Felhasznál");
        use.addActionListener(this);
        special.add(use);
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(msf, BorderLayout.CENTER);
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        if(!helpContainer.isEmpty()){
            for(HelpContainer helpContainer1 : helpContainer){
                help.add(helpContainer1.menuItem());
            }
            inGameMenuBar.add(help);
        }
        setSize(2500,1500);
        this.setJMenuBar(inGameMenuBar);
        this.add(msf);
        this.setVisible(true);
        JMenuItem timerItem = new JMenuItem("00:00");
        timerState = new TimerState();
        Timer timer = new Timer(1000, e -> {
            timerState.seconds++;
            time = String.format("%02d:%02d", timerState.seconds / 60, timerState.seconds % 60);
            timerItem.setText(time);
        });

        timer.start();
        inGameMenuBar.add(Box.createHorizontalGlue());
        inGameMenuBar.add(timerItem);
        actionListenerForTheButtons();
    }

    /**
     * Különböző actionok esetén a különböző módosításokat hajtja végre.
     * Kezeli hogy mi fog történni, ha speciális mezőre kattintunk, ha éppen felhasználunk egy speciális mező nyújtotta segítséget, ha menteni szeretnénk.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source instanceof PolygonButton){
            PolygonButton polygonButton = (PolygonButton) source;
            if(polygonButton.getUsingSpecial() && polygonButton.isEnabled()){
                usingSpecialLogic(polygonButton);
            }

        }
        if(source instanceof PolygonButton){
            PolygonButton polygonButton = (PolygonButton) source;
            if(polygonButton.getSpecial()){
                addSpecial();
            }
        }
        if(e.getSource() == use){
            setUsingSpecial(true);
            if(--amountOfSpecial == 0){
                inGameMenuBar.remove(special);
                inGameMenuBar.revalidate();
                inGameMenuBar.repaint();
            }
        }
        if(e.getSource() == buttonOk){
            specialFrame.setVisible(false);
        }
        if(e.getSource() == saveItem){
            Saving saving = new Saving(this);
            try {
                saving.save();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * A MineSweeperFrame által visszaadott grid gombjaihoz hozzáadja az osztály által implementált ActionListenert.
     */
    public void actionListenerForTheButtons(){
        PolygonButton[][] polygonButtons = msf.getButtonGrid();
        for(PolygonButton[] buttons : polygonButtons){
            for(PolygonButton button : buttons){
                button.addActionListener(this);
            }
        }
    }

    /**
     * Beállítja a gombok számára, hogy most éppen speciális mező által nyújtott segítség felhasználása van vagy sem.
     *
     * @param using boolean
     */
    public void setUsingSpecial(boolean using){
        for(PolygonButton[] polygonButtons : msf.getButtonGrid()){
            for(PolygonButton polygonButton : polygonButtons){
                polygonButton.setUsingSpecial(using);
            }
        }
    }

    /**
     * A paraméterül kapott gombon végrehajtja a szükséges módosításokat, ha éppen felhasználás alatt áll a speciális segítség.
     *
     * @param polygonButton PolygonButton
     */
    public void usingSpecialLogic(PolygonButton polygonButton){
        polygonButton.setProcessed(true);
        if(polygonButton.hasBomb()){
            polygonButton.setFlagged(true);
        }else{
            int bombs = 0;
            for(PolygonButton button : msf.getNmg().getNeighbours(polygonButton)){
                if(button.hasBomb()){
                    bombs++;
                }
            }
            polygonButton.setText(String.valueOf(bombs));
            polygonButton.setEnabled(false);
        }
    }

    /**
     * A speciális mezőre kattintáskor megjelenő kis ablakot hozza létre, kezeli.
     */
    public void specialFrame(){
        specialFrame = new JFrame();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        specialFrame .setLocation(dim.width/2-specialFrame .getSize().width/2, dim.height/2-specialFrame .getSize().height/2);
        JLabel text = new JLabel("Megtaláltál egy speciális mezőt! (most vagy korábban)");
        JLabel amount = new JLabel("Felhasználható speciális mezők száma : " + amountOfSpecial);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        amount.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonOk = new JButton("OK");
        buttonOk.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonOk.addActionListener(this);

        JPanel specialPanel = new JPanel();
        specialPanel.setLayout(new BoxLayout(specialPanel,BoxLayout.Y_AXIS));

        specialPanel.add(text);
        specialPanel.add(amount);
        specialPanel.add(buttonOk);

        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(specialPanel,BorderLayout.CENTER);
        specialFrame.add(jpanel);
        specialFrame.setSize(350,100);
        specialFrame.setVisible(true);
    }

    /**
     * Hozzáadja a menühöz a speciális mező menüpontot azon esetben ha éppen van felhasználható speciális segítség.
     */
    public void addSpecial(){
        inGameMenuBar.add(special);
        inGameMenuBar.revalidate();
        inGameMenuBar.repaint();
        amountOfSpecial++;
        specialFrame();
    }

    /**
     * Visszaadja az időt szövegként.
     *
     * @return String
     */
    public String getTime(){
        return this.time;
    }

    /**
     * Visszaadja a MineSweepepFrame objektumot
     *
     * @return MineSweeperFrame
     */
    public MineSweeperFrame getMsf(){return  this.msf;}

    /**
     * Vissza ad egy HelpContainer heterogén kollekciót.
     *
     * @return HelpContainer list
     */
    public List<HelpContainer> getHelpContainer(){return  this.helpContainer;}

    /**
     * Visszaadja, hogy jelenleg hány speciális mező áll a felhasználó rendelkezésére.
     *
     * @return int
     */
    public int getAmountOfSpecial(){return amountOfSpecial;}

    /**
     * Visszaadja a pontos eltelt időt long típusban.
     *
     * @return long
     */
    public long getTimerStateSeconds(){return this.timerState.seconds;}

    /**
     * Beállítja a long típusú időt.
     *
     * @param time long
     */
    public void setTimerState(long time){this.timerState.seconds = time;}

    /**
     * Beállítja a gridben szereplő speciális mezők számát.
     *
     * @param specials int
     */
    public void setAmountOfSpecial(int specials){
        this.amountOfSpecial = specials;
        if(specials > 0){
            this.amountOfSpecial--;
            addSpecial();
        }
    }
}
