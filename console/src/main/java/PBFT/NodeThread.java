package PBFT;

import java.util.Random;

public class NodeThread implements Runnable {

    private String name;
    private PublicValue ppc;
    private PublicValue pc;
    private PublicValue cc;

    public NodeThread(String name, PublicValue ppc, PublicValue pc, PublicValue cc) {
        this.name = name;
        this.ppc = ppc;
        this.pc = pc;
        this.cc = cc;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(3000));
            ppc.beConfirm();
            System.out.println(name+" is pre-prepared.<========================");
            while (ppc.getConfirm() < 3*Consts.NODE_BAD){
                Thread.sleep(100);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            Thread.sleep(new Random().nextInt(4000));
            pc.beConfirm();
            System.out.println(name+" is prepared.<====================");
            while (pc.getConfirm() < 2*Consts.NODE_BAD){
                Thread.sleep(100);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            Thread.sleep(new Random().nextInt(5000));
            cc.beConfirm();
            System.out.println(name+" is committed.<===================");
            while (cc.getConfirm() < 2*Consts.NODE_BAD+1){
                Thread.sleep(100);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
