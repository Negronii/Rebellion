import java.awt.*;
import java.util.ArrayList;

public abstract class Turtle {
    public final Simulator simulator;
    public Point point;
    // how many days left for treatment
    public int injureTerm;

    public Turtle(Point point, Simulator simulator) {
        this.point = point;
        this.simulator = simulator;
    }

    // decide where to move for this turtle
    public void move() {
        // remove cop's or not-in-jail agent's coordinates
        ArrayList<Point> availableList = new ArrayList<>();
        for (Point coord : simulator.withinDistance(point, simulator.vision)) {
            if (simulator.map.get(coord).isEmpty()) {
                availableList.add(coord);
                continue;
            }
            boolean availability = true;
            for (Turtle turtle : simulator.map.get(coord)) {
                if (turtle.isOccupying()) {
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
        Point randomCoordinate = availableList.get(
                Simulator.random.nextInt(availableList.size()));
        // update map
        simulator.map.get(point).remove(this);
        simulator.map.get(randomCoordinate).add(this);
        point = randomCoordinate;
    }

    // see what it will update in each tick
    public abstract void go();

    // see if it takes up the coordinate
    public boolean isOccupying(){
        return injureTerm <= 0;
    }
}
