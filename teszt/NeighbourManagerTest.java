import org.junit.Test;

import static org.junit.Assert.*;

public class NeighbourManagerTest {

    @Test
    public void addIfValid() {
        MenuManager menuManager = new MenuManager();
        GridCreator gc = new HexagonGrid(3,40,40*26,10*40,10, menuManager);
        gc.populateButtonGrid();
        NeighbourManager neighbourManager = new NeighbourManager(gc.getGrid(),3);
        neighbourManager.addIfValid(-5,2);
        neighbourManager.addIfValid(0,-2);
        neighbourManager.addIfValid(-5,-2);
        neighbourManager.addIfValid(0,0);
        neighbourManager.addIfValid(2,1);
        neighbourManager.addIfValid(1,0);
        neighbourManager.addIfValid(0,2);
        assertEquals(4,neighbourManager.getList().size());
    }
}