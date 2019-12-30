package pattern.app.team;

import pattern.app.pieces.Piece;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pattern.app.Chess.board;

public interface Team {

    /**
     * @return The list of players for this team
     */
    default Map<String, Piece> getPieces() {
        Map<String, Piece> my_team = new HashMap<>();

        for (String coord : board.keySet()) {
            Piece piece = board.get(coord);
            if (piece != null && piece.team == this) {
                my_team.put(coord, piece);
            }
        }
        return my_team;
    }

    char format(char code);

    int[] home_space();

    //every team must implement the mechanism for getting next row and column
    int next_row(int current, int steps);
    char next_column(char current, int steps);

    List<String> possible_moves();
}
