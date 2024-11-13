import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A felfedett mezők körül random bomba segítségért felelős osztály
 */
public class SurroundingBomb extends HelpContainer implements ActionListener {
    JMenuItem jMenuItem;

    /**
     * A SurroundingBomb osztály konstruktora
     *
     * @param buttonGrid PolygonButton[][]
     * @param neighbourManager NeighbourManager
     */
    protected SurroundingBomb(PolygonButton[][] buttonGrid, NeighbourManager neighbourManager) {
        super(buttonGrid, neighbourManager);
    }
    /**
     * Létrehozza a megfelelő segítség névvel a JMenuItemet és hozzáadja az ActionListenert.
     *
     * @return JMenuItem
     */
    @Override
    public JMenuItem menuItem(){
        jMenuItem = new JMenuItem("Random akna a felfedettek körül");
        jMenuItem.addActionListener(this);
        return jMenuItem;
    }
    /**
     * Ha igénybe veszik a segítséget meghívja a exploreMine függvényt.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jMenuItem){
            exploreMine();
        }
    }

    /**
     * Feltölt egy listát olya PolygonButton-okkal, amelyek fel vannak feldve és körülöttük biztos van akna
     * Majd ezek közül vesz egyet random és végig megy a szomszédain és keres egy bombát, amit megjelöl
     * Ha nem talált körülötte felfedetlen bombát kiszedi a listából és újra csinálja, maximum addig, amíg a lista nem üres
     */
    private void exploreMine() {
        List<PolygonButton> exploreds = new ArrayList<>();
        for (PolygonButton[] polygonButtons : buttonGrid) {
            for (PolygonButton polygonButton : polygonButtons) {
                if (!polygonButton.isEnabled() && 0 < amountOfBombs(neighbourManager.getNeighbours(polygonButton))) {
                    exploreds.add(polygonButton);
                }
            }
        }
        Random rnd = new Random();
        boolean flag = false;
        while(!flag && !exploreds.isEmpty()){
            int randomIndex = rnd.nextInt(exploreds.size());
            List<PolygonButton> list = neighbourManager.getNeighbours(exploreds.get(randomIndex));
            for(PolygonButton button : list){
                if(!button.getFlagged() && button.hasBomb()){
                    flag = true;
                    button.setFlagged(true);
                    button.setProcessed(true);
                    break;
                }
            }
            exploreds.remove(randomIndex);
        }
    }

}
