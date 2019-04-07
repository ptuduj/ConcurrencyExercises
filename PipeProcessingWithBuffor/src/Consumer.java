public class Consumer implements Runnable, IProcess{

    public final int converToThis = -1;
    Buffor buffor;
    BufforMonitor monitor;


    public Consumer(BufforMonitor bufforMonitor){
        this.monitor = bufforMonitor;
        this.buffor = bufforMonitor.buffor;
    }


    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < buffor.bufforSize; i++) {
                monitor.processProduct(this, i);
            }
        }
    }
}
