import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A potenciális bombák megmutatása segítségért felelős osztály.
 * A HelpContainerből származik le.
 */
public class PotentialBomb extends HelpContainer implements ActionListener {
    JMenuItem jMenuItem;
    boolean helpNeeded = false;
    int[][] disableds;

    /**
     * A potentialBomb osztály konstruktora
     *
     * @param buttonGrid PolygonButton[][]
     * @param neighbourManager NeighbourManager
     */
    protected PotentialBomb(PolygonButton[][] buttonGrid, NeighbourManager neighbourManager) {
        super(buttonGrid, neighbourManager);
        disableds = new int[buttonGrid.length][buttonGrid[0].length];
    }

    /**
     * Létrehozza a megfelelő segítség névvel a JMenuItemet és hozzáadja az ActionListenert.
     *
     * @return JMenuItem
     */
    @Override
    public JMenuItem menuItem(){
        jMenuItem = new JMenuItem("Potenciális aknamezők");
        jMenuItem.addActionListener(this);
        return jMenuItem;
    }

    /**
     * Megnézi hogy kiválasztották-e az e segítséghez tartozó jMenuItemet és ha igen végrehajtja a segítséget
     * Csinál egy listát azokból a gombokból amelyek nincsenek felfedve és nem tartalmaznak bombát
     * Utána ezen listán végigmegy és veszi a lista elemének összes szomszédját
     * És ha a szomszédai között annyi vagy megjelölve bombának ahány bomba van körülötte
     * Akkor kiszedi a listából és a végén ami megmarad azt sárgára színezi mert azok a potenciális bomba mezők
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        List<PolygonButton> potentialBombPlaces = new ArrayList<>();
        if(e.getSource() == jMenuItem){
            addListeners();
        }else if(e.getSource() instanceof PolygonButton polygonButton && helpNeeded){
            List<PolygonButton> list = neighbourManager.getNeighbours(polygonButton);
            for(PolygonButton iteratorButton : list){
                if(iteratorButton.getBackground()!=Color.cyan && !iteratorButton.getFlagged()){
                    potentialBombPlaces.add(iteratorButton);
                }
            }
            Iterator<PolygonButton> iterator = potentialBombPlaces.iterator();
            List<PolygonButton> buttonsToRemove = new ArrayList<>();
            while (iterator.hasNext()) {
                PolygonButton button = iterator.next();
                List<PolygonButton> adjancies = neighbourManager.getNeighbours(button);
                List<PolygonButton> copyOfAdjancies = new ArrayList<>(adjancies);
                for (PolygonButton iteratorButton : copyOfAdjancies){
                    if(iteratorButton.getBackground() == Color.cyan && amountOfBombs( neighbourManager.getNeighbours(iteratorButton)) <= markedBombs(iteratorButton)){
                        buttonsToRemove.add(button);
                    }
                }
            }
            potentialBombPlaces.removeAll(buttonsToRemove);
            if(!potentialBombPlaces.isEmpty()){
                for (PolygonButton goodChanceForBombs : potentialBombPlaces){
                    goodChanceForBombs.setBackground(Color.yellow);
                }
            }
            int i=0;
            for(PolygonButton[] polygonButtons : buttonGrid){
                int j = 0;
                for (PolygonButton pB : polygonButtons){
                    if(disableds[i][j] == 1){
                        pB.setEnabled(false);
                    }
                    j++;
                }
                i++;
            }
            helpNeeded = false;
        }
    }

    /**
     * A buttonGrid összes buttonjához hozzáadja az ActionListenert ami önmgaga a class mert implementálja
     */
    private void addListeners() {
        int i=0;
        for(PolygonButton[] polygonButtons : buttonGrid){
            int j = 0;
            for (PolygonButton polygonButton : polygonButtons){
                polygonButton.addActionListener(this);
                if(!polygonButton.isEnabled()){
                    disableds[i][j] = 1;
                    polygonButton.setEnabled(true);
                }
                j++;
            }
            i++;
        }
        helpNeeded = true;
    }

    /**
     * Visszaadja a paraméterül kapott PolygonButton körül levő megjelölt mezők számát.
     *
     * @param polygonButton PolygonButton
     * @return int
     */
    public int markedBombs(PolygonButton polygonButton){
        List<PolygonButton> list = neighbourManager.getNeighbours(polygonButton);
        int amount =0;
        for(PolygonButton button : list){
            if(button.getFlagged()){
                amount++;
            }
        }
        return amount;
    }
}
