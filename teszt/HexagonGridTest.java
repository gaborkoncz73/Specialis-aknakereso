import org.junit.Test;

import static org.junit.Assert.*;

public class HexagonGridTest {

    @Test
    public void populateButtonGrid() {
        MenuManager menuManager = new MenuManager();
        HexagonGrid hexagonGrid = new HexagonGrid(6,40,40*30,40*16,10, menuManager);
        hexagonGrid.populateButtonGrid();
        assertTrue(hexagonGrid.getGrid().length == 16 && hexagonGrid.getGrid()[0].length == 30);
    }
}