package PBFT.example;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程相同逻辑的情况下可以使用同一个 Runnable对象
 * 例如：多窗口买票
 */
//测试量上来不安全
public class SameThread implements Runnable{

    private int tickets = 1000;

    @Override
    public void run() {
        while (true){
            if(this.tickets >0){
                tickets = tickets - 1;
                System.out.println(Thread.currentThread().getName() + " 目前余票： " + this.tickets);
                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                break;
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        SameThread sameThread = new SameThread();
        executor.submit(new Thread(sameThread));
        executor.submit(new Thread(sameThread));
        executor.submit(new Thread(sameThread));
        executor.submit(new Thread(sameThread));
        executor.submit(new Thread(sameThread));
    }
}
