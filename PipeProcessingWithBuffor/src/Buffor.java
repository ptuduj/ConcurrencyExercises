import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class Buffor {

    int bufforSize;
    List<Integer> bufforArray;

    public Buffor(int bufforSize){
        this.bufforSize = bufforSize;
        this.bufforArray = new ArrayList<>();
        initialiseBuffor();
    }

    public void printBuffor(){

        for (int i=0; i<bufforSize; i++){
           out.print(bufforArray.get(i) + " ");
        }
        out.println();
    }


    public void initialiseBuffor(){
        for (int i=0; i<bufforSize; i++){
            bufforArray.add(-1);
        }
    }
}
