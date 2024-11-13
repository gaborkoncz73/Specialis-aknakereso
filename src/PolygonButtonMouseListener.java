import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A griden belüli mezőkra kattintásokért felelős osztály.
 */
public class PolygonButtonMouseListener implements MouseListener {
    private final NeighbourManager neighbourManager;
    private final MenuManager menuManager;
    private JButton backToMenu;
    private JFrame lost;
    private int allSquares;
    private JButton toHOF;
    private JTextField nameInField;

    /**
     * A PolygonButtonMouseListener osztály konstruktora.
     *
     * @param neighbourManager NeigbourManager
     * @param menuManager MenuManager
     */
    public PolygonButtonMouseListener(NeighbourManager neighbourManager, MenuManager menuManager) {
        this.menuManager = menuManager;
        this.neighbourManager = neighbourManager;
        int rows = neighbourManager.grid.length;
        int cols = neighbourManager.grid[0].length;
        allSquares = rows* cols;
    }

    /**
     * Különböző állapotú gombokra történő kattintásokkor végrehajtandó logigákat kezelő osztály.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Object source =  e.getSource();
        if (source instanceof PolygonButton polygonButton && !polygonButton.getUsingSpecial()) {
            if (SwingUtilities.isLeftMouseButton(e)&&!polygonButton.getFlagged()) {
                if(polygonButton.hasBomb()){
                    finished("Vesztettél!");
                    polygonButton.setBackground(Color.black);
                    return;
                }
                List<PolygonButton> listOfNeighbours = neighbourManager.getNeighbours(polygonButton);

                int surroundingBombs = amountOfBombs(listOfNeighbours);
                if(surroundingBombs == 0){
                    firstHasNoNeighbourBomb(polygonButton);
                }else{
                    polygonButton.setText(String.valueOf(surroundingBombs));
                    polygonButton.setEnabled(false);
                    polygonButton.setProcessed(true);
                }
            } else if (SwingUtilities.isRightMouseButton(e) && polygonButton.isEnabled()) {
                if(!polygonButton.getFlagged()){
                    polygonButton.setFlagged(true);
                    if(polygonButton.hasBomb()){
                        polygonButton.setProcessed(true);
                    }
                }else{
                    polygonButton.setBackground(Color.white);
                    polygonButton.setProcessed(false);
                    polygonButton.setFlagged(false);
                }

            }


        }else if(source == backToMenu){
            lost.setVisible(false);
            allSquares = 0;
            new Menu();
        }
        if(source instanceof PolygonButton polygonButton && polygonButton.getUsingSpecial()){
            setUsingSpecial(false);
        }
        if(allSquares == processed() && allSquares != 0 && source != toHOF){
            finished("Nyertél!");
        }
        if(source == toHOF){
            try {
                addHOF();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * A további osztályoknak nincsen törzsük, mert ezeket a Mouse actionokat nem használja a kód.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Ha olyan gombra nyom rá a felhasználó, amely nem tartalmaz körülötte egy aknát sem akkor rekurzívan felfedezi a z összes olyan mezőt, amely körül továbbra sincs bomba mind addig amíg el nem jut egy olyanig ahol van bomba és ott megáll.
     *
     * @param clickedButton JButton
     */
    private void firstHasNoNeighbourBomb(JButton clickedButton) {
        PolygonButton polygonButton = (PolygonButton) clickedButton;
        if(!polygonButton.isEnabled()){
            return;
        }
        if(!polygonButton.getSpecial()){
            polygonButton.setEnabled(false);
            polygonButton.setProcessed(true);
        }
        List<PolygonButton> listOfNeighbours = neighbourManager.getNeighbours(polygonButton);
        int bombs = amountOfBombs(listOfNeighbours);
        if (bombs == 0) {
            List<PolygonButton> copyOfNeighbours = new ArrayList<>(listOfNeighbours);
            for (PolygonButton button : copyOfNeighbours) {
                if(button.isEnabled()){
                    if(!polygonButton.getSpecial())
                        button.setText("");
                    firstHasNoNeighbourBomb(button);
                }
            }
        } else {
            if(!polygonButton.getSpecial())
                polygonButton.setText(String.valueOf(bombs));
        }
    }

    /**
     * Visszaadja a listában szereplő bombák számát
     *
     * @param listOfNeighbours PolygonButton list
     * @return int
     */
    public int amountOfBombs(List<PolygonButton> listOfNeighbours){
        int amountOfBombs = 0;
        for (PolygonButton button: listOfNeighbours){
            if(button.hasBomb()){
                amountOfBombs++;
            }
        }
        return amountOfBombs;
    }

    /**
     * Beállítja a gomboknak, hogy éppen használatban van-e a speciális segítség vagy sem.
     *
     * @param using boolean
     */
    public void setUsingSpecial(boolean using){
        for(PolygonButton[] polygonButtons : neighbourManager.grid){
            for(PolygonButton polygonButton : polygonButtons){
                polygonButton.setUsingSpecial(using);
            }
        }
    }

    /**
     * A megadott String paraméter utalhat győzelemre vagy vereségre és annak emgfelelően létrehozza a játék vége ablakot ahonnan vissza lehet menni a főoldalra.
     * Ha nyert akkor megadhatja a felhasználó a nevét is és betöltheti a dicsőségtáblába.
     *
     * @param text String
     */
    public void finished(String text){
        lost = new JFrame();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        lost.setLocation(dim.width/2-lost.getSize().width/2, dim.height/2-lost.getSize().height/2);
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        backToMenu = new JButton("Vissza a főoldalra!");
        JPanel backPanel = new JPanel(new BorderLayout());
        backPanel.add(backToMenu,BorderLayout.CENTER);
        backToMenu.addMouseListener(this);

        JPanel jpanel = new JPanel(new BorderLayout());
        jpanel.add(label,BorderLayout.CENTER);
        jpanel.add(backPanel,BorderLayout.SOUTH);
        lost.add(jpanel);
        lost.setSize(200,200);
        lost.setVisible(true);
        menuManager.inGameMenu.setVisible(false);
        if(text.equals("Nyertél!")){
            JLabel nameText = new JLabel("Add meg a nevedet:");
            nameText.setHorizontalAlignment(SwingConstants.CENTER);
            toHOF = new JButton("Dicsőségtáblába");
            toHOF.addMouseListener(this);
            nameInField = new JTextField(20);
            JPanel hallOfFame = new JPanel(new BorderLayout());
            hallOfFame.add(nameText,BorderLayout.NORTH);
            hallOfFame.add(nameInField,BorderLayout.CENTER);
            hallOfFame.add(toHOF,BorderLayout.SOUTH);
            backPanel.add(hallOfFame,BorderLayout.NORTH);
        }
    }

    /**
     * Visszaadja a feldolgozott mezők számát
     *
     * @return int
     */
    public int processed(){
        int amount = 0;
        for(PolygonButton[] polygonButtons : neighbourManager.grid){
            for(PolygonButton polygonButton : polygonButtons){
                if(polygonButton.isProcessed()){
                    amount++;
                }
            }
        }
        return amount;
    }

    /**
     * Beleírja a nyertes felhasználó által megadott nevet és idejét a hof.txt fájlba.
     *
     * @throws IOException
     */
    public void addHOF() throws IOException {
        String file = "hof.txt";
        FileWriter fileWriter = new FileWriter(file,true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(nameInField.getText() + " " + menuManager.inGameMenu.getTime() + "\n");
        bufferedWriter.close();
    }
}
