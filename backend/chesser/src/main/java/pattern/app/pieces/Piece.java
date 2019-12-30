package pattern.app.pieces;

import pattern.app.Chess;
import pattern.app.utils.Color;
import pattern.app.utils.Coordinate;
import pattern.app.team.Team;

import java.util.List;

import static pattern.app.Chess.board;

public abstract class Piece {

    //my gps coordinate
    protected Coordinate coordinate;

    // Each piece has a code
    public abstract char code();

    // Each piece has a color
    public final Color color;

    public final Team team;

    protected Piece(Coordinate coordinate, Team team, Color color) {
        this.team = team;
        this.color = color;
        this.coordinate = coordinate;
    }

    /**
     * @return The list of coordinates that this piece can shoot
     */
    public abstract List<String> possible_moves();

    public Coordinate getCoordinate() {
        return coordinate;
    }
    public void setCoordinate(Coordinate coordinate) {

         this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return String.format("%c", this.team.format(code()));
    }

    protected abstract boolean is_long_range_piece();

    public void line_of_sight(
            List<String> possible_moves,
            boolean is_long_range_piece,
            Color original_color,
            Coordinate current,
            int column_direction,
            int row_direction
    ) {

        Coordinate top_left = new Coordinate(this.team.next_column(current.column, column_direction), this.team.next_row(current.row, row_direction));

        if (Chess.within_board(top_left)) {
            String top_left_coordinate = new Coordinate(top_left.column, top_left.row).toString();
            Piece top_left_piece = board.get(top_left_coordinate);

            if (top_left_piece == null) {
                possible_moves.add(top_left.toString());
                // if this piece can see farther than a step
                if (is_long_range_piece) {
                    line_of_sight(possible_moves, is_long_range_piece, original_color, top_left, column_direction, row_direction);
                }
            } else if (top_left_piece.color != original_color) {

                //we can capture it!
                possible_moves.add(capture_notation(top_left.toString()));
            }
        }
    }

    protected final String capture_notation(String destination){
        return String.format("x%s", destination);
    }

    protected final String promote_notation(Coordinate coordinate, char promote_to){
        return String.format("%s=%c", coordinate.toString(), promote_to);
    }
}
