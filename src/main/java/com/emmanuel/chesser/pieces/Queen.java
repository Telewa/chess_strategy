package com.emmanuel.chesser.pieces;


import com.emmanuel.chesser.Coordinate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Queen extends Piece {
    public Queen(COLOR color, Character code) {
        super(color, code);
    }

    @Override
    public List<Coordinate> possible_moves(Map<String, Piece> board, Coordinate coordinate, COLOR original_color, Map<COLOR, Map<String, Boolean>> castle_state, boolean is_long_range_piece) {

        // a Queen is basically a rook and bishop combined

        Piece Rook = new Rook(this.color, 'r');
        Piece Bishop = new Bishop(this.color, 'b');

        return Stream.concat(
                Rook.possible_moves(board, coordinate, original_color, castle_state, is_long_range_piece).stream(),
                Bishop.possible_moves(board, coordinate, original_color, castle_state, is_long_range_piece).stream()
        ).collect(Collectors.toList());
    }
}
