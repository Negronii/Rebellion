import java.util.ArrayList;

public class Cop extends Turtle {
    public final int id;

    public Cop(int id, int x, int y) {
        super(x, y);
        this.id = id;
    }

    public void enforce() {
        // find all active agents nearby
        ArrayList<Agent> actives = new ArrayList<>();
        for (Coord coord : Coord.findNeighbour(new Coord(x, y))) {
            for (Turtle turtle : Simulator.map.get(coord)) {
                if (turtle instanceof Cop) continue;
                if (turtle instanceof Agent && ((Agent) turtle).isActive)
                    actives.add((Agent) turtle);
            }
        }
        // no active agents, do nothing
        if (actives.isEmpty()) return;
        // find a random targetActive
        Agent targetActive =
                actives.get(Simulator.random.nextInt(actives.size()));
        // go to the active's place
        x = targetActive.x;
        y = targetActive.y;
        // enforce it
        targetActive.isActive = false;
        targetActive.jailTerm = Simulator.random.nextInt(Params.maxJailTerm);
    }

    @Override
    public void go() {
        move();
        enforce();
    }
}
