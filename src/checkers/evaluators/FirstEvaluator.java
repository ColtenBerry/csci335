package checkers.evaluators;

import checkers.core.Checkerboard;
import checkers.core.PlayerColor;

import java.util.function.ToIntFunction;

public class FirstEvaluator implements ToIntFunction<Checkerboard> {
    @Override
    public int applyAsInt(Checkerboard value) {
        PlayerColor curr_player = value.getCurrentPlayer();
        PlayerColor opp_player;
        if (curr_player == PlayerColor.BLACK) {
            opp_player = PlayerColor.RED;
        }
        else {
            opp_player = PlayerColor.BLACK;
        }
        int num_curr_player = value.numPiecesOf(curr_player);
        int num_opp_player = value.numPiecesOf(opp_player);
        int difference = num_curr_player - num_opp_player;

        return difference;
    }
}
