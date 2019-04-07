import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws InterruptedException {

        Buffor buffor = new Buffor(100000);
        int threadsNum = 1000;

        NewMonitor monitor = new NewMonitor(buffor);
        List<Producer> producers = new LinkedList<>();
        List<Thread> producersThreads = new LinkedList<>();
        List<Consumer> consumers = new LinkedList<>();
        List<Thread> consumersThreads = new LinkedList<>();

        for (int i = 0; i < threadsNum; i++) {
            Producer producer = new Producer(monitor, i);
            Thread t = new Thread(producer);
            producers.add(producer);
            producersThreads.add(t);
            t.start();
        }

        for (int i = 0; i < threadsNum; i++) {
            Consumer consumer = new Consumer(monitor, i);
            Thread t = new Thread(consumer);
            t.start();
            consumers.add(consumer);
            consumersThreads.add(t);
        }



        long startTime = System.nanoTime();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        for(Producer p :  producers)
        {
            p.setEnd();
        }
        for(Consumer c: consumers)
        {
            c.setEnd();
        }

        for (int i=0; i<threadsNum; i++){
            consumersThreads.get(i).join();
            producersThreads.get(i).join();
        }
        System.out.println("ooooooooooooooo");

    //    System.out.println("AVG waiting time for producer" + monitor.producersWaitingTimeSum/monitor.producerAccessCount);
    //a
        //    System.out.println("AVG waiting time for consumer" + monitor.consumersWaitingTimeSum/monitor.consumerAccessCount);

    }



}

