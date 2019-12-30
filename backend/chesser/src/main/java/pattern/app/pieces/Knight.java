package pattern.app.pieces;

import pattern.app.utils.Color;
import pattern.app.utils.Coordinate;
import pattern.app.team.Team;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public char code() {
        return 'n';
    }

    protected boolean is_long_range_piece() {
        return false;
    }

    public Knight(Coordinate coordinate, Team team, Color color) {
        super(coordinate, team, color);
    }

    public List<String> possible_moves() {

        List<String> possible_moves = new ArrayList<>();

        //left
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, -1, 2);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, -1, -2);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, -2, -1);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, -2, 1);

        //right
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 1, 2);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 1, -2);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 2, -1);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 2, 1);

        return possible_moves;
    }
}
