package com.emmanuel.chesser;

import com.emmanuel.chesser.pieces.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class BaseChess {
    protected Map<String, Piece> board = new HashMap<>();
    public static final int[] rows = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
    public static final char[] columns = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    public Map<Piece.COLOR, Map<String, Boolean>> castle_state = new HashMap<>();
    final List<Character> promotion_pieces = new ArrayList<>(Arrays.asList('q', 'r', 'b', 'n'));
    private int max_move_count_before_capture = 51;
    private int move_count_before_capture = 0;
    char turn = 'w';
    String en_passant_target_square = "-";
    int full_move_number = 1;

    public List<String> moves = new ArrayList<>();


    public BaseChess() {

        //populate the coordinates
        for (int row : rows) {
            for (char column : columns) {
                board.put(new Coordinate(column, row).toString(), null);
            }
        }

        //add the pieces
        for (char column : columns) {
            //the Pawns
            board.put(new Coordinate(column, 2).toString(), new Pawn(Piece.COLOR.WHITE, 'p'));
            board.put(new Coordinate(column, 7).toString(), new Pawn(Piece.COLOR.BLACK, 'p'));

            // the Rooks
            if (column == 'a' || column == 'h') {
                board.put(new Coordinate(column, 1).toString(), new Rook(Piece.COLOR.WHITE, 'r'));
                board.put(new Coordinate(column, 8).toString(), new Rook(Piece.COLOR.BLACK, 'r'));
            }

            // the Knights
            else if (column == 'b' || column == 'g') {
                board.put(new Coordinate(column, 1).toString(), new Knight(Piece.COLOR.WHITE, 'n'));
                board.put(new Coordinate(column, 8).toString(), new Knight(Piece.COLOR.BLACK, 'n'));
            }

            // the Bishops
            else if (column == 'c' || column == 'f') {
                board.put(new Coordinate(column, 1).toString(), new Bishop(Piece.COLOR.WHITE, 'b'));
                board.put(new Coordinate(column, 8).toString(), new Bishop(Piece.COLOR.BLACK, 'b'));
            }

            // the Queens
            else if (column == 'd') {
                board.put(new Coordinate(column, 1).toString(), new Queen(Piece.COLOR.WHITE, 'q'));
                board.put(new Coordinate(column, 8).toString(), new Queen(Piece.COLOR.BLACK, 'q'));
            }

            // the Kings
            else if (column == 'e') {
                board.put(new Coordinate(column, 1).toString(), new King(Piece.COLOR.WHITE, 'k'));
                board.put(new Coordinate(column, 8).toString(), new King(Piece.COLOR.BLACK, 'k'));
            }
        }

        //set the initial castle state
        Map<String, Boolean> black_castle = new HashMap<>();
        black_castle.put("long", true);
        black_castle.put("short", true);

        Map<String, Boolean> white_castle = new HashMap<>();
        white_castle.put("long", true);
        white_castle.put("short", true);

        castle_state.put(Piece.COLOR.BLACK, black_castle);
        castle_state.put(Piece.COLOR.WHITE, white_castle);
    }

    BaseChess(String fen) {
        //populate the coordinates

        //expand the fen string
        String[] split = fen.split(" ");
        split[0] = expand_fen_string(split[0]);

        int position = 0;
        for (int row : rows) {
            for (char column : columns) {
                Piece piece = null;

                if (split[0].charAt(position) == '/') position++;

                switch (split[0].charAt(position)) {
                    case 'r':
                        piece = new Rook(Piece.COLOR.BLACK, 'r');
                        break;
                    case 'R':
                        piece = new Rook(Piece.COLOR.WHITE, 'r');
                        break;
                    case 'n':
                        piece = new Knight(Piece.COLOR.BLACK, 'n');
                        break;
                    case 'N':
                        piece = new Knight(Piece.COLOR.WHITE, 'n');
                        break;
                    case 'b':
                        piece = new Bishop(Piece.COLOR.BLACK, 'b');
                        break;
                    case 'B':
                        piece = new Bishop(Piece.COLOR.WHITE, 'b');
                        break;
                    case 'q':
                        piece = new Queen(Piece.COLOR.BLACK, 'q');
                        break;
                    case 'Q':
                        piece = new Queen(Piece.COLOR.WHITE, 'q');
                        break;
                    case 'k':
                        piece = new King(Piece.COLOR.BLACK, 'k');
                        break;
                    case 'K':
                        piece = new King(Piece.COLOR.WHITE, 'k');
                        break;
                    case 'p':
                        piece = new Pawn(Piece.COLOR.BLACK, 'p');
                        break;
                    case 'P':
                        piece = new Pawn(Piece.COLOR.WHITE, 'p');
                        break;
                    case '1':
                        piece = null;
                        break;
                }


                board.put(new Coordinate(column, row).toString(), piece);

                position++;
            }
        }

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

        castle_state.put(Piece.COLOR.WHITE, white_castle);
        castle_state.put(Piece.COLOR.BLACK, black_castle);

        en_passant_target_square = split[3];

        move_count_before_capture = Integer.parseInt(split[4]);

        full_move_number = Integer.parseInt(split[5]);

//        fen = String.join(" ", split);

//        System.out.println(fen);
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

    private void move(String from, String to) {

        if (!from.equals(to)) {
            Piece fromPiece = board.get(from);

            board.put(to, fromPiece);
            board.put(from, null);
        }
    }

    private boolean move(String move) {
        Map<String, String> parsedMove = parseMove(move);
        if (parsedMove != null) {
            String from = parsedMove.get("from");
            String to = parsedMove.get("to");

            this.move(from, to);
            return true;
        } else {
            return false;
        }
    }

    private Map<String, String> parseMove(String move) {
        Pattern p = Pattern.compile("([a-h][1-8])([a-h][1-8])");
        Matcher m = p.matcher(move);
        if (m.matches()) {
            String from = m.group(1);
            String to = m.group(2);

            Map<String, String> parsedMove = new HashMap<>();
            parsedMove.put("from", from);
            parsedMove.put("to", to);

            return parsedMove;
        }
        return null;
    }

    /**
     * Get the current board position as a fen string
     * https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     *
     * @return The fen string
     */
    final String fen() {
        StringBuilder fen = new StringBuilder();

        //part 1: Piece placement
        for (int row : rows) {
            for (char col : columns) {
                String coordinate = String.format("%c%d", col, row);
                Piece piece = board.get(coordinate);
                fen.append(piece == null ? "1" : piece.code);
            }
            if (row != 1) fen.append("/");
        }

        //part 2: Active color
        fen.append(String.format(" %c ", turn));

        //part 3: Castling availability
        StringBuilder castling = new StringBuilder();

        if (castle_state.get(Piece.COLOR.WHITE).get("short")) castling.append(String.format("%c", 'K'));
        if (castle_state.get(Piece.COLOR.WHITE).get("long")) castling.append(String.format("%c", 'Q'));

        if (castle_state.get(Piece.COLOR.BLACK).get("short")) castling.append(String.format("%c", 'k'));
        if (castle_state.get(Piece.COLOR.BLACK).get("long")) castling.append(String.format("%c", 'q'));

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

    /**
     * Find all the series of 1's in the fen string and replace with their count/sum
     *
     * @param fen_string The long format of the fen string
     * @return The condensed version of the fen string
     */
    private String expand_fen_string(String fen_string) {

        Pattern patt = Pattern.compile("([2-8]+)");
        Matcher m = patt.matcher(fen_string);
        StringBuffer sb = new StringBuffer(fen_string.length());

        while (m.find()) {
            String text = m.group(1);
            StringBuilder replacement = new StringBuilder();
            for (int i = 0; i < Integer.parseInt(text); i++) replacement.append("1");
            m.appendReplacement(sb, replacement.toString());
        }
        m.appendTail(sb);

        return sb.toString();
    }


//    private boolean three_fold_repetition(){
//        if(moves.size() > 3 && ){
//
//        }
//    }


    /**
     * This will update the state of the board
     *
     * @param move
     * @return
     */
    boolean make_move(String move) {

        Map<String, String> parsedMove = parseMove(move);
        if (parsedMove != null) {
            String from = parsedMove.get("from");
            String to = parsedMove.get("to");

            Coordinate from_c = new Coordinate(from);
            Coordinate to_c = new Coordinate(to);

            //castle rules
            if (to.equals("a1") || from.equals("a1")) {

                Map<String, Boolean> white_castle = castle_state.get(Piece.COLOR.WHITE);
                white_castle.put("short", false);

                castle_state.put(Piece.COLOR.WHITE, white_castle);
            } else if (to.equals("h1") || from.equals("h1")) {

                Map<String, Boolean> white_castle = castle_state.get(Piece.COLOR.WHITE);
                white_castle.put("long", false);

                castle_state.put(Piece.COLOR.WHITE, white_castle);
            } else if (to.equals("a8") || from.equals("a8")) {

                Map<String, Boolean> black_castle = castle_state.get(Piece.COLOR.BLACK);
                black_castle.put("long", false);

                castle_state.put(Piece.COLOR.BLACK, black_castle);
            } else if (to.equals("h8") || from.equals("h8")) {

                Map<String, Boolean> black_castle = castle_state.get(Piece.COLOR.BLACK);
                black_castle.put("short", false);

                castle_state.put(Piece.COLOR.BLACK, black_castle);
            } else if (from.equals("e1")) {//first white king move
                Map<String, Boolean> white_castle = castle_state.get(Piece.COLOR.WHITE);
                white_castle.put("long", false);
                white_castle.put("short", false);

                castle_state.put(Piece.COLOR.WHITE, white_castle);
            } else if (from.equals("e8")) {//first black king move
                Map<String, Boolean> black_castle = castle_state.get(Piece.COLOR.BLACK);
                black_castle.put("short", false);
                black_castle.put("long", false);

                castle_state.put(Piece.COLOR.BLACK, black_castle);
            }

            // if the piece is a king and it's a castle, we need to move the rook as well
            Piece piece = board.get(from);
            Piece captured_piece = board.get(to);
            if (captured_piece != null || piece instanceof Pawn) {//an actual capture
                //the 50-50 rule
                move_count_before_capture = 0;
            } else {
                move_count_before_capture += 1;
            }

            //push the move down the move stack
            moves.add(move);

            //update whose turn it is next
            turn = turn == 'w' ? 'b' : 'w';

            // update en_passant_target_square
            if (piece instanceof Pawn) {

                //check if it was an en passant move and remove the captured pawn
                if (captured_piece == null && to.equals(en_passant_target_square)) {

                    Coordinate captured_square = new Coordinate(to_c.column, from_c.row);
                    System.out.println(String.format("capturing en passant on '%s' to '%s'", captured_square, en_passant_target_square));

                    assert board.get(captured_square.toString()) instanceof Pawn : "Wrong en passant!";
                    //actually capture
                    board.put(captured_square.toString(), null);
                }


                if (Math.abs((from_c.row - to_c.row)) == 2) {
                    en_passant_target_square = new Coordinate(
                            new Coordinate(from).column,
                            piece.next_row(new Coordinate(from).row, 1)
                    ).toString();
                } else {
                    en_passant_target_square = "-";
                }
            } else {
                en_passant_target_square = "-";
            }

            //update the full move number
            if (piece != null && piece.color == Piece.COLOR.BLACK) {
                full_move_number++;
            }

            if (piece instanceof King) {

                switch (move) {
                    case "e1c1":
                        System.out.println("<<==============================white Castle long!==============================>>");
                        if (can_actually_castle(piece.color, King.CASTLE.LONG.castle_direction).size() < 1) {
                            this.move(from, to);
                            this.move("a1d1");
                        }
                        break;
                    case "e1g1":
                        System.out.println("<<==============================white Castle short!==============================>>");
                        if (can_actually_castle(piece.color, King.CASTLE.SHORT.castle_direction).size() < 1) {
                            this.move(from, to);
                            this.move("h1f1");
                        }
                        break;
                    case "e8c1":
                        System.out.println("<<==============================black Castle long!==============================>>");
                        if (can_actually_castle(piece.color, King.CASTLE.LONG.castle_direction).size() < 1) {
                            this.move(from, to);
                            this.move("a8d8");
                        }
                        break;
                    case "e8g8":
                        System.out.println("<<==============================black Castle short!==============================>>");
                        if (can_actually_castle(piece.color, King.CASTLE.SHORT.castle_direction).size() < 1) {
                            this.move(from, to);
                            this.move("h8f8");
                        }
                        break;
                    default:
                        // not castling
                        this.move(from, to);
                        break;
                }
            } else {
                this.move(from, to);
            }

            //check if it requires a pawn promotion
            if (piece instanceof Pawn && (to_c.row == 8 || to_c.row == 1)) {

                char piece_promotion = promote_pawn();

                // System.out.println(String.format("Promotes %s on %s to a %c", piece, from,piece_promotion));
                switch (piece_promotion) {
                    case 'q':
                        board.put(to, new Queen(piece.color, 'q'));
                    case 'n':
                        board.put(to, new Knight(piece.color, 'n'));
                    case 'b':
                        board.put(to, new Bishop(piece.color, 'b'));
                    case 'r':
                        board.put(to, new Rook(piece.color, 'r'));
                }
            }


            return true;
        } else {
            System.out.println("-======================-------");
        }


        return false;
    }

    public List<String> can_actually_castle(Piece.COLOR side_king_code, int castle_direction) {
        int step = Integer.signum(castle_direction);
        List<String> attacked_castle_squares = new ArrayList<>();

        char king_color;
        String from;
        if (side_king_code == Piece.COLOR.WHITE) {
            king_color = 'K';
            from = "e1";
        } else {
            king_color = 'k';
            from = "e8";
        }
        boolean can_castle = true;

        Coordinate from_c = new Coordinate(from);
        Piece piece = board.get(from);

        if (piece != null) {
            if (in_check(king_color)) {
                can_castle = false;
            } else {
                for (int i = 1; i < 3; i++) {

                    //only do this if the king can actually castle
                    if ((i == 1 && castle_state.get(side_king_code).get("short")) || (i == 2 && castle_state.get(side_king_code).get("long"))) {
                        char next_column = piece.next_column(from_c.column, step * i);
                        String temp_to = String.format("%c%d", next_column, from_c.row);

                        Piece saved_piece = board.get(temp_to);
                        move(from, temp_to);
                        attacked_castle_squares.add(temp_to);

                        if (in_check(king_color)) {
                            can_castle = false;
//                System.out.println(String.format("cannot castle because %s is attacked", temp_to));
                        }
                        move(temp_to, from);
                        board.put(temp_to, saved_piece);
                    }
                }
            }

        }

        if (can_castle) {
            attacked_castle_squares.clear();
        }

        return attacked_castle_squares;
    }

    private Coordinate get_king_coordinates(char side_king_code) {
        Coordinate found_king_location = null;
        for (String coord : board.keySet()) {
            Piece piece = board.get(coord);
            if (piece != null && piece.code == side_king_code) {
                found_king_location = new Coordinate(coord);
            }
        }

        if (found_king_location == null) {
            System.out.println(String.format("not found '%s' king!!", side_king_code));
            System.out.println(board);
        }

        assert found_king_location != null;

        return found_king_location;
    }

    /**
     * The color of the king we want to check if they are in check
     *
     * @param side_king_code 'k' or 'K'
     * @return true if that king is in check, false otherwise
     */
    protected boolean in_check(char side_king_code) {

        boolean is_in_check = false;

        //get the king.
        final Coordinate king = get_king_coordinates(side_king_code);

        for (String coord : new HashMap<>(board).keySet()) {

            Piece piece = board.get(coord);

            if (piece != null && piece.code != side_king_code) {

                Coordinate current = new Coordinate(coord);
                boolean is_long_range_piece = false;
                if (piece instanceof Queen || piece instanceof Rook || piece instanceof Bishop)
                    is_long_range_piece = true;

                List<Coordinate> possible_moves = piece.possible_moves(board, current, piece.color, castle_state, is_long_range_piece);
                if (possible_moves.stream().anyMatch(o -> o.toString().equals(king.toString()))) {
                    is_in_check = true;
                    break; // no need to keep going
                }
            }

        }
        return is_in_check;
    }

    /**
     * What are the possible moves in this position?
     *
     * @return List<Coordinate> per possible piece
     */
    Map<String, List<Coordinate>> possible_moves() {
        // assume the king is in check. Find all moves that avert this situation

        char king_color;
        Piece.COLOR color;

        if (turn == 'w') {
            king_color = 'K';
            color = Piece.COLOR.WHITE;
        } else {
            king_color = 'k';
            color = Piece.COLOR.BLACK;
        }

        Map<String, List<Coordinate>> final_moves = new HashMap<>();

        if (move_count_before_capture < max_move_count_before_capture) {

            //filter out all those that do not lead to `color` not being in check
            for (String coord : new HashMap<>(board).keySet()) {

                Piece piece = board.get(coord);
                Coordinate current_piece = new Coordinate(coord);

                if (piece != null && piece.color == color) {
                    boolean is_long_range_piece = false;
                    if (piece instanceof Queen || piece instanceof Rook || piece instanceof Bishop)
                        is_long_range_piece = true;

                    List<Coordinate> piece_moves = piece.possible_moves(board, current_piece, piece.color, castle_state, is_long_range_piece);

                    //add en passant move if available
                    if (!en_passant_target_square.equals("-") && piece instanceof Pawn) {
                        Coordinate enpassant_coord = new Coordinate(en_passant_target_square);

                        if (en_passant_target_square.equals(new Coordinate(piece.next_column(current_piece.column, 1), piece.next_row(current_piece.row, 1)).toString()) ||
                                en_passant_target_square.equals(new Coordinate(piece.next_column(current_piece.column, -1), piece.next_row(current_piece.row, 1)).toString())) {
                            piece_moves.add(enpassant_coord);

                            System.out.println(String.format(" '%s' on '%s' can capture to '%s' using en passant move", piece, coord, enpassant_coord));
                            System.out.println(piece_moves);
                        }
                    }


                    for (Coordinate my_move : piece_moves) {
                        //move the piece temporarily
                        Piece captured_piece = board.get(my_move.toString());
                        if (!(captured_piece instanceof King)) {// you cannot capture a king

                            Piece old_to = board.get(my_move.toString());

                            move(coord, my_move.toString()); // this works because this the moves are for the piece itself

                            if (!in_check(king_color)) {

                                List<Coordinate> moves = final_moves.get(coord);
                                if (moves == null) {
                                    moves = new ArrayList<>();
                                }

                                moves.add(my_move);
                                final_moves.put(coord, moves);

                                // System.out.println(board.get(coord).code + " on " + coord + " to " + my_move);
                            }

                            move(my_move.toString(), coord);

                            assert board.get(my_move.toString()) == null;
                            board.put(my_move.toString(), old_to);
                            assert board.get(my_move.toString()) == old_to;
                        }
                    }

                    //final clean up
                    List<String> attacked_castle_squares = Stream.concat(
                            can_actually_castle(color, King.CASTLE.SHORT.castle_direction).stream(),
                            can_actually_castle(color, King.CASTLE.LONG.castle_direction).stream()
                    ).collect(Collectors.toList());

                    List<Coordinate> all_moves = final_moves.get(coord);
                    if (all_moves != null) {
                        List<Coordinate> new_list = all_moves.stream()
                                .filter(o -> !attacked_castle_squares.contains(o.toString()))
                                .collect(Collectors.toList());
                        final_moves.put(coord, new_list);
                    }

                }

            }

            //filter out pieces with no moves
            Map<String, List<Coordinate>> final_moves_1 = new HashMap<>();
            for (String piece : final_moves.keySet()) {
                List<Coordinate> items = final_moves.get(piece);
                if (items.size() > 0) {
                    final_moves_1.put(piece, items);
                }
            }
            return final_moves_1;
        } else return final_moves;
    }

    public String get_result() {
        if (in_check('k') && move_count_before_capture < max_move_count_before_capture) {
            return "1-0";
        } else if (in_check('K') && move_count_before_capture < max_move_count_before_capture) {
            return "0-1";
        } else {
            return "1/2-1/2";
        }
    }

    // everyone must implement this method with their own logic
    protected abstract String generate_next_move();


    //by default promote to a queen
    protected Character promote_pawn() {
        return promotion_pieces.get(0);
    }
}


