import java.util.ArrayList;
import java.util.List;

public class Buffor {


    int bufforSize;
    int bufforTaken;  // number of elements put to buffor
    int M;

    // 0 item not put to buffor
    // 1 item put to buffor
    ArrayList<Integer> bufforArray = new ArrayList<>();

    public Buffor(int M){
        this.M = M;
        this.bufforSize = 2 * M;
        this.bufforTaken = 0;

        for (int i=0; i<bufforSize; i++){
            this.bufforArray.add(0);
        }
    }

}
