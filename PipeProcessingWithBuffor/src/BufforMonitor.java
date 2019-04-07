import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.out;

public class BufforMonitor {

    int maxIndirectCovertersNum; // processing ones number
    Buffor buffor;
    List<Lock> locks = new ArrayList<>();
    List<Condition> conditions = new ArrayList<>();

    public BufforMonitor(Buffor buffor, int maxIndirectCovertersNum){
        this.buffor = buffor;
        this.maxIndirectCovertersNum = maxIndirectCovertersNum;

        for (int i=0; i<buffor.bufforSize; i++){
            locks.add(new ReentrantLock());
            conditions.add(locks.get(i).newCondition());
        }
    }


  // productNumber from 0 to bufforSize -1
    public void processProduct(IProcess iProcess, int productNumber){


        locks.get(productNumber).lock();
        if (iProcess instanceof Producer){

            while (buffor.bufforArray.get(productNumber) != -1){
                try {
                    conditions.get(productNumber).await();
                } catch (InterruptedException e) {  e.printStackTrace(); }
            }

            sleepRandomSec();
            buffor.bufforArray.set(productNumber, 0);
            out.println("Producer updated product " + productNumber + " to 0");
            conditions.get(productNumber).signalAll();
        }


        if (iProcess instanceof ProductConverter){
            ProductConverter converter =  (ProductConverter) iProcess;
            while (buffor.bufforArray.get(productNumber) !=  converter.ID - 1){
                try {
                    conditions.get(productNumber).await();
                } catch (InterruptedException e) {  e.printStackTrace(); }
            }

            sleepRandomSec();
            buffor.bufforArray.set(productNumber, converter.ID);
            out.println("ProductConverter updated product " + productNumber + " to " + converter.ID);
            conditions.get(productNumber).signalAll();
        }

        if (iProcess instanceof Consumer){
            while (buffor.bufforArray.get(productNumber) != this.maxIndirectCovertersNum){
                try {
                    conditions.get(productNumber).await();
                } catch (InterruptedException e) {  e.printStackTrace(); }
            }

            sleepRandomSec();
            buffor.bufforArray.set(productNumber, -1);
            out.println("Consumer updated product " + productNumber + " to -1");
            conditions.get(productNumber).signalAll();
        }
        locks.get(productNumber).unlock();

    }

    private void sleepRandomSec() {

        try {
            Thread.sleep((int) Math.random() * 100 + 20);
        } catch (InterruptedException e) {    e.printStackTrace();  }
    }

}
