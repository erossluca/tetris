package tetris;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;

public class ScoresTest
{
    ArrayList<Scores> allScores;
    Scores scoreOne, scoreTwo;

    @Before
    public void setUp()
    {
        allScores = new ArrayList<>();
        scoreOne = new Scores(1, "Béla", 20);
        allScores.add(scoreOne);

        scoreTwo = new Scores(2, "Julcsi", 30);
        allScores.add(scoreTwo);
    }

    @Test
    public void comparatorTest()
    {
        Collections.sort(allScores, Scores.compareByScores());
        int result = allScores.get(0).getScore();

        Assert.assertEquals(20, result);
    }


    @Test
    public void getPlaceTest()
    {
        Assert.assertEquals(2, scoreTwo.getPlace());
    }

    @Test
    public void getNameTest()
    {
        Assert.assertEquals("Béla", scoreOne.getName());
    }

    @Test
    public void getScoreTest()
    {
        Assert.assertEquals(30, scoreTwo.getScore());
    }

    @Test
    public void setPlaceTest()
    {
        scoreOne.setPlace(3);

        Assert.assertEquals(3, scoreOne.getPlace());
    }

    @Test
    public void setNameTest()
    {
        scoreTwo.setName("Józsi");

        Assert.assertEquals("Józsi", scoreTwo.getName());
    }

    @Test
    public void setScoreTest()
    {
        scoreTwo.setScore(50);

        Assert.assertEquals(50, scoreTwo.getScore());
    }

}
