package ui;

/**
 *
 * @author Kevin Strileckis
 */
public class UserInfo {
    private static String name;
    private static int score;

    public static String getName() {
        return name;
    }
    public static void setName(String aName) {
        name = aName;
    }
    
    public static int getScore() {
        return score;
    }
    public static void setScore(int aScore) {
        score = aScore;
    }
    
}
