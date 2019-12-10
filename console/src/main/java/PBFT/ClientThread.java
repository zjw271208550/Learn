package PBFT;

import java.util.Random;

public class ClientThread implements Runnable {

    private PublicValue ppc;
    private PublicValue pc;
    private PublicValue cc;

    public ClientThread(PublicValue ppc, PublicValue pc, PublicValue cc) {
        this.ppc = ppc;
        this.pc = pc;
        this.cc = cc;
    }

    @Override
    public void run() {
        try {
            while (ppc.getConfirm() < 3*Consts.NODE_BAD){
                Thread.sleep(100);
                System.out.println("pre-prepared num: "+ppc.getConfirm());
            }
            System.out.println("#====================# pre-prepare has done! #====================#");
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            while (pc.getConfirm() < 2*Consts.NODE_BAD){
                Thread.sleep(100);
                System.out.println("prepared num: "+pc.getConfirm());
            }
            System.out.println("#====================#   prepare has done!   #====================#");
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            while (cc.getConfirm() < 2*Consts.NODE_BAD+1){
                Thread.sleep(100);
                System.out.println("committed num: "+cc.getConfirm());
            }
            System.out.println("#====================#   commit has done!    #====================#");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
