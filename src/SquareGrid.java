import java.awt.event.MouseListener;

/**
 * A GridCreator osztályból származik le és a négyzet grid kialakításáért felelős
 */
public class SquareGrid extends GridCreator{
    /**
     * A SquareGrid osztály konstruktora
     *
     * @param vertices int
     * @param size int
     * @param width int
     * @param height int
     * @param font int
     * @param menuManager int
     */
    protected SquareGrid(int vertices, int size, int width, int height, int font, MenuManager menuManager) {
        super(vertices, size, width, height, font, menuManager);
    }
    /**
     * Feltölti az ősosztály grid 2 dimenziós tömbjét PolygonButton objektumokkal, amelyek a megadott adatoknak megfelelően, a megfelelő helyeken helyezkednek el.
     */
    @Override
    protected void populateButtonGrid() {
        int rectangleX=(int)(size*0.8);
        int rectangleY =(int)(size*0.8);
        int rowCounter=0;
        for (int middleY = rectangleY/2; rowCounter < rows; rowCounter++,middleY += rectangleY) {
            int colCounter = 0;
            for (int middleX = rectangleX/2; colCounter < cols;colCounter++, middleX += rectangleX) {
                buttonGrid[rowCounter][colCounter] = super.createButtonForShape(vertices, (int)(rectangleX*Math.sqrt(2)),(int)(rectangleY*Math.sqrt(2)),Math.PI/4);
                buttonGrid[rowCounter][colCounter].setButton(buttonGrid[rowCounter][colCounter], middleX - (rectangleX/2), middleY - (rectangleY/2), (int)(rectangleX*Math.sqrt(2)), (int)(rectangleY*Math.sqrt(2)));
                add(buttonGrid[rowCounter][colCounter]);
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
