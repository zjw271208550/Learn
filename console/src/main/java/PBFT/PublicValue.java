package PBFT;

public class PublicValue {
    private int confirm = 0;

    public synchronized void beConfirm(){
        confirm = confirm + 1;
    }

    public int getConfirm() {
        return confirm;
    }
}
