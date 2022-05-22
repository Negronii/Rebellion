import java.util.ArrayList;
import java.util.Objects;

public class Coord {
    // x-axis position of this coordinate
    public int x;
    // y-axis position of this coordinate
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

    // rewrite equals so that it will not only compare by object id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

    // rewrite hashCode so that it will not only compare by object id
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
