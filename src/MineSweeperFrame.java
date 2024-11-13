import javax.swing.*;
import java.awt.*;

/**
 * A játék mező létrehozásért felelős osztály
 */
public class MineSweeperFrame extends JPanel {
    private final int vertices;
    private final int size;
    private final int width;
    private final int height;
    PolygonButton[][] buttonGrid;
    private final int font;
    private final transient int specials;
    private transient NeighbourManager nmg;
    private transient MenuManager menuManager;

    /**
     * Inicializálja a tagokat és elindítja a mezők legenerálását.
     *
     * @param vertices int
     * @param size int
     * @param width int
     * @param height int
     * @param font int
     * @param specials int
     */
    public MineSweeperFrame(int vertices, int size, int width, int height, int font, int specials){
        this.vertices = vertices;
        this.size = size;
        this.width = width;
        this.height = height;
        this.font = font;
        this.specials = specials;
        startGame();
    }

    /**
     * Beállítja a méreteket, létrehozza a gridet és elhelyezi benne a bombákat.
     */
    public void startGame() {
        setSize(new Dimension(width,height));
        setLayout(null);
        GridCreator gc = null;
        menuManager = new MenuManager();
        switch (vertices){
            case 3:
                gc = new TriangleGrid(vertices, size, width, height, font, menuManager);
                break;
            case 4:
                gc = new SquareGrid(vertices, size, width, height, font, menuManager);
                break;
            case 6:
                gc = new HexagonGrid(vertices, size, width, height, font, menuManager);
                break;
            default:
                break;
        }
        if(gc != null){
            gc.populateButtonGrid();
            buttonGrid = gc.getGrid();
            add(gc);
        }
        nmg = new NeighbourManager(gc.getGrid(),gc.getVertices());
        BombPlacer bombPlacer = new BombPlacer(gc.getGrid(),gc.rows,gc.cols, specials,nmg);
        bombPlacer.placeThem();
        JFrame frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setSize(2500, 1400);
    }

    /**
     * Visszaadja a csúcsok számát.
     *
     * @return int
     */
    public int getVertices(){
        return vertices;
    }

    /**
     * Visszaadja a PolygonButton gridet
     *
     * @return PolygonButton[][]
     */
    public PolygonButton[][] getButtonGrid(){
        return buttonGrid;
    }

    /**
     * Visszaadja a NeighbourManagert
     *
     * @return NeighbourManager
     */
    public NeighbourManager getNmg(){
        return this.nmg;
    }

    /**
     * Visszaadja a MenuManager-t.
     *
     * @return MenuManager
     */
    public MenuManager getMenuManager(){return this.menuManager;}

    /**
     * Visszaadja a mező méretét.
     *
     * @return int
     */
    public int getTileSize(){return this.size;}

    /**
     * Visszaadja a mező szélességét.
     *
     * @return int
     */
    public int getTileWidth(){return this.width;}

    /**
     * Visszaadja a mező magasságát.
     *
     * @return int
     */
    public int getTileHeight(){return  this.height;}

    /**
     * Visszaadja a mmező betűméretét.
     *
     * @return int
     */
    public int getFontSize() {return font;}

    /**
     * Visszaadja a speciális mezők számát.
     *
     * @return int
     */
    public int getSpecials() {return specials;}

    /**
     * Beállítja a PolygonButton gridet.
     *
     * @param newGrid PolygonButton[][]
     */
    public void setButtonGrid(PolygonButton[][] newGrid){this.buttonGrid = newGrid;}
}
