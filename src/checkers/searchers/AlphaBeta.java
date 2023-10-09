package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import checkers.core.PlayerColor;
import checkers.gui.AutoCheckers;
import core.Duple;
import javafx.util.Pair;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class AlphaBeta extends CheckersSearcher {
    public AlphaBeta(ToIntFunction<Checkerboard> e) {
        super(e);
    }
    int numNodes = 0;
    int depth_limit = 12;
    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return selectMoveHelper(board, depth_limit, -Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    public Optional<Duple<Integer, Move>> selectMoveHelper(Checkerboard board, int depth_limit, int alpha, int beta) {
        Optional<Duple<Integer, Move>> best = Optional.empty();
        Optional<Duple<Integer, Move>> new_move = Optional.empty();
        PlayerColor current_player = board.getCurrentPlayer();
        PlayerColor opposite_player;
        int evaluation;
        if (current_player == PlayerColor.BLACK) {
            opposite_player = PlayerColor.RED;
        }
        else {
            opposite_player = PlayerColor.BLACK;
        }
        if (board.gameOver()) {
            if(board.playerWins(current_player)) {
                new_move = Optional.of(new Duple<>(Integer.MAX_VALUE, board.getLastMove()));
                return new_move;
            }
            else if (board.playerWins(opposite_player)) {
                new_move = Optional.of(new Duple<>(-Integer.MAX_VALUE, board.getLastMove()));
                return new_move;
            }
            else {
                new_move = Optional.of(new Duple<>(0, board.getLastMove()));
                return new_move;
            }
        }
        else if (depth_limit == 0) {
            evaluation = getEvaluator().applyAsInt(board);
            new_move = Optional.of(new Duple<>(evaluation, board.getLastMove()));
            return new_move;
        }
        int best_score = -Integer.MAX_VALUE;
        Move best_move = null;
        for (Checkerboard alternative: board.getNextBoards()) {
            numNodes += 1;
            int negation = board.getCurrentPlayer() != alternative.getCurrentPlayer() ? -1 : 1;
            new_move = selectMoveHelper(alternative, depth_limit - 1, -beta, -alpha);
            int new_score = negation * new_move.get().getFirst();
            if (new_move.isEmpty() || new_score > best_score) {
                best_score = new_score;
                best_move = alternative.getLastMove();
                alpha = new_score;
                if (alpha >= beta) {
                    break;
                }

            }
        }
        best = Optional.of(new Duple<>(best_score, best_move));
        return best;
    }

//have select move as a other function, put the new "helper" function in it.
//this should be a new function. This allows depth limit, alpha, beta, etc. to be parameters
}
