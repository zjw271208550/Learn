package PBFT;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) {
        PublicValue prePrepare = new PublicValue();
        PublicValue prepare = new PublicValue();
        PublicValue commit = new PublicValue();

        ExecutorService executor = Executors.newFixedThreadPool(Consts.NODE_AMOUNT);
        executor.submit(new ClientThread(prePrepare,prepare,commit));
        for(int i=0; i<Consts.NODE_AMOUNT-Consts.NODE_BAD;i++){
            executor.submit(new NodeThread("Node-"+i,prePrepare,prepare,commit));
        }
    }
}
