import java.util.List;
import java.util.Random;

/**
 * A bombák és a speciális mezők kiosztásáért felelős osztály.
 */
public class BombPlacer {

    private final PolygonButton[][] grid;
    private final int specials;
    private final NeighbourManager nmg;
    private final int bombs;

    /**
     * A BombPlacer osztály konsturktora, amely inicializálja a konstruktor attribútumait és kiszámolja a bombák számát
     *
     * @param grid PolyGonButton typúsú 2 dimenziós tömb
     * @param row int
     * @param col int
     * @param specials int
     * @param nmg NeighbourManager typusú objektum
     */
    public BombPlacer(PolygonButton[][] grid, int row, int col, int specials, NeighbourManager nmg){
        this.grid = grid;
        this.bombs = row * col / 8;
        this.specials = specials;
        this.nmg = nmg;
    }

    /**
     * Ez a függvény a konstruktorban kiszámolt bombák számát random szétszórja a gridben
     */
    public void placeThem (){
        Random random = new Random();
        int bombsPlaced=0;
        while (bombsPlaced < bombs) {
            int randomRow = random.nextInt(grid.length);
            int randomCol = random.nextInt(grid[randomRow].length);

            if (!grid[randomRow][randomCol].hasBomb()) {
                grid[randomRow][randomCol].setBomb(true);
                bombsPlaced++;
            }
        }
        int specialPlaced = 0;
        while (specialPlaced < specials) {
            int randomRow = random.nextInt(grid.length);
            int randomCol = random.nextInt(grid[randomRow].length);

            if (!grid[randomRow][randomCol].hasBomb() && anyBomb(nmg.getNeighbours(grid[randomRow][randomCol]))) {
                grid[randomRow][randomCol].setSpecial(true);
                specialPlaced++;
            }
        }
    }

    /**
     * A függvény kap egy PolygonButton típusú listát és visszaadja, hohgy van-e olyan mező közöttük, amely tartal,maz bombát.
     *
     * @param list PolygonButton típusú lista
     * @return egy boolean érték
     */
    public boolean anyBomb(List<PolygonButton> list){
        for(PolygonButton button : list){
            if(button.hasBomb()){
                return true;
            }
        }
        return false;
    }
}
