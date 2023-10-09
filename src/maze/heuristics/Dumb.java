package maze.heuristics;
import java.util.function.ToIntFunction;
import java.lang.Math;
import maze.core.MazeExplorer;


public class Dumb implements ToIntFunction<MazeExplorer>{

    @Override
    public int applyAsInt(MazeExplorer value) {
        int y_distance;
        int x_distance;
        int result;
        x_distance = Math.abs(value.getLocation().getX());
        y_distance = Math.abs(value.getLocation().getY());
        result = x_distance + y_distance;
        System.out.println(result);
        return result;
    }
}
