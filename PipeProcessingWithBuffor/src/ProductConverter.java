public class ProductConverter implements Runnable, IProcess{

    int ID;
    BufforMonitor monitor;
    Buffor buffor;
    int converToThis;

    public ProductConverter(int ID, BufforMonitor bufforMonitor){
        this.ID = ID;
        this.monitor = bufforMonitor;
        this.buffor = monitor.buffor;
        this.converToThis = ID;
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
