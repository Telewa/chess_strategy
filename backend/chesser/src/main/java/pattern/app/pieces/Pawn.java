package pattern.app.pieces;

import pattern.app.Chess;
import pattern.app.utils.Color;
import pattern.app.utils.Coordinate;
import pattern.app.team.Team;

import java.util.ArrayList;
import java.util.List;

import static pattern.app.Chess.board;
import static pattern.app.Chess.promotion_pieces;


public class Pawn extends Piece {

    public char code() {
        return 'p';
    }

    public Pawn(Coordinate coordinate, Team team, Color color) {
        super(coordinate, team, color);
    }

    protected boolean is_long_range_piece() {
        return false;
    }

    /**
     * @param myself The pawn in question
     * @return true if i am at the seventh rank, false otherwise
     */
    private boolean is_at_the_seventh_rank(Piece myself) {
        return (myself.coordinate.row == 2 || myself.coordinate.row == 7) && myself.coordinate.row != myself.team.home_space()[1];
    }

    public int value(){return 1;}

    public List<String> possible_moves() {

        List<String> possible_moves = new ArrayList<>();

        Piece myself = board.get(coordinate.toString());
        Piece next_piece = board.get(new Coordinate(coordinate.column, myself.team.next_row(coordinate.row, 1)).toString());


        //if the next square on the same column is empty, a pawn can move to that
        if (next_piece == null) {
            if (is_at_the_seventh_rank(myself)) {

                //a move that will result in a promotion
                for (char promo : promotion_pieces) {
                    possible_moves.add(
                            promote_notation(new Coordinate(coordinate.column, myself.team.next_row(coordinate.row, 1)), promo)
                    );
                }

            } else {
                //normal pawn move
                line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 0, 1);
            }
        }


        // if the pawn is in base_row and row base_row+2 is empty it can move to row base_row+2
        if (next_piece == null && (coordinate.row == myself.team.home_space()[1]) &&
                board.get(new Coordinate(coordinate.column, myself.team.next_row(coordinate.row, 2)).toString()) == null
        ) {
            possible_moves.add(
                    new Coordinate(coordinate.column, myself.team.next_row(coordinate.row, 2)).toString()
            );
        }
        // captures

        //if the next right is an opponent's piece then, a pawn can capture it.

        Coordinate right_target_coordinates = new Coordinate(myself.team.next_column(coordinate.column, 1), myself.team.next_row(coordinate.row, 1));
        next_piece = board.get(
                right_target_coordinates.toString()
        );
        if (next_piece != null && next_piece.color != color) {

            //if we are at the seventh, then it's a promotion as well
            if (is_at_the_seventh_rank(myself)) {

                //a capture to the right that will result in a promotion
                for (char promo : promotion_pieces) {
                    possible_moves.add(
                            capture_notation(promote_notation(new Coordinate(myself.team.next_column(coordinate.column, 1), myself.team.next_row(coordinate.row, 1)), promo))
                    );
                }

            } else {

                //otherwise normal capture
                line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, 1, 1);
            }

        } else if (next_piece == null && right_target_coordinates.toString().equals(Chess.en_passant_target_square)) {
            //i can capture it using en passant move
            possible_moves.add(capture_notation(right_target_coordinates.toString()));
        }


        //if the next left is an opponent's piece then, a pawn can capture it.
        Coordinate left_target_coordinates = new Coordinate(myself.team.next_column(coordinate.column, -1), myself.team.next_row(coordinate.row, 1));

        next_piece = board.get(
                left_target_coordinates.toString()
        );
        if (next_piece != null && next_piece.color != color) {

            //if we are at the seventh, then it's a promotion as well
            if (is_at_the_seventh_rank(myself)) {

                //a capture to the left that will result in a promotion
                for (char promo : promotion_pieces) {
                    possible_moves.add(
                            capture_notation(promote_notation(new Coordinate(myself.team.next_column(coordinate.column, -1), myself.team.next_row(coordinate.row, 1)), promo))
                    );
                }

            } else {

                //otherwise normal capture
                line_of_sight(possible_moves, is_long_range_piece(), color, coordinate, -1, 1);
            }

        } else if (next_piece == null && left_target_coordinates.toString().equals(Chess.en_passant_target_square)) {
            //i can capture it using en passant move
            possible_moves.add(capture_notation(left_target_coordinates.toString()));
        }

        return possible_moves;
    }
}
