public class Producer implements Runnable {

    int producerID;
 //   BufforMonitor monitor;
    NewMonitor monitor;
    int howManyItemsToPut;
    boolean end = false;


    public Producer(NewMonitor bufforMonitor, int consumerId){
        this.monitor = bufforMonitor;
        this.producerID = consumerId;
    }

    @Override
    public void run() {
        while (!end ) {
            this.howManyItemsToPut = Math.max((int) (Math.random() * monitor.buffor.M ) + 1, monitor.buffor.M-1);
            monitor.put(producerID, howManyItemsToPut);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("ENDP " + monitor.producersEnded);
        monitor.producersEnded++;
    }

    public void setEnd(){
        end = true;
    }

}
