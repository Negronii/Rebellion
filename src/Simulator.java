import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Simulator {
    public static Random random = new Random();
    // basic model parameters
    public double initial_cop_density = 0.04;
    public double initial_agent_density = 0.70;
    public double government_legitimacy = 0.82;
    public double vision = 7;
    public double k = 2.3;
    public double threshold = 0.10;
    public int maxJailTerm = 30;
    public boolean movement = true;

    // length refers to y-axis
    public int length = 40;
    // width refers to x-axis
    public int width = 40;

    // extension
    public boolean injure_extension = false;
    public int maxTreatmentTerm = 10;
    public double equipmentCoefficient = 5.0;

    // all turtles including agents and cops
    public ArrayList<Turtle> turtles = new ArrayList<>();

    // the key is coordinate and the value is all agent and cops on it
    public HashMap<Point, ArrayList<Turtle>> map = new HashMap<>();

    // number of quiet at end of each turn
    public ArrayList<Integer> nQuietList = new ArrayList<>();

    // number of active at end of each turn
    public ArrayList<Integer> nActiveList = new ArrayList<>();

    // number of jailed at end of each turn
    public ArrayList<Integer> nJailList = new ArrayList<>();

    // number of injured cops at end of each turn
    public ArrayList<Integer> nInjuredCopList = new ArrayList<>();

    // number of injured agents at end of each turn
    public ArrayList<Integer> nInjuredAgentList = new ArrayList<>();

    // number of cops in the simulation
    private int nCop = (int) Math.round(initial_cop_density * width * length);

    // the number of agents in the simulation
    private int nAgent = (int) Math.round(initial_agent_density * width * length);

    public int successfulTerm = 0;

    ArrayList<Integer> terms = new ArrayList<>();

    public Simulator(Boolean guiOn) throws Exception {
        // avoid total turtle number more than total grid number
        if (nCop + nAgent > width * length) {
            throw new Exception("too crowded! Reset your parameters!");
        }
        if (guiOn) new GUI(this);
    }

    // initiate the system, clear all stored data
    public void init() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                map.put(new Point(i, j), new ArrayList<>());
            }
        }
        turtles.clear();
        nQuietList.clear();
        nActiveList.clear();
        nJailList.clear();
        nInjuredAgentList.clear();
        nInjuredCopList.clear();
        nCop = (int) Math.round(initial_cop_density * width * length);
        nAgent = (int) Math.round(initial_agent_density * width * length);
    }

    // generate all turtles
    public void generateTurtles() {
        // an arraylist of all un occupied keys
        ArrayList<Point> unUsedKeys = new ArrayList<>(map.keySet());
        // shuffle the order
        Collections.shuffle(unUsedKeys);
        // generate agents
        for (int i = 0; i < nAgent; i++) {
            Point key = unUsedKeys.get(0);
            Agent agent = new Agent(i, key, this);
            turtles.add(agent);
            map.get(key).add(agent);
            unUsedKeys.remove(0);
        }
        // generate cops
        for (int i = 0; i < nCop; i++) {
            Point key = unUsedKeys.get(0);
            Cop cop = new Cop(i, key, this);
            turtles.add(cop);
            map.get(key).add(cop);
            unUsedKeys.remove(0);
        }
        count();
    }

    // count the number of each field at the moment
    public void count() {
        int nQuiet = 0, nActive = 0, nJail = 0, nInjuredCops = 0,
                nInjuredAgents = 0;
        // count the number of each field at the moment
        for (Turtle turtle : turtles) {
            if (turtle instanceof Agent) {
                if (((Agent) turtle).isActive) ++nActive;
                else if (turtle.injureTerm > 0) ++nInjuredAgents;
                else if (((Agent) turtle).jailTerm > 0) ++nJail;
                else ++nQuiet;
            }
            if (turtle instanceof Cop && turtle.injureTerm > 0) ++nInjuredCops;
        }
        // check if it is a successful rebellion term
        // i.e. injured cop more than half of total cops
        if (nInjuredCops > nCop / 2) successfulTerm++;
        if (nInjuredCops < nCop / 2) successfulTerm = 0;
        // add to list respectively
        nQuietList.add(nQuiet);
        nActiveList.add(nActive);
        nJailList.add(nJail);
        nInjuredCopList.add(nInjuredCops);
        nInjuredAgentList.add(nInjuredAgents);
        terms.add(successfulTerm);
    }

    // write the count result to csv file
    public void writeToCsv(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.append("time");
            fw.append(',');
            fw.append("quite");
            fw.append(',');
            fw.append("jail");
            fw.append(',');
            fw.append("active");
            if (injure_extension) {
                fw.append(',');
                fw.append("injuredCop");
                fw.append(',');
                fw.append("injuredAgent");
            }
            fw.append(',');
            fw.append("RebellionSuccessfulTurns");
            fw.append('\n');
            for (int i = 0; i < nActiveList.size(); i++) {
                fw.append(Integer.toString(i));
                fw.append(',');
                fw.append(nQuietList.get(i).toString());
                fw.append(',');
                fw.append(nJailList.get(i).toString());
                fw.append(',');
                fw.append(nActiveList.get(i).toString());
                if (injure_extension) {
                    fw.append(',');
                    fw.append(nInjuredCopList.get(i).toString());
                    fw.append(',');
                    fw.append(nInjuredAgentList.get(i).toString());
                }
                fw.append(',');
                fw.append(terms.get(i).toString());
                fw.append('\n');
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // set up the system
    public void setup() {
        init();
        generateTurtles();
    }

    // go one tick
    public void go() {
        Collections.shuffle(turtles);
        for (Turtle turtle : turtles) {
            turtle.go();
        }
        count();
    }

    // find all point within input distance between input point
    public ArrayList<Point> withinDistance(Point point, double distance) {
        ArrayList<Point> points = new ArrayList<>();
        int rDistance = (int) distance;
        for (int i = 0; i < 2 * rDistance + 1; i++) {
            for (int j = 0; j < 2 * rDistance + 1; j++) {
                if (new Point(rDistance, rDistance).distance(i, j) - 0.0000001 < distance) {
                    int x = (int) (i - distance + point.x + width) % width;
                    int y = (int) (j - distance + point.y + length) % length;
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }
}
