package maze.heuristics;
import java.util.function.ToIntFunction;
import java.lang.Math;
import maze.core.MazeExplorer;


public class DumbV2 implements ToIntFunction<MazeExplorer>{
    @Override
    public int applyAsInt(MazeExplorer value) {
        int y_distance;
        int x_distance;
        int result;
        x_distance = 7 * (Math.abs(value.getLocation().getX() - value.getGoal().getLocation().getX()));
        y_distance = (Math.abs(value.getLocation().getY() - value.getGoal().getLocation().getX()) - 16);
        result = x_distance + y_distance;
        return result;
    }
}
