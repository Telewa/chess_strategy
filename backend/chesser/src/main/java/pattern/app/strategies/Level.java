package pattern.app.strategies;

import pattern.app.Chess;
import pattern.app.pieces.Piece;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * - Get all the possible moves
 * - For each move
 * - imagine you play that move,
 * - find a move for the opponent
 * - if
 */
public class Level extends Chess {

    private String fen;
    public Level(String fen) {
        super(fen);
        this.fen = fen;
    }

    private Map<Integer, List<String>> move_values = new HashMap<>();

    // Creating empty priority queue
    private PriorityQueue<Integer> best_moves = new PriorityQueue<>(Collections.reverseOrder());


    public String generate_next_move() {
        List<String> possible_moves = new ArrayList<>( possible_moves() );
        System.out.println("possible moves: ");
        System.out.println(possible_moves);
        String selected_move = "";

        if (possible_moves.size() < 1) {
            return null;
        } else {
            char original_turn = turn;
            int move_value = 0;
            for(String move: possible_moves){
                Pattern move_pattern = Pattern.compile("([KkQqBbNnRrPp])([a-h][1-8])[x\\-]([a-h][1-8])(=[QNBRqnrb])?#");
                Matcher m = move_pattern.matcher(move);

                //we have mate!
                if(m.matches()){ selected_move= move; break;}

                makeMove(move);

                if(original_turn=='w'){
                    move_value = white.value() - black.value();
                }
                else{
                    move_value = black.value() - white.value();
                }

                best_moves.add(move_value);

                if( move_values.containsKey(move_value) ){
                    List <String> moves = move_values.get(move_value);
                    moves.add(move);
                    move_values.put(move_value, moves);
                }
                else{
                    List <String> moves = new ArrayList<>();
                    moves.add(move);
                    move_values.put(move_value, moves);
                }

                this.reset_with_fen(fen);
            }

            if(selected_move.equals("") && !move_values.isEmpty()) {
                System.out.println(move_values);
                List<String> potentials = move_values.get(best_moves.peek());

                //pick any random move
                Random r = new Random();
                selected_move= potentials.get(r.nextInt(potentials.size()));
            }

            return selected_move;
        }
    }
}
