package tetris;

import org.junit.Rule;
import org.junit.Test;

import java.awt.*;
import java.io.FileNotFoundException;

public class MenuTest
{
    @Test(expected = FileNotFoundException.class)
    public void testest() throws FileNotFoundException
    {
        Menu menu = new Menu();
        menu.loadManual("teszt.txt");
    }
}
