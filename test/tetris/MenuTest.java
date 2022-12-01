package tetris;

import org.junit.Test;

import java.io.FileNotFoundException;

public class MenuTest
{
    @Test(expected = FileNotFoundException.class)
    public void loadManualTest() throws FileNotFoundException
    {
        Menu menu = new Menu();
        menu.loadManual("teszt.txt");
    }
}
