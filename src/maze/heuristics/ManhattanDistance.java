package maze.heuristics;
import maze.core.Maze;
import maze.core.MazeExplorer;
import java.util.function.ToIntFunction;
import java.lang.Math;

public class ManhattanDistance implements ToIntFunction<MazeExplorer>{
    @Override
    public int applyAsInt(MazeExplorer value) {
        int y_distance;
        int x_distance;
        int result;
        x_distance = Math.abs(value.getLocation().getX() - value.getGoal().getLocation().getX());
        y_distance = Math.abs(value.getLocation().getY() - value.getGoal().getLocation().getY());
        result = x_distance + y_distance;
        System.out.println(result);

        return result;
    }
}

/*
All of my heuristics return similar results, despite my dumb ones being completely illogical
I am not sure if I'm not using the GUI correctly or if I just don't understand how it words

 */