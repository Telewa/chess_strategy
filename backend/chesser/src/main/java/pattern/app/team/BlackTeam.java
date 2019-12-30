package pattern.app.team;

import pattern.app.Chess;
import pattern.app.utils.Color;
import pattern.app.utils.Coordinate;
import pattern.app.pieces.*;

import static pattern.app.Chess.board;

public class BlackTeam extends CommonTeam implements Team {

    public int[] home_space() {
        return new int[]{8, 7};
    }

    protected Color color() {
        return Color.BLACK;
    }

    public char format(char code) {
        return Character.toLowerCase(code);
    }

    public BlackTeam() { super(); init();}
    public BlackTeam(String fen) {
        //expand the fen string
        fen = expand_fen_string(fen);

        int position = 0;

        for (int row : Chess.rows) {
            for (char col : Chess.columns) {
                Coordinate coordinate = new Coordinate(col, row);

                if (fen.charAt(position) == '/') position++;

                switch (fen.charAt(position)) {
                    case 'r':
                        board.put(coordinate.toString(), new Rook(coordinate, this, color()));
                        break;
                    case 'n':
                        board.put(coordinate.toString(), new Knight(coordinate, this, color()));
                        break;
                    case 'b':
                        board.put(coordinate.toString(), new Bishop(coordinate, this, color()));
                        break;
                    case 'q':
                        board.put(coordinate.toString(), new Queen(coordinate, this, color()));
                        break;
                    case 'k':
                        board.put(coordinate.toString(), new King(coordinate, this, color()));
                        break;
                    case 'p':
                        board.put(coordinate.toString(), new Pawn(coordinate, this, color()));
                        break;
                }

                position++;

            }

        }
    }

    public int next_row(int current, int steps){
        return current - steps;
    }
    public char next_column(char current, int steps){
        return (char) ((int)current - (int)steps);
    }
}
