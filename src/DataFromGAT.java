import multithread.LAJDSThread;
import multithread.PADJBThread;

/**
 * Author:Lunch
 * Data : 2017/7/23 上午12:57
 */

public class DataFromGAT {
    public static void main(String[] args) {

        LAJDSThread lajdsThread = new LAJDSThread();
        PADJBThread padjbThread = new PADJBThread();
        
        Thread lajds = new Thread(lajdsThread);
        Thread padjb = new Thread(padjbThread);

        lajds.start();
        padjb.start();
    }
}
