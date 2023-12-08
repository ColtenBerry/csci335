package maze.heuristics;
import java.util.function.ToIntFunction;

import core.Pos;
import maze.core.MazeExplorer;

public class Close implements ToIntFunction<MazeExplorer> {

    @Override
    public int applyAsInt(MazeExplorer value) {
        int close = Integer.MAX_VALUE;
        for (Pos treasure : value.getAllTreasureFromMaze()) {
            if(value.getLocation().getManhattanDist(treasure) < close && !value.getAllTreasureFound().contains(treasure)){
                close = value.getLocation().getManhattanDist(treasure);
            }
        }
        if (close == Integer.MAX_VALUE) {
            return value.getLocation().getManhattanDist(value.getGoal().getLocation());
        }
        return close;
    }
}
    /*
    Close: 3,210,174
    Far: 3,227,287
    ManDistance: 1,588,400
    Treasure Adder: 2,698,395
     */

