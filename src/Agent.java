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

    public Agent(int id, int x, int y) {
        super(x, y);
        this.id = id;
    }

    // determine behavior of the agent in this tick,
    public void determineBehavior() {
        double grievance = perceivedHardship * (1 -
                Params.government_legitimacy);
        // the number of cops in vision
        double c = 0;
        // the number of actives in vision
        double a = 1;
        for (Coord coord :
                Coord.findNeighbour(new Coord(x, y))) {
            if (Simulator.map.get(coord).isEmpty()) continue;
            for (Turtle turtle : Simulator.map.get(coord)) {
                if (turtle instanceof Agent && ((Agent) turtle).isActive) a++;
                if (turtle instanceof Cop) c++;
            }
        }
        // calculation details comes from netlogo library module Rebellion code
        double estimateArrestProbability = 1 - Math.exp(-Params.k *
                Math.floor(c / a));
        isActive = (grievance - riskAversion * estimateArrestProbability >
                Params.threshold);
    }

    // set jail terms for this agent
    public void goToJail(int terms) {
        jailTerm = terms;
    }

    // what it will do in this tick, logic comes from netlogo library module
    // Rebellion code
    @Override
    public void go() {
        if (jailTerm == 0) {
            move();
            determineBehavior();
        } else {
            jailTerm--;
        }
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", riskAversion=" + riskAversion +
                ", perceivedHardship=" + perceivedHardship +
                ", isActive=" + isActive +
                ", jailTerm=" + jailTerm +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
