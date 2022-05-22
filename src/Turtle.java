import java.util.ArrayList;

public abstract class Turtle {
    public int x;
    public int y;

    public Turtle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // decide where to move for this turtle
    public void move() {
        ArrayList<Coord> neighbour = Coord.findNeighbour(new Coord(x, y));
        // remove cop's or not-in-jail agent's coordinates
        ArrayList<Coord> availableList = new ArrayList<>();
        for (Coord coord : neighbour) {
            if (Simulator.map.get(coord).isEmpty()) {
                availableList.add(coord);
                continue;
            }
            boolean availability = true;
            for (Turtle turtle : Simulator.map.get(coord)) {
                if (turtle instanceof Cop || (turtle instanceof Agent &&
                        ((Agent) turtle).jailTerm == 0)) {
                    availability = false;
                    break;
                }
            }
            if (availability) availableList.add(coord);
        }
        // if all stuck, don't move
        if (availableList.size() == 0) {
            return;
        }
        // select one random place from the availableList
        Coord randomCoordinate = availableList.get(
                Simulator.random.nextInt(availableList.size()));
        // update map
        Simulator.map.get(new Coord(x, y)).remove(this);
        Simulator.map.get(randomCoordinate).add(this);
        // update itself
        x = randomCoordinate.x;
        y = randomCoordinate.y;
    }

    // see what it will update in each tick
    public abstract void go();
}
