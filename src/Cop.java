import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class Cop extends Turtle {

    // unique id of the cop turtle
    public final int id;
    // the corruption, value randomly lies between 0 and corruption
    public final double corruption =
            Params.corruption;
//            Simulator.random.nextDouble()*Params.corruption;
//    public final double corruption = 1;

    public Cop(int id, int x, int y) {
        super(x, y);
        this.id = id;
    }

    // enforce an active agent within its vision
    public void enforce() {
        // find all active agents nearby
        ArrayList<Agent> actives = new ArrayList<>();
        ArrayList<Cop> cops = new ArrayList<>();
        for (Coord coord : Coord.findNeighbour(new Coord(x, y))) {
            for (Turtle turtle : Simulator.map.get(coord)) {
                if (turtle instanceof Cop && turtle.injuryTerm == 0)
                    cops.add((Cop) turtle);
                if (turtle instanceof Agent && ((Agent) turtle).isActive)
                    actives.add((Agent) turtle);
            }
        }
        // no active agents, do nothing
        if (actives.isEmpty()) return;
        // cop successfully enforce an active agent based on its corruption
        if (Params.extension) { if(Simulator.random.nextDouble()<corruption)
            return;}
        // find a random targetActive
        Agent targetActive =
                actives.get(Simulator.random.nextInt(actives.size()));
        // go to the active's place
        Simulator.map.get(new Coord(x, y)).remove(this);
        x = targetActive.x;
        y = targetActive.y;
        Simulator.map.get(new Coord(x, y)).add(this);
        // enforce it
        if (Params.extension) {
            if(Simulator.random.nextDouble() <= targetActive.grievance){
                if((cops.size()+1) * Params.equipment >= actives.size()+1) {
                    targetActive.injuryTerm =
                            Simulator.random.nextInt(Params.maxInjuryTerm + 1);
                    targetActive.isActive = false;
                    targetActive.jailTerm =
                            Simulator.random.nextInt(Params.maxJailTerm + 1);
                }
                else this.injuryTerm =
                        Simulator.random.nextInt(Params.maxInjuryTerm + 1);

            }
            else {
                targetActive.isActive = false;
                targetActive.jailTerm =
                        Simulator.random.nextInt(Params.maxJailTerm+1);
            }
        }
        else {
            targetActive.isActive = false;
            targetActive.jailTerm =
                    Simulator.random.nextInt(Params.maxJailTerm+1);
        }
    }

    @Override
    public void go() {
        if(this.injuryTerm == 0) {
            move();
            enforce();
        } else this.injuryTerm--;
    }

    @Override
    public String toString() {
        return "Cop{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", injuryTerm=" + injuryTerm +
                '}';
    }
}
