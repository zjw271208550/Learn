package PBFT.example;


/**
 * 线程逻辑不同的情况下可以使用一个 共享变量对象
 * 对象中保存变量值
 * 对变量值操作的方法加 synchronized
 */
//todo synchronized作用 threadlocal
public class ShareValue {

    private int money;
    public ShareValue(int money) {
        this.money = money;
    }

    /**
     * 取款
     * 判断余额不足就挂起等待下次存款
     * 唤醒后再次判断
     */
    public synchronized void getMoney(int money) {
        while (this.money < money) {
            System.out.println("取出" + money + ",余额不足" + (this.money-money));
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.money -= money;
        System.out.println("取出" + money + ",余额" + this.money);
    }

    /**
     * 存款
     * 存款完成就唤醒上一个取款
     */
    public synchronized void setMoney(int money) {
        this.money += money;
        System.out.println("存入" + money + ",余额：" + this.money);

        notify();

    }

    public static void main(String[] args) {
        ShareValue acount = new ShareValue(0);
        AddThread addThread = new AddThread(acount);
        SubThread subThread = new SubThread(acount);
        new Thread(addThread).start();
        new Thread(subThread).start();

    }
}
