import java.awt.*;

public class Agent extends Turtle {
    // the unique id of the agent
    public final int id;
    // the risk aversion, value randomly lies between 0 and 1
    public final double riskAversion = Simulator.random.nextDouble();
    // the perceived hardship, value randomly lies between 0 and 1
    public final double perceivedHardship = Simulator.random.nextDouble();
    // whether the agent is currently active or not
    public boolean isActive;
    // how many days left before go to jail
    public int jailTerm;


    public Agent(int id, Point point, Simulator simulator) {
        super(point, simulator);
        this.id = id;
    }

    @Override
    public boolean isOccupying() {
        return jailTerm <= 0 && injureTerm <= 0;
    }

    // determine behavior of the agent in this tick,
    public void determineBehavior() {
        double grievance = perceivedHardship * (1 -
                simulator.government_legitimacy);
        // the number of cops in vision
        double c = 0;
        // the number of actives in vision
        double a = 1;
        for (Point point : simulator.withinDistance(point, simulator.vision)) {
            if (simulator.map.get(point).isEmpty()) continue;
            for (Turtle turtle : simulator.map.get(point)) {
                if (turtle instanceof Agent && ((Agent) turtle).isActive) a++;
                if (turtle instanceof Cop && turtle.injureTerm <= 0) c++;
            }
        }
        // calculation details comes from netlogo library module Rebellion code
        double estimateArrestProbability = 1 - Math.exp(-simulator.k *
                Math.floor(c / a));
        isActive = (grievance - riskAversion * estimateArrestProbability >
                simulator.threshold);
    }

    @Override
    public void move() {
        if (simulator.movement) super.move();
    }

    // what it will do in this tick, logic comes from netlogo library module
    // Rebellion code
    @Override
    public void go() {
        if (jailTerm <= 0) {
            if (injureTerm <= 0) {
                move();
                determineBehavior();
            } else {
                injureTerm--;
                if (injureTerm == 0)
                    jailTerm = Simulator.random.nextInt(simulator.maxJailTerm);
            }
        } else jailTerm--;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", riskAversion=" + riskAversion +
                ", perceivedHardship=" + perceivedHardship +
                ", isActive=" + isActive +
                ", jailTerm=" + jailTerm +
                ", point=" + point +
                ", injureTerm=" + injureTerm +
                '}';
    }
}