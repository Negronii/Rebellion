import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Simulator {

    // all turtles including agents and cops
    public static ArrayList<Turtle> turtles = new ArrayList<>();

    // the key is coordinate and the value is all agent and cops on it
    public static HashMap<Coord, ArrayList<Turtle>> map = new HashMap<>();

    // number of quiet at end of each turn
    public static ArrayList<Integer> nQuietList = new ArrayList<>();

    // number of active at end of each turn
    public static ArrayList<Integer> nActiveList = new ArrayList<>();

    // number of jailed at end of each turn
    public static ArrayList<Integer> nJailList = new ArrayList<>();

    // number of cops in the simulation
    public static int nCop = (int) Math.round(Params.initial_cop_density
            * Params.width * Params.length);

    // the number of agents in the simulation
    public static int nAgent = (int) Math.round(Params.initial_agent_density
            * Params.width * Params.length);

    // a helper instance from Random class
    public static final Random random = new Random();

    public static void main(String[] args) throws Exception {
        // when there are more than 100% turtles, throw exception
        if (nCop + nAgent > Params.width * Params.length) {
            throw new Exception("toooooooooo crowded!");
        }
        new GUI();
    }

    // set up the system
    public static void setup() {
        init();
        generateTurtles();
    }

    // go one tick
    public static void go() {
        Collections.shuffle(turtles);
        for (Turtle turtle : turtles) {
            turtle.go();
        }
        countActiveJail();
    }

    // generate all turtles
    public static void generateTurtles() {
        // an arraylist of all un occupied keys
        ArrayList<Coord> unUsedKeys =
                new ArrayList(Arrays.asList(map.keySet().toArray()));
        // shuffle the order
        Collections.shuffle(unUsedKeys);
        // generate agents
        for (int i = 0; i < nAgent; i++) {
            Coord key = unUsedKeys.get(0);
            Agent agent = new Agent(i, key.x, key.y);
            turtles.add(agent);
            map.get(key).add(agent);
            unUsedKeys.remove(0);
        }
        // generate cops
        for (int i = 0; i < nCop; i++) {
            Coord key = unUsedKeys.get(0);
            Cop cop = new Cop(i, key.x, key.y);
            turtles.add(cop);
            map.get(key).add(cop);
            unUsedKeys.remove(0);
        }
    }

    // initiate the system, clear all stored data
    public static void init() {
        for (int i = 0; i < Params.width; i++) {
            for (int j = 0; j < Params.length; j++) {
                map.put(new Coord(i, j), new ArrayList<>());
            }
        }
        turtles.clear();
        nQuietList.clear();
        nActiveList.clear();
        nJailList.clear();
    }

    // count the number of active and jail
    public static void countActiveJail() {
        int nQuiet = 0, nActive = 0, nJail = 0;
        for (Turtle turtle :
                turtles) {
            if (turtle instanceof Agent && ((Agent) turtle).isActive) nActive++;
            if (turtle instanceof Agent && ((Agent) turtle).jailTerm > 0) nJail++;
        }
        nQuietList.add(nAgent-nActive-nJail);
        nActiveList.add(nActive);
        nJailList.add(nJail);
    }

    // save file to indicated file name
    public static void writeToCsv(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.append("time");
            fw.append(',');
            fw.append("quite");
            fw.append(',');
            fw.append("jail");
            fw.append(',');
            fw.append("active");
            fw.append('\n');
            fw.append('0');
            fw.append(',');
            fw.append(Integer.toString(nAgent));
            fw.append(',');
            fw.append('0');
            fw.append(',');
            fw.append('0');
            fw.append('\n');

            for (int i = 0; i < nActiveList.size(); i++) {
                fw.append(Integer.toString(i+1));
                fw.append(',');
                fw.append(nQuietList.get(i).toString());
                fw.append(',');
                fw.append(nJailList.get(i).toString());
                fw.append(',');
                fw.append(nActiveList.get(i).toString());
                fw.append('\n');
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

