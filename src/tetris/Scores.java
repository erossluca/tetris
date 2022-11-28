package tetris;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;

public class Scores implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int place;
    private String name;
    private int score;


    /**
     * Bejegyzés konstruktora
     * @param place adott eredményhez tartozó helyezés
     * @param name adott eredményt elért játékos neve
     * @param score elért pontszám
     */
    public Scores(int place, String name, int score)
    {
        this.place = place;
        this.name = name;
        this.score = score;
    }

    /**
     * Helyezés setter-e
     * @param place az elért helyezés
     */
    public void setPlace(int place) {this.place = place;};

    /**
     * Névhez tartozó setter
     * @param name az eredményt elért játékos neve
     */
    public void setName(String name) {this.name = name;}

    /**
     * Pontszámhoz tartozó setter
     * @param score elért pontszám
     */
    public void setScore(int score) {this.score = score;}

    /**
     * A helyezéshez tartozó getter
     * @return visszaadja az elért helyezést
     */
    public int getPlace() {return this.place;}

    /**
     * Névhez tartozó getter
     * @return visszaadja az adott játékos nevét
     */
    public String getName() {return this.name;}

    /**
     * Pontszámhoz tartozó getter
     * @return visszaadja az elért pontszámot
     */
    public int getScore() {return this.score;}

    /**
     * A pontszámok összehasonlítását végzi
     * @return visszaadja a 2 pontszám közül a nagyobbat
     */
    public static Comparator<Scores> compareByScores()
    {
        Comparator<Scores> comparator = Comparator.comparingInt(Scores::getScore);
        return comparator;
    }
}