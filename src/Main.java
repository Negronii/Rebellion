import java.util.Objects;
// main class to drive simulations
// argument GUI to be GUI mode
// arguments x y to be repeat x times y steps
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("argument GUI to be GUI mode");
            System.out.println("arguments x y to be repeat x times y steps");
        }
        else{
            if (Objects.equals(args[0], "GUI")) {
                new Simulator(true);
            }
            else {
                Simulator simulator = new Simulator(false);
                for (int i = 0; i < Integer.parseInt(args[0]); i++) {
                    simulator.setup();
                    for (int j = 0; j < Integer.parseInt(args[1]); j++) {
                        simulator.go();
                    }
                    simulator.writeToCsv("dataSamples/java/"
                    + simulator.initial_cop_density + "_"
                    + simulator.initial_agent_density + "_"
                    + simulator.vision + "_"
                    + simulator.government_legitimacy + "_"
                    + simulator.maxJailTerm + "_" + i + ".csv");
                }
            }
        }
    }
}
