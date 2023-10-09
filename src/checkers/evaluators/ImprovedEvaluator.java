package checkers.evaluators;

import checkers.core.Checkerboard;
import checkers.core.PlayerColor;

import java.util.function.ToIntFunction;

public class ImprovedEvaluator implements ToIntFunction<Checkerboard> {
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
        int king_factor = 5;
        int piece_factor = 3;
        int location_factor = 1;
        int num_curr_player_kings = value.numKingsOf(curr_player);
        int num_opp_player_kings = value.numKingsOf(opp_player);
        int num_curr_player = value.numPiecesOf(curr_player);
        int num_opp_player = value.numPiecesOf(opp_player);
        int curr_player_total = (num_curr_player * piece_factor) + (num_curr_player_kings * king_factor);
        int opp_player_total = (num_opp_player * piece_factor) + (num_opp_player_kings * king_factor);
        if (value.colorAt(7, 2, PlayerColor.BLACK)) {
            if (curr_player == PlayerColor.BLACK) {
                curr_player_total += 1.5;
            }
            else {
                opp_player_total += 1.5;
            }
        }
        if (value.colorAt(7, 6, PlayerColor.BLACK)) {
            if (curr_player == PlayerColor.BLACK) {
                curr_player_total += 1.5;
            }
            else {
                opp_player_total += 1.5;
            }
        }
        if (value.colorAt(0, 1, PlayerColor.RED)) {
            if (curr_player == PlayerColor.RED) {
                curr_player_total += 1.5;
            }
            else {
                opp_player_total += 1.5;
            }
        }
        if (value.colorAt(0, 5, PlayerColor.RED)) {
            if (curr_player == PlayerColor.RED) {
                curr_player_total += 1.5;
            }
            else {
                opp_player_total += 1.5;
            }
        }

        int difference = curr_player_total - opp_player_total;
        return difference;
    }
}
