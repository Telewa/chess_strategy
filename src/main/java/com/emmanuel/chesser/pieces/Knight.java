package com.emmanuel.chesser.pieces;


import com.emmanuel.chesser.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Knight extends Piece {
    public Knight(COLOR color, Character code) {
        super(color, code);
    }

    @Override
    public List<Coordinate> possible_moves(Map<String, Piece> board, Coordinate coordinate, COLOR original_color, Map<COLOR, Map<String, Boolean>> castle_state, boolean is_long_range_piece) {
        List<Coordinate> possible_moves = new ArrayList<>();

        //left
        line_of_sight(possible_moves, board, is_long_range_piece, original_color, coordinate, -1, 2);
        line_of_sight(possible_moves, board, is_long_range_piece, original_color, coordinate, -1, -2);
        line_of_sight(possible_moves, board, is_long_range_piece, original_color, coordinate, -2, -1);
        line_of_sight(possible_moves, board, is_long_range_piece, original_color, coordinate, -2, 1);

        //right
        line_of_sight(possible_moves, board, is_long_range_piece, original_color, coordinate, 1, 2);
        line_of_sight(possible_moves, board, is_long_range_piece, original_color, coordinate, 1, -2);
        line_of_sight(possible_moves, board, is_long_range_piece, original_color, coordinate, 2, -1);
        line_of_sight(possible_moves, board, is_long_range_piece, original_color, coordinate, 2, 1);

        return possible_moves;
    }
}
