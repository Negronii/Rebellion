import java.util.Objects;
import java.io.IOException;
import java.io.FileWriter;
// main class to drive simulations
// argument GUI to be GUI mode
// arguments x y to be repeat x times y steps
public class Main {

    // all experiment values, first is default and rest are candidates
    public static double[] cop = {0.04, 0.01, 0.02, 0.06, 0.08, 0.1};
    public static double[] agent = {0.7, 0.40, 0.50, 0.60, 0.80, 0.90};
    public static int[] vision = {7, 2, 4, 6, 8, 10};
    public static double[] legit = {0.82, 0.2, 0.4, 0.6, 0.8, 1.0};
    public static int[] jail = {30, 0, 10, 20, 40, 50};
    public static double[] corruption = {0, 0.2, 0.4, 0.6, 0.8};
    public static int sampleSize = 100;
    public static double[] extensionLegit = new double[sampleSize];
    public static double[] result = new double[sampleSize];
    public static double buffer;

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
                int x = Integer.parseInt(args[0]), y = Integer.parseInt(args[1]);
                // runOneFeature(simulator, x, y);
                //  //for each feature, run nRun times with nStepRun steps
                // for (int i = 1; i < cop.length; i++) {
                //     simulator.initial_cop_density = cop[i];
                //     runOneFeature(simulator, x, y);
                // }
                // simulator.initial_cop_density = cop[0];

                // for (int i = 1; i < agent.length; i++) {
                //     simulator.initial_agent_density = agent[i];
                //     runOneFeature(simulator, x, y);
                // }
                // simulator.initial_agent_density = agent[0];

                // for (int i = 1; i < vision.length; i++) {
                //     simulator.vision = vision[i];
                //     runOneFeature(simulator, x, y);
                // }
                // simulator.vision = vision[0];

                // for (int i = 1; i < legit.length; i++) {
                //     simulator.government_legitimacy = legit[i];
                //     runOneFeature(simulator, x, y);
                // }
                // simulator.government_legitimacy = legit[0];

                // for (int i = 1; i < jail.length; i++) {
                //     simulator.maxJailTerm = jail[i];
                //     runOneFeature(simulator, x, y);
                // }
                // simulator.maxJailTerm = jail[0];

                simulator.injure_extension = true;
                for (int i = 0; i < 100; i++) {
                    extensionLegit[i] = (double)i/100;
                }
                for (int i = 0; i < 100; i++) {
                    simulator.government_legitimacy = extensionLegit[i];
                    runOneFeature(simulator, x, y);
                    result[i] = buffer;
                }
                try{
                    FileWriter fw = new FileWriter("dataSamples/extension/"
                            + "result" + ".csv");
                    fw.append("legit");
                    fw.append(',');
                    fw.append("equipment");
                    fw.append('\n');
                    for (int i = 0; i < sampleSize; i++) {
                        fw.append(Integer.toString(i));
                        fw.append(',');
                        fw.append(""+result[i]);
                        fw.append('\n');
                    }
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // runOneFeature(simulator, x, y);
            }
        }
    }

    public static void runOneFeature(Simulator simulator, int x, int y){
        int total = x;
        int success;
        // double equipment = simulator.equipmentCoefficient;
        double upperEquipment = 100;
        double lowerEquipment = 0;
        // System.out.println("equipment: " + equipment);
        // System.out.println("lower: " + lowerEquipment);
        // System.out.println("upper: " + upperEquipment);
        while (upperEquipment - lowerEquipment > 0.001) {
            total = x;
            success = 0;
            for (int i = 0; i < x; i++) {
                simulator.setup();
                for (int j = 0; j < y; j++) {
                    simulator.go();
                    if (simulator.successfulTerm > 10) {
                        success++;
                        break;
                    }
                }
                // simulator.writeToCsv("dataSamples/java/"
                //         + simulator.initial_cop_density + "_"
                //         + simulator.initial_agent_density + "_"
                //         + simulator.vision + "_"
                //         + simulator.government_legitimacy + "_"
                //         + simulator.maxJailTerm + "_" + i + ".csv");
                // simulator.writeToCsv("dataSamples/extension/"
                //         + Params.government_legitimacy + "_"
                //         + simulator.equipmentCoefficient + ".csv");
            }
            if (success/total > 0.1) {
                lowerEquipment = simulator.equipmentCoefficient;
                simulator.equipmentCoefficient = simulator.equipmentCoefficient + (upperEquipment - simulator.equipmentCoefficient)/2;
            } else {
                upperEquipment = simulator.equipmentCoefficient;
                simulator.equipmentCoefficient = simulator.equipmentCoefficient + (lowerEquipment - simulator.equipmentCoefficient)/2;
            }
            // System.out.println("equipment: " + equipment);
            // System.out.println("lower: " + lowerEquipment);
            // System.out.println("upper: " + upperEquipment);
        }
        // System.out.println("equipment: " + equipment);
        // System.out.println("lower: " + lowerEquipment);
        // System.out.println("upper: " + upperEquipment);
        // System.out.println("Finished");
        buffer = simulator.equipmentCoefficient;
        System.out.println(simulator.government_legitimacy + ": "  + buffer);
    }
}
