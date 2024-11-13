import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Egy gomb szomszédainak a meghatározásáért felelős osztály.
 */
public class NeighbourManager {
    PolygonButton[][] grid;
    private final int vertices;
    List<PolygonButton> list;

    /**
     * A NeighbourManager osztály konstruktora, amely inicializálja a tagokat.
     *
     * @param grid PolygonButton[][]
     * @param vertices int
     */
    public NeighbourManager(PolygonButton[][] grid, int vertices){
        this.vertices = vertices;
        this.grid = grid;
        list = new ArrayList<>();
    }

    /**
     * Hozzáadja a listához az adott mezőt, ha az érvényes
     *
     * @param row int
     * @param col int
     */
    public void addIfValid(int row, int col){
        if(row >= 0 && row < grid.length && col >= 0 && col < grid[row].length){
            list.add(grid[row][col]);
        }
    }

    /**
     * Kap paraméterül egy PolygonButton típusú gombot és visszaadja a szomszédait figyelembe véve a helyét és a gomb alakját. Egy PolygonButton listában adja vissza a szomszédokat.
     *
     * @param button PolygonButton
     * @return PolygonButton lista
     */
    public List<PolygonButton> getNeighbours(PolygonButton button){
        list.clear();
        int row = 0, col=0;
        loop:
        for(row = 0; row < grid.length; row++){
            for(col = 0; col < grid[row].length; col++){
                if(grid[row][col] == button){
                    break loop;
                }
            }
        }
        switch (vertices){
            case 4:
                for(int i = -1;i<2;i++){
                    for(int j = -1; j < 2; j++){
                        if(!(i==0&&j==0))
                            addIfValid(row+i,col+j);
                    }
                }
                break;
            case 6:
                addIfValid(row-1,col);
                addIfValid(row,col-1);
                addIfValid(row,col+1);
                addIfValid(row+1,col);
                int shift = (col%2==1) ? 1 : -1;
                addIfValid(row+shift,col-1);
                addIfValid(row+shift,col+1);
                break;
            case 3:
                addIfValid(row-1,col);
                addIfValid(row+1,col);
                for(int i = - 2; i < 3;i++){
                    if(i!=0)
                        addIfValid(row,col+i);
                }
                if(col%4==1 || col% 4 == 2){
                    for(int i = - 2; i < 3;i++){
                        if(i!=0)
                            addIfValid(row+1,col+i);
                    }
                    if(col%4==1){
                        addIfValid(row,col-3);
                        addIfValid(row-1,col+1);
                    }
                    else {
                        addIfValid(row,col+3);
                        addIfValid(row-1,col-1);
                    }
                }else{
                    for(int i = - 2; i < 3;i++){
                        if(i!=0)
                            addIfValid(row-1,col+i);
                    }
                    if(col%4==0){
                        addIfValid(row,col+3);
                        addIfValid(row+1,col-1);
                    }else{
                        addIfValid(row,col-3);
                        addIfValid(row+1,col+1);
                    }
                }
                break;
            default:
                break;
        }
        return list;
    }

    /**
     * Visszaadja a listát.
     *
     * @return PolygonButton list
     */
    public List<PolygonButton> getList(){return this.list;}
}
