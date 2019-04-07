import java.util.concurrent.Callable;

public class MyThread implements Callable {

    private int fromX;
    private int toX;
    private int fromY;
    private int toY;
    private Mandelbrot mandelbrot;

    public MyThread(Mandelbrot mandelbrot, int fromX, int toX, int fromY, int toY){
        this.mandelbrot = mandelbrot;
        this.fromX = fromX;
        this.toX = toX;
        this.fromY = fromY;
        this.toY = toY;
    }

    public Integer call(){
        mandelbrot.task(mandelbrot, fromX, toX, fromY, toY);

        return 0;
    }
}
