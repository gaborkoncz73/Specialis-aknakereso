import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InGameMenuTest {
    MineSweeperFrame msf;
    InGameMenu inGameMenu;
    @Before
    public void setup(){
        msf = new MineSweeperFrame(4,40,40*30,16*40,10,1);
        List<HelpContainer> hlpc = new ArrayList<>();
        inGameMenu = new InGameMenu(msf,hlpc);
    }
    @Test
    public void usingSpecialLogic(){
        msf.getButtonGrid()[0][0].setBomb(false);
        msf.getButtonGrid()[0][1].setBomb(true);
        msf.getButtonGrid()[1][1].setBomb(true);
        msf.getButtonGrid()[1][0].setBomb(false);
        inGameMenu.usingSpecialLogic(msf.getButtonGrid()[0][0]);
        assertEquals(2,Integer.parseInt(msf.getButtonGrid()[0][0].getText()));
    }
    @Test
    public void setAmountOfSpecial(){
        int specials = 5;
        inGameMenu.setAmountOfSpecial(specials);
        assertEquals(specials,inGameMenu.getAmountOfSpecial());
    }
}