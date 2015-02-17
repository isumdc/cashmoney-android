package mobiledev.club.cashmoney.Models;

/**
 * Created by Ethan on 2/12/2015.
 */
public class GameData {

    private int score;
    private int difficulty;


    public GameData()
    {
        score = 0;
        difficulty = 0;
    }

    public int getScore()
    {
        return score;
    }

    public int getDifficulty()
    {
        return difficulty;
    }
}
