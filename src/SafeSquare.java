import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A biztonságos mező segítségért felelős osztály
 */
public class SafeSquare extends HelpContainer implements ActionListener {
    JMenuItem jMenuItem;

    /**
     * A SafeSquare osztály konstruktora
     *
     * @param buttonGrid PolygonButton[][]
     * @param neighbourManager NeighbourManager
     */
    protected SafeSquare(PolygonButton[][] buttonGrid, NeighbourManager neighbourManager) {
        super(buttonGrid, neighbourManager);
    }
    /**
     * Létrehozza a megfelelő segítség névvel a JMenuItemet és hozzáadja az ActionListenert.
     *
     * @return JMenuItem
     */
    @Override
    public JMenuItem menuItem(){
        jMenuItem = new JMenuItem("Random biztonságos mező");
        jMenuItem.addActionListener(this);
        return jMenuItem;
    }

    /**
     * Ha igénybe veszik a segítséget meghívja a randomSafeTile függvényt.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jMenuItem){
            randomSafeTile();
        }
    }

    /**
     * Beletölti egy listába az összes biztonságos gombot: engedélyezve van, nem bomba, nem speciális mező, van akna körtülötte
     * Végül ezek közül vesz egy random mezőt
     */
    private void randomSafeTile() {
        List<PolygonButton> safeButtons = new ArrayList<>();
        for (PolygonButton[] buttons : buttonGrid){
            for(PolygonButton polygonButton : buttons){
                if(polygonButton.isEnabled() && !polygonButton.hasBomb() && !polygonButton.getSpecial() && 0 < amountOfBombs(neighbourManager.getNeighbours(polygonButton))){
                    safeButtons.add(polygonButton);
                }
            }
        }
        Random rnd = new Random();
        if(!safeButtons.isEmpty()){
            int randomIndex = rnd.nextInt(safeButtons.size());
            safeButtons.get(randomIndex).setText(String.valueOf(amountOfBombs(neighbourManager.getNeighbours(safeButtons.get(randomIndex)))));
            safeButtons.get(randomIndex).setEnabled(false);
            safeButtons.get(randomIndex).setProcessed(true);
        }
    }
}
