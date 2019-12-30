package pattern.app.strategies;

import pattern.app.Chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * If there are mates, pick any one at random
 * if there are captures, pick a random capture
 * otherwise pick a random move
 */
public class MaterialRandomChess extends Chess {


    public MaterialRandomChess(String fen) {
        super(fen);
    }

    public String generate_next_move() {
        List<String> possible_moves = possible_moves();
        System.out.println("possible moves: " + possible_moves);

        if (possible_moves.size() < 1) {
            return null;
        } else {

            //get the mates
            List<String> mates = new ArrayList<>();
            List<String> captures = new ArrayList<>();
            List<String> checks = new ArrayList<>();


            Pattern mate_pattern = Pattern.compile(".*#");
            Pattern capture_pattern = Pattern.compile(".*x.*");
            Pattern check_pattern = Pattern.compile(".*\\+");

            for (String move : possible_moves) {
                if (mate_pattern.matcher(move).matches()) {
                    mates.add(move); //the first mate is sufficient
                    break;
                }
                if (capture_pattern.matcher(move).matches()) captures.add(move);
                if (check_pattern.matcher(move).matches()) checks.add(move);
            }

            Random r = new Random();

            String selected_move = null;

            //pick any check mate move
            if(mates.size() > 0) {
                selected_move = mates.get(r.nextInt(mates.size()));//mates
            }
            else if(captures.size() > 0){
                selected_move = captures.get(r.nextInt(captures.size())); //captures
            }
            else if (checks.size() > 0) {
                selected_move = checks.get(r.nextInt(checks.size())); //checks
            }
            else if(possible_moves.size() > 0){
                //no interesting moves, pick a random one

                selected_move = possible_moves.get(r.nextInt(possible_moves.size())); //all possible_moves
            }

            return selected_move;
        }
    }
}
