import org.junit.Test;

import static org.junit.Assert.*;

public class PotentialBombTest {
    @Test
    public void markedBombs(){
        MenuManager menuManager = new MenuManager();
        GridCreator gc = new HexagonGrid(4,40,40*10,8*40,10, menuManager);
        gc.populateButtonGrid();
        NeighbourManager neighbourManager = new NeighbourManager(gc.getGrid(),4);
        PotentialBomb potentialBomb = new PotentialBomb(gc.getGrid(),neighbourManager);
        gc.getGrid()[3][4].setFlagged(true);
        gc.getGrid()[4][5].setFlagged(true);
        gc.getGrid()[5][4].setFlagged(true);
        gc.getGrid()[5][5].setFlagged(true);
        assertEquals(4,potentialBomb.markedBombs(gc.getGrid()[4][4]));
    }
}