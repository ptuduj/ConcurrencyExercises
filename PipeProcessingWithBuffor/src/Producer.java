public class Producer implements Runnable, IProcess{

    public final int converToThis = 0;
    BufforMonitor monitor;
    Buffor buffor;

    public Producer(BufforMonitor bufforMonitor){
        this.monitor = bufforMonitor;
        this.buffor = bufforMonitor.buffor;
    }


    @Override
    public void run() {
        while(true){
            for (int i=0; i<buffor.bufforSize; i++){
                monitor.processProduct(this, i);

            }
        }

    }
}
