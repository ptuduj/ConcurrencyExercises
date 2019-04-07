import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args){

        Buffor buffor = new Buffor(20);
        BufforMonitor bufforMonitor = new BufforMonitor(buffor, 5);
        List<Thread> usersThreads = new LinkedList<>();

        // producer thread
        Thread t = new Thread(new Producer(bufforMonitor));
        usersThreads.add(t);
        t.start();

        for (int i=0; i< 5; i++){
            t = new Thread(new ProductConverter((i + 1), bufforMonitor));
            usersThreads.add(t);
            t.start();
        }

        // cosnumer thread
        t = new Thread(new Consumer(bufforMonitor));
        usersThreads.add(t);
        t.start();

        for (int i=0; i<5 + 2; i++){
            try {
                usersThreads.get(i).join();
            }
            catch (InterruptedException e) { e.printStackTrace(); }
        }


    }
}
