package pattern.app.team;

import pattern.app.Chess;
import pattern.app.utils.Color;
import pattern.app.utils.Coordinate;
import pattern.app.pieces.*;

import static pattern.app.Chess.board;

public class WhiteTeam extends CommonTeam implements Team {

    public int[] home_space() {
        return new int[]{1, 2};
    }

    protected Color color() {
        return Color.WHITE;
    }

    public char format(char code) {
        return Character.toUpperCase(code);
    }

    public WhiteTeam() {
        super();
        init();
    }

    public WhiteTeam(String fen) {
        //expand the fen string
        fen = expand_fen_string(fen);

        int position = 0;

        for (int row : Chess.rows) {
            for (char col : Chess.columns) {
                Coordinate coordinate = new Coordinate(col, row);

                if (fen.charAt(position) == '/') position++;

                switch (fen.charAt(position)) {
                    case 'R':
                        board.put(coordinate.toString(), new Rook(coordinate, this, color()));
                        break;
                    case 'N':
                        board.put(coordinate.toString(), new Knight(coordinate, this, color()));
                        break;
                    case 'B':
                        board.put(coordinate.toString(), new Bishop(coordinate, this, color()));
                        break;
                    case 'Q':
                        board.put(coordinate.toString(), new Queen(coordinate, this, color()));
                        break;
                    case 'K':
                        board.put(coordinate.toString(), new King(coordinate, this, color()));
                        break;
                    case 'P':
                        board.put(coordinate.toString(), new Pawn(coordinate, this, color()));
                        break;
                }

                position++;

            }

        }
    }

    public int next_row(int current, int steps) {
        return current + steps;
    }

    public char next_column(char current, int steps) {
        return (char) ((int) current + steps);
    }

}