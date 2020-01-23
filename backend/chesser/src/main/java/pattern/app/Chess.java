package pattern.app;


import pattern.app.pieces.*;
import pattern.app.team.BlackTeam;
import pattern.app.team.WhiteTeam;
import pattern.app.utils.Color;
import pattern.app.utils.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Chess {
    public static final int[] rows = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
    public static final char[] columns = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    public static final char[] promotion_pieces = new char[]{'Q', 'R', 'B', 'N'};

    public static Map<String, Piece> board;
    public BlackTeam black;
    public WhiteTeam white;


    //these MUST be reset in the constructor: TODO: FIX this
    public static Map<Color, Map<String, Boolean>> castle_state = new HashMap<>();
    public static String en_passant_target_square;

    protected int max_move_count_before_capture = 51;
    protected int move_count_before_capture = 0;
    protected char turn = 'w';
    protected int full_move_number = 1;

    protected void reset_no_fen(){
        //static variables stuff
        board = new HashMap<>();
        en_passant_target_square = "-";

        white = new WhiteTeam();
        black = new BlackTeam();

        //set the initial castle state
        Map<String, Boolean> black_castle = new HashMap<>();
        black_castle.put("long", false);
        black_castle.put("short", false);

        Map<String, Boolean> white_castle = new HashMap<>();
        white_castle.put("long", false);
        white_castle.put("short", false);

        castle_state.put(Color.BLACK, black_castle);
        castle_state.put(Color.WHITE, white_castle);
    }

    protected void reset_with_fen(String fen){
        //static variables stuff
        board = new HashMap<>();
        en_passant_target_square = "-";

        String[] split = fen.split(" ");

        black = new BlackTeam(split[0]);
        white = new WhiteTeam(split[0]);

        //just check that we have intepreted the fen string as it was given
        assert fen().equals(fen) : "Game misinterpreted";

        turn = split[1].charAt(0);

        //castling ability
        Map<String, Boolean> black_castle = new HashMap<>();
        black_castle.put("long", false);
        black_castle.put("short", false);

        Map<String, Boolean> white_castle = new HashMap<>();
        white_castle.put("long", false);
        white_castle.put("short", false);

        if (split[2].contains("K")) white_castle.put("short", true);
        if (split[2].contains("Q")) white_castle.put("long", true);
        if (split[2].contains("k")) black_castle.put("short", true);
        if (split[2].contains("q")) black_castle.put("long", true);

        castle_state.put(Color.WHITE, white_castle);
        castle_state.put(Color.BLACK, black_castle);

        en_passant_target_square = split[3];

        move_count_before_capture = Integer.parseInt(split[4]);

        full_move_number = Integer.parseInt(split[5]);
    }

    public Chess() {
        this.reset_no_fen();
    }

    public Chess(String fen) {
        reset_with_fen(fen);
    }

    /**
     * Get the current board position as a fen string
     * https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     *
     * @return The fen string
     */
    public final String fen() {
        StringBuilder fen = new StringBuilder();

        //part 1: Piece placement
        for (int row : rows) {
            for (char col : columns) {
                String coordinate = String.format("%c%d", col, row);
                Piece piece = board.get(coordinate);
                fen.append(piece == null ? "1" : piece);
            }
            if (row != 1) fen.append("/");
        }

        //part 2: Active color
        fen.append(String.format(" %c ", turn));

        //part 3: Castling availability
        StringBuilder castling = new StringBuilder();

        if (castle_state.get(Color.WHITE).get("short")) castling.append(String.format("%c", 'K'));
        if (castle_state.get(Color.WHITE).get("long")) castling.append(String.format("%c", 'Q'));

        if (castle_state.get(Color.BLACK).get("short")) castling.append(String.format("%c", 'k'));
        if (castle_state.get(Color.BLACK).get("long")) castling.append(String.format("%c", 'q'));

        fen.append(castling.length() > 0 ? castling : " - ");

        //part 4: En passant target square in algebraic notation
        fen.append(String.format(" %s ", en_passant_target_square));

        //part 5: Halfmove clock: This is the number of halfmoves since the last capture or pawn advance. This is used to determine if a draw can be claimed under the fifty-move rule.
        fen.append(String.format(" %d ", move_count_before_capture));

        //part 6: Fullmove number: The number of the full move. It starts at 1, and is incremented after Black's move.
        fen.append(String.format(" %d ", full_move_number));

        return condence_fen_string(fen.toString().replaceAll(" {2}", " "));
    }

    /**
     * Find all the series of 1's in the fen string and replace with their count/sum
     *
     * @param fen_string The long format of the fen string
     * @return The condensed version of the fen string
     */
    private String condence_fen_string(String fen_string) {
        Pattern patt = Pattern.compile("(1+)");
        Matcher m = patt.matcher(fen_string);
        StringBuffer sb = new StringBuffer(fen_string.length());
        while (m.find()) {
            String text = m.group(1);
            String replacement = String.format("%d", text.length());
            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);
        return sb.toString();
    }


    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        for (int row : rows) {
            response.append(String.format("%d ", row));
            for (char column : columns) {
                Piece piece = board.get(new Coordinate(column, row).toString());
                response.append(String.format("%s ", piece != null ? piece : "."));
            }
            response.append("\n");
        }
        response.append("  ");
        for (char col : columns) {
            response.append(String.format("%c ", col));
        }
        response.append("\n\n");
        return response.toString();
    }

    public static boolean within_board(Coordinate coordinate) {
        if (coordinate.row > 0) if (coordinate.row < 9) if (coordinate.column > columns[0] - 1)
            return coordinate.column < columns[columns.length - 1] + 1;
        return false;
    }

    public final List<String> possible_moves() {
        if (move_count_before_capture < max_move_count_before_capture) {
            if (turn == 'b') {
                return black.possible_moves();
            } else {
                return white.possible_moves();
            }
        }//games should not go on for ever
        else return new ArrayList<>();
    }

    /**
     * Make a move on the board!
     *
     * @param move The String representation of the move
     * @return True if move was made, False otherwise
     */
    public boolean makeMove(String move) {
        Pattern move_pattern = Pattern.compile("([KkQqBbNnRrPp])([a-h][1-8])[x\\-]([a-h][1-8])(=([QNBRqnrb]))?[+#]?");
        Matcher m = move_pattern.matcher(move);
        boolean move_made = false;
        if (m.matches()) {

            String from_coord = String.format("%s", m.group(2));
            String to_coord = String.format("%s", m.group(3));

            Piece original_piece = board.get(from_coord);
            Piece dest_piece = board.get(to_coord);

            board.put(from_coord, null);
            original_piece.setCoordinate(new Coordinate(to_coord));


            //what if it was a promotion?
            try{
                char promote_to =Character.toLowerCase(m.group(5).charAt(0));

                switch (promote_to){
                    case 'q': original_piece= new Queen(new Coordinate(to_coord), original_piece.team, original_piece.color); break;
                    case 'r': original_piece= new Rook(new Coordinate(to_coord), original_piece.team, original_piece.color); break;
                    case 'n': original_piece= new Knight(new Coordinate(to_coord), original_piece.team, original_piece.color); break;
                    case 'b': original_piece= new Bishop(new Coordinate(to_coord), original_piece.team, original_piece.color); break;
                }
            }
            catch (Exception ignored){

            }
            board.put(to_coord, original_piece);

            //todo
            //what if it is en-passant?

            //what if it is castling?

            //update whose turn to play
            turn = turn == 'w' ? 'b' : 'w';
            move_made = true;
        }

        return move_made;
    }
}
