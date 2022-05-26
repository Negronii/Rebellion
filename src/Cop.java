import java.awt.*;
import java.util.ArrayList;

public class Cop extends Turtle {
    // unique id of the cop turtle
    public final int id;

    public Cop(int id, Point point, Simulator simulator) {
        super(point, simulator);
        this.id = id;
    }

    // enforce an active agent within its vision
    public void enforce() {
        // find all active agents nearby
        ArrayList<Agent> actives = new ArrayList<>();
        for (Point point : simulator.withinDistance(point, simulator.vision)) {
            for (Turtle turtle : simulator.map.get(point))
                if (turtle instanceof Agent && ((Agent) turtle).isActive)
                    actives.add((Agent) turtle);
        }
        // no active agents, do nothing
        if (actives.isEmpty()) return;
        // find a random targetActive
        Agent targetActive =
                actives.get(Simulator.random.nextInt(actives.size()));
        // go to the active's place
        simulator.map.get(point).remove(this);
        point = targetActive.point;
        simulator.map.get(point).add(this);
        boolean fight = false;
        // extension case
        if (simulator.injure_extension) {
            double targetGrievance = targetActive.perceivedHardship * (1 -
                    simulator.government_legitimacy);
            // see if there is a fight
            if (Simulator.random.nextDouble() < targetGrievance) {
                fight = true;
                // if number of cops within vision * equipment coefficient >
                // number of active agent within vision then active agent injure
                // otherwise cop injure
                int nCops = 0, nActive = 0;
                for (Point point : simulator.withinDistance(point,
                        simulator.vision)) {
                    if (simulator.map.get(point).isEmpty()) continue;
                    for (Turtle turtle : simulator.map.get(point)) {
                        if (turtle instanceof Cop && turtle.injureTerm <= 0)
                            nCops++;
                        else if (turtle instanceof Agent && ((Agent)
                                turtle).isActive) nActive++;
                    }
                }
                if (nCops * simulator.equipmentCoefficient > nActive){
                    targetActive.injureTerm = Simulator.random.nextInt(
                            simulator.maxTreatmentTerm + 1);
                    targetActive.isActive = false;
                }
                else injureTerm = Simulator.random.nextInt(
                        simulator.maxTreatmentTerm + 1);
            }
        }
        // if no fight happened
        if (!fight) {
            // enforce it
            targetActive.isActive = false;
            targetActive.jailTerm = Simulator.random.nextInt(simulator.maxJailTerm + 1);
        }
    }

    @Override
    public void go() {
        if (injureTerm <= 0){
            move();
            enforce();
        }
        else injureTerm--;
    }
}
