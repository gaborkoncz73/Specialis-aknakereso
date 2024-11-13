import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Az elmentett állapot betöltéséért felelős osztály.
 */
public class Load {
    /**
     * A Load osztály konstruktora.
     * Beolvassa a saved.txt fájlból a szükséges adatokat, amelyeket feldolgoz és létrehozza a InGameMenu számára szükséges objektumokat, beotölti a grid elmentett állapotát és beállítja a gridben szereplő PolygonButtonok állapotait majd létrehozza a z InGameMenu-t.
     *
     * @throws FileNotFoundException
     */
    public Load() throws FileNotFoundException {
        File file = new File("saved.txt");
        Scanner scanner = new Scanner(file);
        String line = scanner.nextLine();
        String[] msfElements = line.split(" ");
        int[] msfIntElements = new int[msfElements.length];
        for(int i = 0; i < msfElements.length; i++){
            msfIntElements[i] = Integer.parseInt(msfElements[i]);
        }
        MineSweeperFrame msf = new MineSweeperFrame(msfIntElements[0],msfIntElements[1],msfIntElements[2],msfIntElements[3],msfIntElements[4],msfIntElements[5]);

        line = scanner.nextLine();
        List<HelpContainer> helpContainers = new ArrayList<>();
        int helps = Integer.parseInt(line);
        if(helps > 0){
            for(int i = 0; i < helps; i++){
                line = scanner.nextLine();
                if(line.equals("class PotentialBomb")){
                    helpContainers.add(new PotentialBomb(msf.getButtonGrid(), new NeighbourManager(msf.getButtonGrid(),msf.getVertices())));
                }
                if(line.equals("class SafeSquare")){
                    helpContainers.add(new SafeSquare(msf.getButtonGrid(), new NeighbourManager(msf.getButtonGrid(),msf.getVertices())));
                }
                if(line.equals("class SurroundingBomb")){
                    helpContainers.add(new SurroundingBomb(msf.getButtonGrid(), new NeighbourManager(msf.getButtonGrid(),msf.getVertices())));
                }
            }
        }
        int usableSpecials = Integer.parseInt(scanner.nextLine());
        long timer = Long.parseLong(scanner.nextLine());
        List<List<String>> tiles = new ArrayList<>();
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            String[] elements = line.split(" ");
            List<String> tmp = new ArrayList<>(Arrays.asList(elements));
            tiles.add(tmp);
        }
        for(int i = 0; i < tiles.size(); i++){
            for(int j = 0; j < tiles.get(i).size(); j++){
                msf.getButtonGrid()[i][j].setBomb(false);
                msf.getButtonGrid()[i][j].setFlagged(false);
                msf.getButtonGrid()[i][j].setProcessed(false);
                msf.getButtonGrid()[i][j].setSpecial(false);
                if(tiles.get(i).get(j).equals("f")){
                    msf.getButtonGrid()[i][j].setBomb(true);
                    msf.getButtonGrid()[i][j].setFlagged(true);
                    msf.getButtonGrid()[i][j].setProcessed(true);
                }else if(tiles.get(i).get(j).equals("fe")){
                    msf.getButtonGrid()[i][j].setFlagged(true);
                }else if(tiles.get(i).get(j).equals("fs")){
                    msf.getButtonGrid()[i][j].setFlagged(true);
                    msf.getButtonGrid()[i][j].setSpecial(true);
                }else if(tiles.get(i).get(j).equals("s")){
                    msf.getButtonGrid()[i][j].setSpecial(true);
                }else if(tiles.get(i).get(j).equals("b")){
                    msf.getButtonGrid()[i][j].setBomb(true);
                }else if(tiles.get(i).get(j).equals("e")) {
                    msf.getButtonGrid()[i][j].setProcessed(true);
                    msf.getButtonGrid()[i][j].setEnabled(false);
                }
            }
        }
        for(PolygonButton[] polygonButtons : msf.getButtonGrid()){
            for(PolygonButton polygonButton : polygonButtons){
                if(!polygonButton.isEnabled()){
                    int amount = amountOfBombs(msf.getNmg().getNeighbours(polygonButton));
                    if(amount > 0){
                        polygonButton.setText(String.valueOf(amount));
                    }else{
                        polygonButton.setText("");
                    }
                }
            }
        }
        InGameMenu inGameMenu = new InGameMenu(msf,helpContainers);
        inGameMenu.setTimerState(timer);
        inGameMenu.setAmountOfSpecial(usableSpecials);
    }

    /**
     * Visszaadja, hogy a paraméterül kapott listában hány bomba szerepel.
     *
     * @param list PolygonButton list
     * @return int
     */
    private int amountOfBombs(List<PolygonButton> list){
        int amount=0;
        for(PolygonButton polygonButton : list){
            if(polygonButton.hasBomb()){
                amount++;
            }
        }
        return amount;
    }
}
