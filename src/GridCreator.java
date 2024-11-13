import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * Absztrakt osztály, mely a különböző alakzatok ősosztálya
 */
public abstract class GridCreator extends JPanel {
    protected final int size;
    protected final int fieldWidth;
    protected final int fieldHeight;
    protected final int vertices;
    protected int rows;
    protected int cols;
    protected PolygonButton[][] buttonGrid;
    private final int font;
    private final transient  MenuManager menuManager;

    /**
     * A függvény visszaad egy olyan MouseListenert, amely hozzáfér egy adott gomb szomszédaihoz és a menükhöz
     *
     * @param neighbourManager NeighbourManager objektum
     * @param menuManager MenuManager objektum
     * @return MouseListener objektum
     */
    protected abstract MouseListener createPolygonButtonMouseListener(NeighbourManager neighbourManager, MenuManager menuManager);

    /**
     * A GridCreator konstruktora, mely inicializál
     *
     * @param vertices int
     * @param size int
     * @param width int
     * @param height int
     * @param font int
     * @param menuManager MenuManager
     */
    protected GridCreator(int vertices, int size, int width, int height, int font, MenuManager menuManager){
        this.menuManager = menuManager;
        this.size = size;
        this.fieldWidth = width;
        this.fieldHeight = height;
        this.vertices = vertices;
        this.rows = fieldHeight/size;
        this.cols = fieldWidth/size;
        this.font = font;
        buttonGrid = new PolygonButton[rows][cols];
        setLayout(null);
        setSize(new Dimension(fieldWidth+20,fieldHeight+20));
    }

    /**
     * A sorok és oszlopok alapján módosítja a grid 2 dimenziós tömböt
     */
    protected void create2DArray(){
        buttonGrid = new PolygonButton[rows][cols];
    }

    /**
     * Absztrakt osztály
     */
    protected abstract void populateButtonGrid();

    /**
     * Létrehoz egy PolygonButton objektumot a kért alakzat szerint
     *
     * @param vertices int
     * @param rectangleX int
     * @param rectangleY int
     * @param rotate double
     * @return PolygonButton
     */
    protected PolygonButton createButtonForShape(int vertices, int rectangleX, int rectangleY, double rotate){
        NeighbourManager neighbourManager = new NeighbourManager(buttonGrid, vertices);
        PolygonButtonMouseListener buttonMouseListener = new PolygonButtonMouseListener(neighbourManager, menuManager);
        return new PolygonButton(vertices,rectangleX,rectangleY,rotate, buttonMouseListener, font);
    }

    /**
     * Módosítja a sorokat
     *
     * @param newRow int
     */
    protected void setRows(int newRow){
        this.rows = newRow;
    }

    /**
     * Módosítja az oszlopokat
     *
     * @param newCol int
     */
    protected void setCols(int newCol){
        this.cols = newCol;
    }

    /**
     * Visszaadja a sorok számát
     *
     * @return int
     */
    public int getRows(){
        return rows;
    }

    /**
     * Visszaadja az oszlopok számát
     *
     * @return int
     */
    public int getCols(){
        return cols;
    }

    /**
     * Visszaadja a PolygonButton gridet
     * @return PolygonButton[][]
     */
    public PolygonButton[][] getGrid(){
        return buttonGrid;
    }

    /**
     * Visszaadja a csúcsok számát
     *
     * @return int
     */
    public int getVertices(){return vertices;}
}
