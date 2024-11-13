import java.awt.event.MouseListener;

/**
 * GridCreator osztályból származik és a hatszögek létrehozásáért felelős.
 */
public class HexagonGrid extends GridCreator{
    /**
     * A HexagonGrid konstruktora, amely használkja az ősosztály konstruktorát.
     * @param vertices int
     * @param size int
     * @param width int
     * @param height int
     * @param font int
     * @param menuManager MenuManager
     */
    protected HexagonGrid(int vertices, int size, int width, int height, int font, MenuManager menuManager) {
        super(vertices, size, width, height, font, menuManager);
    }

    /**
     * Feltölti az ősosztály grid 2 dimenziós tömbjét PolygonButton objektumokkal, amelyek a megadott adatoknak megfelelően, a megfelelő helyeken helyezkednek el.
     */
    @Override
    protected void populateButtonGrid(){
        int rectangleX = size;
        int rectangleY = size;
        int rowCounter = 0;
        int stepY = (int) (rectangleY * 0.875);
        for (int middleY = stepY/2; rowCounter<rows;rowCounter++, middleY += stepY) {
            int colCounter=0;
            for (int middleX = rectangleX; colCounter<cols; middleX += (int)(rectangleX*1.5)) {
                for(int tempX = middleX, tempY = middleY, i = 0;colCounter < cols && i < 2; i++, tempX += (int) (rectangleX*0.75), tempY += (int) (stepY*0.5)){
                    buttonGrid[rowCounter][colCounter] = super.createButtonForShape(vertices, rectangleX,rectangleY,0);
                    buttonGrid[rowCounter][colCounter].setButton(buttonGrid[rowCounter][colCounter], tempX - (rectangleX/2), tempY - (rectangleY/2), rectangleX, rectangleY);
                    add(buttonGrid[rowCounter][colCounter]);
                    colCounter++;
                }
            }
        }
    }

    /**
     * A paraméterekből létrehoz egy új PolygonButtonMouseListener-t.
     *
     * @param neighbourManager NeighbourManager objektum
     * @param menuManager MenuManager objektum
     * @return PolygonButtonMouseListener objektum
     */
    @Override
    protected MouseListener createPolygonButtonMouseListener(NeighbourManager neighbourManager, MenuManager menuManager) {
        return new PolygonButtonMouseListener(neighbourManager, menuManager);
    }
}
