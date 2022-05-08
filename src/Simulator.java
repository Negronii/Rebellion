import java.util.*;


public class Simulator {

    // all turtles including agents and cops
    public static ArrayList<Turtle> turtles = new ArrayList<>();

    // the key is coordinate and the value is all agent and cops on it
    public static HashMap<Coord, ArrayList<Turtle>> map = new HashMap<>();

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
        initMap();
        setup();
        while (true) go();
    }

    public static void setup() {
        initMap();
        generateTurtles();
    }

    public static void go() {
        Collections.shuffle(turtles);
        for (Turtle turtle : turtles) {
            turtle.go();
        }
    }

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

    // initiate the map with empty Arraylist
    public static void initMap() {
        for (int i = 0; i < Params.width; i++) {
            for (int j = 0; j < Params.length; j++) {
                map.put(new Coord(i, j), new ArrayList<>());
            }
        }
    }


}
