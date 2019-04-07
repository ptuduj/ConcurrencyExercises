import java.math.BigInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.out;

public class BufforMonitor {

    Buffor buffor;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    long producerAccessCount;
    long producersWaitingTimeSum;
    long consumerAccessCount;
    long consumersWaitingTimeSum;


    public BufforMonitor(Buffor buffor){
        this.buffor=buffor;
        this.consumerAccessCount =0;
        this.producerAccessCount = 0;
        this.producersWaitingTimeSum = 0;
        this.consumersWaitingTimeSum = 0;
    }

    public void put(int producerID, int howManyToPut){
        lock.lock();
        out.println("Producer " + producerID + " wants to put " + howManyToPut +
                " items. BufforAvailbale " + (buffor.bufforSize - buffor.bufforTaken));
        long startTime = System.nanoTime();
        while (buffor.bufforSize - buffor.bufforTaken < howManyToPut) {  // not enough space
            try {
                condition.await();
            } catch (InterruptedException e) {   e.printStackTrace(); }
        }

        long endWaitingTime= System.nanoTime();
        this.producersWaitingTimeSum += (endWaitingTime - startTime);
        this.producerAccessCount++;
        
        int tmp = howManyToPut;
        out.println("Producer " + producerID + " is putting " + howManyToPut +
                " items. BufforAvailbale " + (buffor.bufforSize - buffor.bufforTaken));
        for (int i=0; howManyToPut!=0; i++) {
            if (buffor.bufforArray.get(i) == 0) {
                // put item
                howManyToPut--;
                buffor.bufforArray.set(i, 1);
            }
        }

        buffor.bufforTaken += tmp;
        condition.signalAll();
        out.println("Producer " + producerID + " put" + tmp + " items");
        lock.unlock();

    }

    public void take(int consumerID, int howManyToTake) {
        lock.lock();
        out.println("Consumer " + consumerID +  "wants to take " + howManyToTake +
                " items. BufforTaken " + buffor.bufforTaken);

        long startTime = System.nanoTime();
        while (buffor.bufforTaken < howManyToTake) {  // not enough items to take
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endWaitingTime= System.nanoTime();
        this.consumersWaitingTimeSum += (endWaitingTime - startTime);
        this.consumerAccessCount++;

        int tmp = howManyToTake;
        for (int i = 0; howManyToTake != 0; i++) {
            if (buffor.bufforArray.get(i) == 1) {
                // take item
                howManyToTake--;
                buffor.bufforArray.set(i, 0);
            }
        }
        buffor.bufforTaken -= tmp;
        condition.signalAll();
        out.println("Consumer " + consumerID + "took" + tmp + "items");
        lock.unlock();
    }

}
