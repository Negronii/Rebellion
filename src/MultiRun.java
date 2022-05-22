// this class is to run simulator for multiple times and store data in .csv file
public class MultiRun {
    // number of runs
    public static final int nRun = 2;
    // how many steps we want to go in each run
    public static final int nStepsPerRun = 200;

    public static void main(String[] args) {
        for (int i = 0; i < nRun; i++) {
            Simulator.setup();
            for (int j = 0; j < nStepsPerRun; j++) {
                Simulator.go();
            }
            Simulator.writeToCsv("dataSamples/DataSample"+i+".csv");
        }
    }
}
