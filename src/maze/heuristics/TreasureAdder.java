package maze.heuristics;
import core.Pos;
import maze.core.MazeExplorer;
import java.util.function.ToIntFunction;
import java.lang.Math;
//non monotonic
public class TreasureAdder implements ToIntFunction<MazeExplorer>{

    @Override
    public int applyAsInt(MazeExplorer value) {
        int y_distance = 0;
        int x_distance = 0;
        int result;
        for (Pos treasure: value.getAllTreasureFromMaze()) {
            for (Pos treasure_found: value.getAllTreasureFound()) {
                if (treasure != treasure_found) {
                    x_distance += Math.abs(value.getLocation().getX() - treasure.getX());
                    y_distance += Math.abs(value.getLocation().getY() - treasure.getY());
                    result = x_distance + y_distance;
                    return result;
                }
            }
        }
        x_distance += Math.abs(value.getLocation().getX() - value.getGoal().getLocation().getX());
        y_distance += Math.abs(value.getLocation().getY() - value.getGoal().getLocation().getY());
        result = x_distance + y_distance;
        System.out.println(result);
        return result;
    }
    //monotonic
    /*
    Monotonicity means that when the explorer moves 1 place, the estimate moves 1 place.
    Non - monotonic example would be if all the distances to treasures were added together. That way,
    moving one square towards multiple treasures would decrease the distance by more than one

    the way the queue works is it adds all successors' heuristics to a heap, picks the best, then
    it adds all of its successors' heuristics to the heap and they all compete against each other. It
    picks the best and repeats
     */
}
