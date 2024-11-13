import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A fájlba mentésért felelős osztály
 */
public class Saving {
    InGameMenu inGameMenu;
    MineSweeperFrame msf;

    /**
     * A Saving class konstruktora
     *
     * @param inGameMenu InGameMenu
     */
    public Saving(InGameMenu inGameMenu){
        this.inGameMenu = inGameMenu;
        this.msf = inGameMenu.getMsf();
    }

    /**
     * Belementi a saved.txt-be
     * Első sorba a MineSweeperFrame attribútumait
     * Második sroban a segítésgek számát, majd annak megfelelő mennyiségű sorban a segítségeket
     * Utána hogy hány segítség áll rendelkezésre
     * Majd hogy mennyi időnél tart
     * Végül pediga teljes gridet mátrixosan úgy hogy különböző állapotokat különböztet meg a mezők között:
     * f: flagged és bomba van ott, e: felfedett, fe: flagged de sima mező van ott, fs: flagged de special mező van ott, b: bomba van ott, s: special mező, t: sima felfedetlen mező
     *
     * @throws IOException
     */
    public void save() throws IOException {
        FileWriter fileWriter = new FileWriter("saved.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(inGameMenu.getMsf().getVertices() + " " + inGameMenu.getMsf().getTileSize() + " " +
                                 inGameMenu.getMsf().getTileWidth() + " " + inGameMenu.getMsf().getTileHeight() + " " +
                                 inGameMenu.getMsf().getFontSize() + " " + inGameMenu.getMsf().getSpecials() + "\n");
        bufferedWriter.write(inGameMenu.getHelpContainer().size() + "\n");
        for(HelpContainer asd : inGameMenu.getHelpContainer()){
            bufferedWriter.write(asd.getClass() + "\n");
        }
        bufferedWriter.write(Integer.toString(inGameMenu.getAmountOfSpecial())+ "\n");
        bufferedWriter.write(inGameMenu.getTimerStateSeconds()+ "\n");
        for(PolygonButton[] polygonButtons : msf.getButtonGrid()){
            for(PolygonButton polygonButton : polygonButtons){
                if(polygonButton.isProcessed() && polygonButton.hasBomb()){
                    bufferedWriter.write("f ");
                }else if(polygonButton.isProcessed() && !polygonButton.hasBomb()){
                    bufferedWriter.write("e ");
                }else if(!polygonButton.isProcessed() && polygonButton.getFlagged()){
                    bufferedWriter.write("fe ");
                }else if(!polygonButton.isProcessed() && polygonButton.getFlagged() &&polygonButton.getSpecial()){
                    bufferedWriter.write("fs ");
                }else if(!polygonButton.isProcessed() && polygonButton.hasBomb()){
                    bufferedWriter.write("b ");
                }else if(!polygonButton.isProcessed() && polygonButton.getSpecial()){
                    bufferedWriter.write("s ");
                }
                else{
                    bufferedWriter.write("t ");
                }
            }
            bufferedWriter.write("\n");
        }
        bufferedWriter.close();
    }
}
