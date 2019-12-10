package Time;

import org.apache.commons.lang3.time.StopWatch;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

public class TimerPeriod {

    public static void main(String[] args) throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.sleep(899);
        stopWatch.stop();
        System.out.println(stopWatch.getTime());
    }
}
