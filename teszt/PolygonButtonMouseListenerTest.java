import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PolygonButtonMouseListenerTest {

    MenuManager menuManager;
    GridCreator gc;
    NeighbourManager neighbourManager;
    PolygonButtonMouseListener polygonButtonMouseListener;
    BombPlacer bombPlacer;
    List<PolygonButton> list;
    @Before
    public void setup(){
        menuManager = new MenuManager();
        gc = new HexagonGrid(6,40,40*30,16*40,10, menuManager);
        gc.populateButtonGrid();
        neighbourManager = new NeighbourManager(gc.getGrid(), gc.getVertices());
        polygonButtonMouseListener = new PolygonButtonMouseListener(neighbourManager,menuManager);
        bombPlacer = new BombPlacer(gc.getGrid(),16,40,1,neighbourManager);
        list = new ArrayList<>();
        for(PolygonButton[] polygonButtons : gc.getGrid()){
            list.addAll(Arrays.asList(polygonButtons));
        }
    }

    @Test
    public void processed() {
        gc.getGrid()[10][5].setProcessed(true);
        gc.getGrid()[5][8].setProcessed(true);
        gc.getGrid()[1][15].setProcessed(true);
        gc.getGrid()[14][29].setProcessed(true);
        assertEquals(4,polygonButtonMouseListener.processed());
    }

    @Test
    public void amountOfBombs(){
        int necessaryBombs = 16 * 40 / 8;
        bombPlacer.placeThem();
        assertEquals(necessaryBombs,polygonButtonMouseListener.amountOfBombs(list));
    }

    @Test
    public void setUsingSpecial(){
        polygonButtonMouseListener.setUsingSpecial(true);
        boolean result = false;
        for(PolygonButton[] polygonButtons : gc.getGrid()){
            for(PolygonButton polygonButton : polygonButtons){
                if(!polygonButton.getUsingSpecial()){
                    result = true;
                }
            }
        }
        assertFalse(result);
    }
}