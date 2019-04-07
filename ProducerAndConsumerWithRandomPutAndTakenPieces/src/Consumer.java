
public class Consumer implements Runnable{

    int consumerId;
    NewMonitor monitor;
    int howManyItemsToTake;

    boolean end = false;


    public Consumer(NewMonitor bufforMonitor, int consumerId){
        this.monitor = bufforMonitor;
        this.consumerId = consumerId;
    }

    @Override
    public void run() {
        while (!end ){
            monitor.take(consumerId, howManyItemsToTake);
            this.howManyItemsToTake = Math.max((int) (Math.random() * monitor.buffor.M) + 1, monitor.buffor.M-1);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        monitor.consEnded++;
        System.out.println("ENDC " + consumerId +" " + monitor.consEnded);
    }

    public void setEnd(){
        end = true;
    }
}
