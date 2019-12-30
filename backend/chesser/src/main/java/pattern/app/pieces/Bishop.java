package pattern.app.pieces;

import pattern.app.utils.Color;
import pattern.app.utils.Coordinate;
import pattern.app.team.Team;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public char code() {
        return 'b';
    }

    protected boolean is_long_range_piece(){
        return true;
    }

    public Bishop(Coordinate coordinate, Team team, Color color) {
        super(coordinate, team, color);
    }

    public List<String> possible_moves() {

        List<String> possible_moves = new ArrayList<>();

        //check the barriers
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, -1, 1);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, -1, -1);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 1, -1);
        line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 1, 1);

        return possible_moves;
    }
}
