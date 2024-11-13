/**
 * A menük tárolásáért felelős osztály
 */
public class MenuManager{
    NewGame newGame;
    Menu menu;
    InGameMenu inGameMenu;

    /**
     * Beállítja az új játék menüt
     *
     * @param nw NewGame
     */
    public void setNewGame(NewGame nw){
        this.newGame = nw;
    }

    /**
     * Beállítja a kezdeti menüt
     *
     * @param m Menu
     */
    public void setMenu(Menu m){
        this.menu = m;
    }

    /**
     * Beállítja a játék közbeni menüt
     *
     * @param igm InGameMenu
     */
    public void setInGameMenu(InGameMenu igm){
        this.inGameMenu = igm;
    }
}
