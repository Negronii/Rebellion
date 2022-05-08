import java.util.ArrayList;
import java.util.Objects;

public class Coord {
    public int x;
    public int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // find all neighbours including its coordinate
    public static ArrayList<Coord> findNeighbour(Coord coord) {
        ArrayList<Coord> neighbour = new ArrayList<>();
        int vision = (int) Params.vision;
        for (int i = 0; i < 2 * vision + 1; i++) {
            for (int j = 0; j < 2 * vision + 1; j++) {
                // if distance to centre - error range < vision
                if (Math.sqrt((i - vision) * (i - vision) +
                        (j - vision) * (j - vision))
                        - 0.0000001 < Params.vision) {
                    int adjustedX = (int) (i - Params.vision + coord.x +
                            Params.width) % Params.width;
                    int adjustedY = (int) (j - Params.vision + coord.y +
                            Params.length) % Params.length;
                    neighbour.add(new Coord(adjustedX, adjustedY));
                }
            }
        }
        return neighbour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coord{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
