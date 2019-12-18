package com.emmanuel.chesser.pieces;


import com.emmanuel.chesser.Coordinate;

import java.util.List;
import java.util.Map;

public class King extends Piece {

    public enum CASTLE {
        LONG(-1), SHORT(1);
        public int castle_direction;

        CASTLE(int castle_direction) {
            this.castle_direction = castle_direction;
        }
    }

    public King(COLOR color, Character code) {
        super(color, code);
    }

    private Coordinate[] rook_coordinates() {
        Coordinate[] rook_coordinates = new Coordinate[2];
        if (color == COLOR.BLACK) {
            rook_coordinates[0] = new Coordinate('a', 8);
            rook_coordinates[1] = new Coordinate('h', 8);
        } else {
            rook_coordinates[0] = new Coordinate('a', 1);
            rook_coordinates[1] = new Coordinate('h', 1);
        }
        return rook_coordinates;

    }

    /**
     * Get the castle square after checking that the king can castle
     *
     * @param board          The current board of the game
     * @param coordinate     The coordinate of the King
     * @param possible_moves The list of moves were to add if can castle
     * @param castle_state
     */
    private void castle(Map<String, Piece> board, Coordinate coordinate, List<Coordinate> possible_moves, Map<COLOR, Map<String, Boolean>> castle_state, int direction) {

        // for black the directions for columns are in reverse
        int step = Integer.signum(direction);

        Coordinate[] rook_coordinates = rook_coordinates();

        for (Coordinate rook_coordinate : rook_coordinates) {
            Piece rook = board.get(rook_coordinate.toString());

            if (rook instanceof Rook) {

                List<Coordinate> my_possible_moves = rook.possible_moves(board, rook_coordinate, rook.color, castle_state, true);
                Coordinate neighbour = new Coordinate(next_column(coordinate.column, 1 * step), coordinate.row);

                if (my_possible_moves.stream().anyMatch(o -> o.toString().equals(neighbour.toString()))) {
                    possible_moves.add(new Coordinate(next_column(coordinate.column, 2 * step), coordinate.row));
                }
            }
        }

    }


    @Override
    public List<Coordinate> possible_moves(Map<String, Piece> board, Coordinate coordinate, COLOR original_color, Map<COLOR, Map<String, Boolean>> castle_state, boolean is_long_range_piece) {
        // a King is basically a queen but short range and can castle but cannot move to line of fire
        Piece Queen = new Queen(this.color, 'r');

        List<Coordinate> possible_moves = Queen.possible_moves(board, coordinate, original_color, castle_state, false);

        if (castle_state.get(color).get("long")) {
            castle(board, coordinate, possible_moves, castle_state, CASTLE.LONG.castle_direction);
        }
        if (castle_state.get(color).get("short")) {
            castle(board, coordinate, possible_moves, castle_state, CASTLE.SHORT.castle_direction);
        }

        return possible_moves;
    }
}
