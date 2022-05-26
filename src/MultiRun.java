// this class is to run simulator for multiple times and store data in .csv file
public class MultiRun {
    // number of runs
    public static final int nRun = 1;
    // how many steps we want to go in each run
    public static final int nStepsPerRun = 1000;

    // all experiment values, first is default and rest are candidates
    public static double[] cop = {0.04, 0.01, 0.02, 0.06, 0.08, 0.1};
    public static double[] agent = {0.7, 0.40, 0.50, 0.60, 0.80, 0.90};
    public static int[] vision = {7, 2, 4, 6, 8, 10};
    public static double[] legit = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
    public static int[] jail = {30, 0, 10, 20, 40, 50};
    public static double[] corruption = {0, 0.2, 0.4, 0.6, 0.8};
    


    public static void main(String[] args) {

//        runOneFeature();
//        // for each feature, run nRun times with nStepRun steps
//        for (int i = 1; i < cop.length; i++) {
//            Params.initial_cop_density = cop[i];
//            runOneFeature();
//        }
//        Params.initial_cop_density = cop[0];
//
//        for (int i = 1; i < agent.length; i++) {
//            Params.initial_agent_density = agent[i];
//            runOneFeature();
//        }
//        Params.initial_agent_density = agent[0];
//
//        for (int i = 1; i < vision.length; i++) {
//            Params.vision = vision[i];
//            runOneFeature();
//        }
//        Params.vision = vision[0];
//
//        for (int i = 1; i < legit.length; i++) {
//            Params.government_legitimacy = legit[i];
//            runOneFeature();
//        }
//        Params.government_legitimacy = legit[0];
//
//        for (int i = 1; i < jail.length; i++) {
//            Params.maxJailTerm = jail[i];
//            runOneFeature();
//        }
//        Params.maxJailTerm = jail[0];

        // runOneFeature();
    }

    // run nRun times with nStepRun steps, output file name as parameters
//     public static void runOneFeature(){
//         for (int i = 0; i < nRun; i++) {
//             Simulator.setup();
//             for (int j = 0; j < nStepsPerRun; j++) {
//                 Simulator.go();
//                 if (Simulator.flag == true) {
//                     break;
//                 }
//             }
// //            Simulator.writeToCsv("dataSamples/java/"
// //                    + Params.initial_cop_density + "_"
// //                    + Params.initial_agent_density + "_"
// //                    + Params.vision + "_"
// //                    + Params.government_legitimacy + "_"
// //                    + Params.maxJailTerm + "_" + i + ".csv");
//             Simulator.writeToCsv("dataSamples/extension/"
//                     + Params.government_legitimacy + "_"
//                     + i + ".csv");
//         }
//     }
}
