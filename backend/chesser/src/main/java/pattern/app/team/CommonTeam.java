package pattern.app.team;

import pattern.app.Chess;
import pattern.app.pieces.*;
import pattern.app.utils.Color;
import pattern.app.utils.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pattern.app.Chess.board;

public abstract class CommonTeam {

    protected abstract int[] home_space();

    protected abstract Color color();


    protected void init() {
        for (int row : Chess.rows) {
            for (char col : Chess.columns) {
                Coordinate coordinate = new Coordinate(col, row);


                if (row == home_space()[0]) {
                    //Rooks
                    if (col == 'a' || col == 'h') {
                        board.put(coordinate.toString(), new Rook(coordinate, (Team) this, color()));
                    }
                    //Knights
                    else if (col == 'b' || col == 'g') {
                        board.put(coordinate.toString(), new Knight(coordinate, (Team) this, color()));
                    }
                    //bishops
                    else if (col == 'c' || col == 'f') {
                        board.put(coordinate.toString(), new Bishop(coordinate, (Team) this, color()));
                    }
                    //queens
                    else if (col == 'd') {
                        board.put(coordinate.toString(), new Queen(coordinate, (Team) this, color()));
                    }
                    //kings
                    else if (col == 'e') {
                        board.put(coordinate.toString(), new King(coordinate, (Team) this, color()));
                    }
                }
                //pawns
                else if (row == home_space()[1]) {
                    board.put(coordinate.toString(), new Pawn(coordinate, (Team) this, color()));
                }

            }
        }
    }

    /**
     * Find all the series of 1's in the fen string and replace with their count/sum
     *
     * @param fen_string The long format of the fen string
     * @return The condensed version of the fen string
     */
    String expand_fen_string(String fen_string) {

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

    public final String get_king(boolean opponent) {
        String opponent_king = null;


        for (int row : Chess.rows) {
            for (char col : Chess.columns) {
                Coordinate current = new Coordinate(col, row);
                Piece piece = board.get(current.toString());

                if (piece != null && Character.toLowerCase(piece.code()) == 'k') {
                    if (opponent && piece.team != this) {
                        opponent_king = current.toString();
                        break;
                    } else if (!opponent && piece.team == this) {
                        opponent_king = current.toString();
                        break;
                    }
                }
            }
        }
        assert opponent_king != null : "The opponent does not have a king!";

        return opponent_king;
    }

    private void move(String from, String move) {

        String dest_coord = clean_move(move);

        if (!from.equals(dest_coord)) {
            Piece fromPiece = board.get(from);

            Pattern p = Pattern.compile(".*=([QRBN])");
            Matcher m = p.matcher(move);
            if (m.matches()) {
                Piece new_piece = null;
                switch (m.group(1).charAt(0)) {
                    case 'Q':
                        new_piece = new Queen(new Coordinate(dest_coord), fromPiece.team, fromPiece.color);
                        break;
                    case 'R':
                        new_piece = new Rook(new Coordinate(dest_coord), fromPiece.team, fromPiece.color);
                        break;
                    case 'N':
                        new_piece = new Knight(new Coordinate(dest_coord), fromPiece.team, fromPiece.color);
                        break;
                    case 'B':
                        new_piece = new Bishop(new Coordinate(dest_coord), fromPiece.team, fromPiece.color);
                        break;
                }
                board.put(dest_coord, new_piece);

            } else {
                fromPiece.setCoordinate(new Coordinate(dest_coord));
                board.put(dest_coord, fromPiece);
            }

            //remove the old coord -  we have just moved something from there anyway
            board.remove(from);
        }
    }

    /**
     * Basically remove the capture sign
     *
     * @param move
     * @return
     */
    private String clean_move(String move) {
        Pattern p = Pattern.compile(".*([a-h][1-8]).*");
        Matcher m = p.matcher(move);
        if (m.matches()) {
            return m.group(1);
        }
        return null;
    }


    public boolean in_check(String opponents_king_coordinate) {

        List<String> board_keys = new ArrayList<>(board.keySet());
        for (String coord : board_keys) {

            Piece piece = board.get(new Coordinate(coord).toString());
            if (piece != null/* && piece.team == this*/) {
                /*
                 * TODO: this can be optimised further.
                 * No need to check my pieces. They will never cause my king to be in check
                 */
//                System.out.println(coord + " => " + piece);

                List<String> temp_possible_moves = new ArrayList<>(piece.possible_moves());

                for (String move : temp_possible_moves) {
                    String dest_coord = clean_move(move);

                    if (dest_coord.equals(opponents_king_coordinate)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public List<String> possible_moves() {
        String opponents_king_coordinate = get_king(true);


//        System.out.println(" target >> " + opponents_king_coordinate);

        List<String> team_possible_moves = new ArrayList<>();

        List<String> board_keys = new ArrayList<>(board.keySet());

        for (String coord : board_keys) {

            Piece piece = board.get(new Coordinate(coord).toString());

            if (piece != null && piece.team == this) {
                List<String> piece_possible_moves = new ArrayList<>(piece.possible_moves());

                if (piece_possible_moves.size() > 0) {
//                        System.out.println(coord + " contains " + piece_possible_moves);

//                        List<String> updated_piece_possible_moves = new ArrayList<>();

                    for (String move : piece_possible_moves) {

                        //make backups
                        String dest_coord = clean_move(move);

                        Piece dest_piece = board.get(dest_coord);
                        Piece original_piece = board.get(coord);

                        //make the move temporarily

                        move(coord, move);

                        //first of all my king shouldn't be in check after making this move.else, it doesn't matter
                        if (!in_check(get_king(false))) {
//
                            //if in making the move the opponent's king will be in check
                            if (in_check(get_king(true))) {
                                boolean can_run = false;

//                                System.out.println("played "+piece+""+coord + ""+ move);
//                                System.out.println(board.toString());

                                //If the opponent has moves that could avert the check, then it's not a mate

                                for (String op_coord : new ArrayList<>(board.keySet())) {
                                    Piece op_piece = board.get(op_coord);
                                    //all pieces that belong to the opponent's team
                                    if (op_piece != null && op_piece.team != this) {
                                        List<String> op_moves = new ArrayList<>(op_piece.possible_moves());

//                                        System.out.println(op_piece+ ": "+ op_moves);

//                                        can_run = false;

                                        //make them temporarily
                                        for (String op_move : op_moves) {

                                            //make backups
                                            String op_dest_coord = clean_move(op_move);

                                            Piece op_dest_piece = board.get(op_dest_coord);
                                            Piece op_original_piece = board.get(op_coord);

                                            //make the move temporarily
                                            move(op_coord, op_move);

                                            //check if king is still in check.
                                            if (!in_check(get_king(true))) {
                                                //ilikuwa vitisho tu
                                                can_run = true;

//                                                System.out.println("can run from "+op_coord + "" + op_move);
                                            }

                                            //return what was moved
                                            move(op_dest_coord, op_coord);

                                            //ensure it has what it had before
                                            board.put(op_coord, op_original_piece);

                                            if (op_dest_piece == null) {
                                                board.remove(op_dest_coord);
                                            } else {
                                                board.put(op_dest_coord, op_dest_piece);
                                            }
                                            if (can_run) {
                                                break;
                                            }
                                        }

                                    }
                                }

                                if (!can_run) { //no where to run!
                                    //add a hash
                                    team_possible_moves.add(correct_notation(piece, coord, String.format("%s#", move)));
                                } else {
                                    //add a plus
                                    team_possible_moves.add(correct_notation(piece, coord, String.format("%s+", move)));
                                }
                            } else {
//                                    System.out.println("not added");
//                                updated_piece_possible_moves.add(move);
                                team_possible_moves.add(correct_notation(piece, coord, move));
                            }
                        }
//
//                          //return what was moved
                        move(dest_coord, coord);

                        //ensure it has what it had before
                        board.put(coord, original_piece);
//                            System.out.println(dest_coord + " is null =>"+ dest_piece);

                        if (dest_piece == null) {
                            board.remove(dest_coord);
                        } else {
                            board.put(dest_coord, dest_piece);
                        }

                    }
//                        team_possible_moves.put(piece, updated_piece_possible_moves);
                }

            }
        }

        //add castle options if available
        team_possible_moves.addAll(castle_options());


        return team_possible_moves;
    }

    private String correct_notation(Piece piece, String coordinate, String move) {
        String sep = "-";
        if (move.charAt(0) == 'x') sep = "";
        return String.format("%s%s%s%s", piece, coordinate, sep, move);
    }

    protected List<String> castle_options() {

        List<String> castle_options = new ArrayList<>();

        String from = color() == Color.WHITE ? "e1" : "e8";


        Coordinate from_c = new Coordinate(from);
        Piece piece = board.get(from);

        if (piece != null) {
            if (!in_check(get_king(false))) {
                for (int castle_direction = -1; castle_direction < 3; castle_direction += 2) {// (-1 and 1 only)
                    boolean can_castle = true;

                    for (int i = 1; i < 3; i++) {

                        //only do this if the king can actually castle
                        if ((i == 1 && Chess.castle_state.get(color()).get("short")) || (i == 2 && Chess.castle_state.get(color()).get("long"))) {

//                            System.out.println("yap");

                            char next_column = piece.team.next_column(from_c.column, castle_direction * i);
                            String temp_to = String.format("%c%d", next_column, from_c.row);

                            Piece saved_piece = board.get(temp_to);
                            //if that piece is not null, cannot castle that way
                            if (saved_piece != null) {
                                can_castle = false;
                                continue;
                            }

                            move(from, temp_to);

                            if (in_check(get_king(false))) {
                                can_castle = false;
//                                System.out.println(String.format("cannot castle because %s is attacked", temp_to));
                            }
                            move(temp_to, from);
                            board.put(temp_to, saved_piece);
                        } else {
                            can_castle = false;
                        }
                    }

                    if (can_castle) {
                        System.out.println(color() + " yip " + castle_direction);

                        if (castle_direction < 0) { //long castle
                            Piece night_space = board.get(new Coordinate(piece.team.next_column(from_c.column, castle_direction * 3), from_c.row).toString());
                            if (night_space == null) {//this square must be empty as well for long castle
                                castle_options.add("O-O-O"); //NOTE:  O instead of 0
                            }
                        } else if (castle_direction > 0) { //short castle
                            castle_options.add("O-O");
                        }
                    }
                }
            }

        }

        return castle_options;
    }

    public int value() {
        int value = 0;
        List<String> board_keys = new ArrayList<>(board.keySet());
        for (String coord : board_keys) {

            Piece piece = board.get(new Coordinate(coord).toString());
            if (piece != null && piece.team == this) {
                value += piece.value();
            }

        }
        return value;
    }

}
