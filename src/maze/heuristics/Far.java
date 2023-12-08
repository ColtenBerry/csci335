package maze.heuristics;
import java.util.function.ToIntFunction;

import core.Pos;
import maze.core.MazeExplorer;


public class Far implements ToIntFunction<MazeExplorer>{

    @Override
    public int applyAsInt(MazeExplorer value) {
//        int y_distance;
//        int x_distance;
//        int result;
//        x_distance = Math.abs(value.getLocation().getX());
//        y_distance = Math.abs(value.getLocation().getY());
//        result = x_distance + y_distance;
//        return result;
        int far = Integer.MIN_VALUE;
        for (Pos treasure : value.getAllTreasureFromMaze()) {
            if(value.getLocation().getManhattanDist(treasure) > far && !value.getAllTreasureFound().contains(treasure)){
                far = value.getLocation().getManhattanDist(treasure);
            }
        }
        if (far == Integer.MIN_VALUE) {
            return value.getLocation().getManhattanDist(value.getGoal().getLocation());
        }
        return far;
    }
}
