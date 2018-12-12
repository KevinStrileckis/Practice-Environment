package lesson;

/**
 *
 * @author Kevin Strileckis
 */
public class Timer {
    private long start;
    private long delay;

    public Timer(long d) {
        start = System.currentTimeMillis();
        delay = d;
    }
    
    public void reset(long d){
        start = System.currentTimeMillis();
        delay = d;
    }
    
    public void reset(double d){
        start = System.currentTimeMillis();
        delay = (long)(d * 1000);
    }
    
    public void reset(){
        start = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return (System.currentTimeMillis() - start) > delay;
    }
    
}
