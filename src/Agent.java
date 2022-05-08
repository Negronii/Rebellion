public class Agent extends Turtle {

    public final int id;
    public final double riskAversion = Simulator.random.nextDouble();
    public final double perceivedHardship = Simulator.random.nextDouble();
    public boolean isActive;
    public int jailTerm;

    public Agent(int id, int x, int y) {
        super(x, y);
        this.id = id;
    }

    public void determineBehavior() {
        double grievance = perceivedHardship * (1 - Params.government_legitimacy);
        double c = 0;
        double a = 1;
        for (Coord coord :
                Coord.findNeighbour(new Coord(x, y))) {
            if (Simulator.map.get(coord).isEmpty()) continue;
            for (Turtle turtle : Simulator.map.get(coord)) {
                if (turtle instanceof Agent && ((Agent) turtle).isActive) a++;
                if (turtle instanceof Cop) c++;
            }
        }
        double estimateArrestProbability = 1 - Math.exp(-Params.k * Math.floor(c / a));
        isActive = (grievance - riskAversion * estimateArrestProbability > Params.threshold);
    }

    public void goToJail(int terms) {
        jailTerm = terms;
    }

    @Override
    public void go() {
        if (jailTerm == 0) {
            move();
            determineBehavior();
        } else {
            jailTerm--;
        }
    }
}
