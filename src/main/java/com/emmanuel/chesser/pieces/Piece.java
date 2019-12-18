package com.emmanuel.chesser.pieces;


import com.emmanuel.chesser.Coordinate;

import java.util.List;
import java.util.Map;

public abstract class Piece {
    public enum COLOR {
        WHITE, BLACK
    }

    protected int base_row;

    // Rook, Bishop, Queen are long range
    // Pawn, Knight, King are short range

    public Character code = ' ';
    public COLOR color;

    public Piece(COLOR color, Character code) {
        this.code = color == COLOR.WHITE ? Character.toUpperCase(code) : Character.toLowerCase(code);
        this.color = color;
        base_row = this.color == COLOR.WHITE ? 2 : 7;
    }

    @Override
    public String toString() {
        return String.format("%c", code);
    }

    /**
     * Given the the chess board and a coordinate on that board,
     * return the list of possible destinations for that piece
     *
     * @param board        The chess board of the current game
     * @param coordinate   The coordiate of the piece on the chess board
     * @param castle_state
     * @return list of possible destinations for that piece
     */
    public abstract List<Coordinate> possible_moves(Map<String, Piece> board, Coordinate coordinate, COLOR original_color, Map<COLOR, Map<String, Boolean>> castle_state, boolean is_long_range_piece);

    public final int next_row(int current, int steps) {
        if (this.color == COLOR.WHITE) {
            return current + steps;
        } else {
            return current - steps;
        }
    }

    public final char next_column(char current, int steps) {
        if (this.color == COLOR.WHITE) {
            return (char) (((int) current) + steps);
        } else {
            return (char) (((int) current) - steps);
        }
    }

    public void line_of_sight(
            List<Coordinate> possible_moves,
            Map<String, Piece> board,
            boolean is_long_range_piece,
            Piece.COLOR original_color,
            Coordinate current,
            int column_direction,
            int row_direction
    ) {

        Coordinate top_left = new Coordinate(next_column(current.column, column_direction), next_row(current.row, row_direction));

        if (board.containsKey(top_left.toString())) {
            String top_left_coordinate = new Coordinate(top_left.column, top_left.row).toString();
            Piece top_left_piece = board.get(top_left_coordinate);

            if (top_left_piece == null) {
                possible_moves.add(top_left);
                // if this piece can see farther than a step
                if (is_long_range_piece) {
                    line_of_sight(possible_moves, board, is_long_range_piece, original_color, top_left, column_direction, row_direction);
                }
            } else if (top_left_piece.color != original_color) {
                possible_moves.add(top_left);
            }
        }
    }
}
