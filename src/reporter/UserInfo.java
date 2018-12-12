package reporter;

/**
 *
 * @author Kevin Strileckis
 */
public class UserInfo {
    private static String name;
    private static long timeStart, timeEnd;
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
    public static void incScore() {
        score++;
    }

    /**
     * @return the timeStart
     */
    public static long getTimeStart() {
        return timeStart;
    }

    /**
     * @param aTimeStart the timeStart to set
     */
    public static void setTimeStart(long aTimeStart) {
        timeStart = aTimeStart;
    }

    /**
     * @return the timeEnd
     */
    public static long getTimeEnd() {
        return timeEnd;
    }

    /**
     * @param aTimeEnd the timeEnd to set
     */
    public static void setTimeEnd(long aTimeEnd) {
        timeEnd = aTimeEnd;
    }
    
}
