import javax.swing.*;
import java.util.List;

/**
 * Absztrakt osztály, mely a segítségek ősosztálya
 */
public abstract class HelpContainer {
    protected PolygonButton[][] buttonGrid;
    protected NeighbourManager neighbourManager;

    /**
     * A HelpContainer osztály konstruktora
     *
     * @param buttonGrid PolygonButton[][]
     * @param neighbourManager NeighbourManager
     */
    protected HelpContainer(PolygonButton[][] buttonGrid, NeighbourManager neighbourManager){
        this.buttonGrid = buttonGrid;
        this.neighbourManager = neighbourManager;
    }

    /**
     * Absztrakt tagfüggvény, mely egy JMenuItemet hoz létre
     *
     * @return JMenuItem
     */
    protected abstract JMenuItem menuItem();

    /**
     * A paraméterül kapott listában megszámolja, hogy hány bombát tartalmazó mező van
     *
     * @param listOfNeighbours PolygonButton lista
     * @return int
     */
    protected int amountOfBombs(List<PolygonButton> listOfNeighbours){
        int amountOfBombs = 0;
        for (PolygonButton button: listOfNeighbours){
            if(button.hasBomb()){
                amountOfBombs++;
            }
        }
        return amountOfBombs;
    }
}
