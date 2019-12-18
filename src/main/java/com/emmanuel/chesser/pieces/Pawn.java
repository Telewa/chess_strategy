package com.emmanuel.chesser.pieces;


import com.emmanuel.chesser.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Pawn extends Piece {

    public Pawn(COLOR color, Character code) {
        super(color, code);
    }

    @Override
    public List<Coordinate> possible_moves(Map<String, Piece> board, Coordinate coordinate, COLOR original_color, Map<COLOR, Map<String, Boolean>> castle_state, boolean is_long_range_piece) {
        List<Coordinate> possible_moves = new ArrayList<>();

        Piece next_piece = board.get(new Coordinate(coordinate.column, next_row(coordinate.row, 1)).toString());

        //if the next square on the same column is empty, a pawn can move to that
        if (next_piece == null) {
            possible_moves.add(
                    new Coordinate(coordinate.column, next_row(coordinate.row, 1))
            );
        }

        // if the pawn is in base_row and row base_row+2 is empty it can move to row base_row+2
        if (
                next_piece == null &&
                        (coordinate.row == base_row) &&
                        board.get(new Coordinate(coordinate.column, next_row(coordinate.row, 2)).toString()) == null
        ) {
            possible_moves.add(
                    new Coordinate(coordinate.column, next_row(coordinate.row, 2))
            );
        }

        // captures

        //if the next right is an opponent's piece then, a pawn can capture it.
        next_piece = board.get(
                new Coordinate((char) ((int) coordinate.column + 1), next_row(coordinate.row, 1)).toString()
        );
        if (next_piece != null && next_piece.color != original_color) {
            possible_moves.add(
                    new Coordinate((char) ((int) coordinate.column + 1), next_row(coordinate.row, 1))
            );
        }

        //if the next left is an opponent's piece then, a pawn can capture it.
        next_piece = board.get(
                new Coordinate((char) ((int) coordinate.column - 1), next_row(coordinate.row, 1)).toString()
        );
        if (next_piece != null && next_piece.color != original_color) {
            possible_moves.add(
                    new Coordinate((char) ((int) coordinate.column - 1), next_row(coordinate.row, 1))
            );
        }

        return possible_moves;
    }

}
