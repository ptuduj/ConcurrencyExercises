import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.JFrame;

public class Mandelbrot extends JFrame {

    private final int MAX_ITER = 10000;
    private final double ZOOM = 150;
    private BufferedImage I;
    private double zx, zy, cX, cY, tmp;
    LinkedList<Pixel> pixels;

    public Mandelbrot() {
        super("Mandelbrot Set");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
        pixels = new LinkedList<>();

    }

    public void task(Mandelbrot m, int  fromX, int toX, int fromY, int toY){
        int width = toX-fromX;
        int height = toY-fromY;

        for (int y = fromY; y < toY; y++) {
            for (int x = fromX; x < toX; x++) {
                zx = zy = 0;
                cX = (x - 400) / ZOOM;
                cY = (y - 300) / ZOOM;
                int iter = MAX_ITER;
                while (zx * zx + zy * zy < 4 && iter > 0) {
                    tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
            //    m.pixels.add(new Pixel(x, y, iter));
                I.setRGB(x , y , iter | (iter << 8));

            }
        }
    }


   // @Override
    public void paint(Graphics g ) {

        g.drawImage(I, 0, 0, this);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int threadsNumber = 600;
        int tasksNumber = 800;
        Mandelbrot m = new Mandelbrot();
        ExecutorService pool = Executors.newFixedThreadPool(tasksNumber);
        Set<Future<Integer>> set = new HashSet<Future<Integer>>();
      //  LinkedList<Callable<Integer>> callables= new LinkedList<>();

        long startTime =  System.nanoTime();
        for (int i=0; i< tasksNumber; i++){

            int fromX = i* 800/tasksNumber;

            for (int j=0; j<threadsNumber; j++){
                int fromY = j* 600/threadsNumber;

                Callable<Integer> callable = new MyThread(m,
                        fromX, fromX + 800/tasksNumber, fromY, fromY + 600/threadsNumber);
                Future<Integer> future = pool.submit(callable);
             //    callables.add(callable);
                set.add(future);
            }
        }

        for (Future<Integer> future : set) {
            future.get();
        }
         //  List<Future<Integer>> answers = pool.invokeAll(callables);

        /*   int i=0;
           for (Future anwer: set){
               i++;
               System.out.println(i + "  " + anwer.isDone());
           }*/

        long endTime = System.nanoTime();
        System.out.println(endTime - startTime);
            m.setVisible(true);


    }
}
