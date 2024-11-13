import java.awt.*;
import java.awt.event.MouseListener;

/**
 * A GridCreator osztályból származik le és a háromszög grid kialakításáért felelős
 */
public class TriangleGrid extends GridCreator{
    /**
     * A TriangleGrid osztály konstruktora
     *
     * @param vertices int
     * @param size int
     * @param width int
     * @param height int
     * @param font int
     * @param menuManager int
     */
    protected TriangleGrid(int vertices, int size, int width, int height, int font, MenuManager menuManager) {
        super(vertices, size, width, height, font, menuManager);
    }
    /**
     * Feltölti az ősosztály grid 2 dimenziós tömbjét PolygonButton objektumokkal, amelyek a megadott adatoknak megfelelően, a megfelelő helyeken helyezkednek el.
     */
    @Override
    protected void populateButtonGrid() {
        int rectangleX=(int)(size*1.3);
        int rectangleY =(int)(size*1.3);
        setSize(new Dimension((int) (fieldWidth*1.3)+20,(int)(fieldHeight*1.3)+20));
        setRows(this.rows/2);
        setCols(this.cols*2);
        create2DArray();
        int stepY = (int)(rectangleY*0.875);
        int rowCounter=0;
        for (int middleY = stepY/2; rowCounter < rows;rowCounter++, middleY += stepY) {
            int colCounter=0;
            for (int middleX = rectangleX; colCounter<cols; middleX += (int)(rectangleX*1.5)) {
                double r = 0;
                int tempX = middleX;
                for(int i = 0; i < 4 && colCounter<cols;i++,tempX += (int)(rectangleX*0.5), r += Math.PI, colCounter++){
                    int tempY = middleY;
                    if(i == 2 ||i == 1){
                        tempY = middleY+stepY/2;
                    }
                    tempX = (i == 3 || i == 1) ? tempX - (int)(rectangleX * 0.25) : tempX;
                    buttonGrid[rowCounter][colCounter] = super.createButtonForShape(vertices, rectangleX,rectangleY, r);
                    buttonGrid[rowCounter][colCounter].setButton(buttonGrid[rowCounter][colCounter], tempX - (rectangleX/2), tempY - (rectangleY/2), rectangleX, rectangleY);
                    add(buttonGrid[rowCounter][colCounter]);
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
