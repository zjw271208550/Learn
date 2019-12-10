package ProgressBar;

import java.text.NumberFormat;
import java.util.Locale;

public class ProgressBar {
    /**
     * 进度条总长度
     */
    public static void printProgressBar(int total, int now){
        int TOTLE_LENGTH = 50;
        for (int i = 0; i < TOTLE_LENGTH+21; i++) {
            System.out.print("\b");
        }
        System.out.print("# Progress : [");
        int l = (int)Math.floor(TOTLE_LENGTH * (now+0.0)/total);
        for (int i = 0; i < l; i++) {
            System.out.print(">");
        }
        for (int i = 0; i < TOTLE_LENGTH - l; i++) {
            System.out.print(" ");
        }
        NumberFormat format = NumberFormat.getPercentInstance(Locale.US);
        format.setMinimumFractionDigits(2);
        System.out.print("]" + format.format((now+0.0)/total));
    }

    public static void endProgressBar(){
        System.out.println();
    }
}
