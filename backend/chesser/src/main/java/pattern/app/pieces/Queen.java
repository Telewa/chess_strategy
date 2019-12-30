package pattern.app.pieces;

import pattern.app.utils.Color;
import pattern.app.utils.Coordinate;
import pattern.app.team.Team;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public char code() {
        return 'q';
    }

    public Queen(Coordinate coordinate, Team team, Color color) {
        super(coordinate, team, color);
    }

    protected boolean is_long_range_piece() {
        return true;
    }

    public List<String> possible_moves() {

        List<String> possible_moves = new ArrayList<>();

        //just like a rook
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 0, 1);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 0, -1);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 1, 0);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, -1, 0);

        //just like a bishop
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, -1, 1);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, -1, -1);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 1, -1);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 1, 1);

        return possible_moves;
    }
}
