import java.util.Objects;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        if (args.length != 0){
            if (Objects.equals(args[0], "GUI")){
                try {
                    new Simulator(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
