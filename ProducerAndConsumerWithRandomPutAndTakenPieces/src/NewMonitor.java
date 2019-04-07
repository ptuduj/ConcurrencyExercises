import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.out;

public class NewMonitor {

    private boolean hasAnyProdWaiters=false;
    private boolean hasAnyConsWaiters=false;
    int producersEnded = 0;
    int consEnded = 0;


    ReentrantLock lock = new ReentrantLock();
    private Condition firstProducer = lock.newCondition();
    private Condition firstConsumer = lock.newCondition();
    private Condition producentCond = lock.newCondition();;
    private Condition consumerCond = lock.newCondition();

    Buffor buffor;
    long producerAccessCount;
    long producersWaitingTimeSum;
    long consumerAccessCount;
    long consumersWaitingTimeSum;

    public NewMonitor(Buffor buffor){
        this.buffor=buffor;
        this.consumerAccessCount =0;
        this.producerAccessCount = 0;
        this.producersWaitingTimeSum = 0;
        this.consumersWaitingTimeSum = 0;
    }


    public void put(int producerID, int howManyToPut) {

        lock.lock();
        out.println("Producer " + producerID + " wants to put " + howManyToPut +
                " items. BufforAvailbale " + (buffor.bufforSize - buffor.bufforTaken));

        long startTime = System.nanoTime();
       /* if (lock.hasWaiters(producentCond)) {
            try {
                producentCond.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } */
        if (lock.hasWaiters(firstProducer)) {
            try {
                producentCond.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

   /*     while (hasAnyProdWaiters) {
            try {
                producentCond.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        hasAnyProdWaiters = true; */

        while (buffor.bufforSize - buffor.bufforTaken < howManyToPut) {  // not enough space
            try {
                firstProducer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endWaitingTime= System.nanoTime();
        this.producersWaitingTimeSum += (endWaitingTime - startTime);
        this.producerAccessCount++;

        int tmp = howManyToPut;
        for (int i=0; howManyToPut!=0; i++) {
            if (buffor.bufforArray.get(i) == 0) {
                // put item
                howManyToPut--;
                buffor.bufforArray.set(i, 1);
            }
        }

        buffor.bufforTaken += tmp;
        out.println("Producer " + producerID + " put " + tmp +
                " items. BufforAvailbale " + (buffor.bufforSize - buffor.bufforTaken));

        producentCond.signal();
        firstConsumer.signal();
        hasAnyProdWaiters = false;
        lock.unlock();
    }


    public void take(int consumerID, int howManyToTake) {
        lock.lock();
        out.println("Consumer " + consumerID +  " wants to take " + howManyToTake +
                " items. BufforTaken " + buffor.bufforTaken);

        long startTime = System.nanoTime();
   /*     if (lock.hasWaiters(consumerCond)) {
            try {
                consumerCond.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } */

        if (lock.hasWaiters(firstConsumer)) {
            try {
                consumerCond.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

     /*   while (hasAnyConsWaiters) {
            try {
                consumerCond.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        hasAnyConsWaiters = true;  */

        while (buffor.bufforTaken < howManyToTake) {
            try {
                firstConsumer.await();
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
        out.println("Consumer " + consumerID + " took " + tmp + "items. Buffor taken " + buffor.bufforTaken);

        consumerCond.signal();
        firstProducer.signal();
        hasAnyProdWaiters = false;
        lock.unlock();
    }

}
