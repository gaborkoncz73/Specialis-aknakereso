import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeFalse;

public class BombPlacerTest {
    MenuManager menuManager;
    GridCreator gc;
    NeighbourManager neighbourManager;
    BombPlacer bombPlacer;

    @Before
    public void setup(){
        menuManager = new MenuManager();
        gc = new HexagonGrid(6,40,40*30,16*40,10, menuManager);
        gc.populateButtonGrid();
        neighbourManager = new NeighbourManager(gc.getGrid(),6);
        bombPlacer = new BombPlacer(gc.getGrid(), 16,30,1,neighbourManager);
    }
    @Test
    public void placeThem() {
        int necessaryBombs = 16 * 30 / 8;
        bombPlacer.placeThem();
        int newBombs = 0;
        for(PolygonButton[] polygonButtons : gc.getGrid()){
            for (PolygonButton polygonButton : polygonButtons){
                if(polygonButton.hasBomb()){
                    newBombs++;
                }
            }
        }
        assertEquals(necessaryBombs,newBombs);
    }

    @Test
    public void anyBomb() {
        List<PolygonButton> list = new ArrayList<>();
        for(PolygonButton[] polygonButtons : gc.getGrid()){
            list.addAll(Arrays.asList(polygonButtons));
        }
        assumeFalse(bombPlacer.anyBomb(list));
    }
}