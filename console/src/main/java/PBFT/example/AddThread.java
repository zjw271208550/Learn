package PBFT.example;

import java.util.Random;

public class AddThread implements Runnable {

    private ShareValue value;

    public AddThread(ShareValue value) {
        this.value = value;
    }

    @Override
    public void run() {
        while(true) {
            int random = new Random().nextInt(1000);
            value.setMoney(random);
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
