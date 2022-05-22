import java.util.ArrayList;

public class Cop extends Turtle {

    // unique id of the cop turtle
    public final int id;

    public Cop(int id, int x, int y) {
        super(x, y);
        this.id = id;
    }

    // enforce an active agent within its vision
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
        Simulator.map.get(new Coord(x, y)).remove(this);
        x = targetActive.x;
        y = targetActive.y;
        Simulator.map.get(new Coord(x, y)).add(this);
        // enforce it
        targetActive.isActive = false;
        targetActive.jailTerm = Simulator.random.nextInt(Params.maxJailTerm);
    }

    @Override
    public void go() {
        move();
        enforce();
    }

    @Override
    public String toString() {
        return "Cop{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
