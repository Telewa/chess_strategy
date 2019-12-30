package pattern.app.strategies;

import pattern.app.Chess;

import java.util.List;
import java.util.Random;

/**
 * This one will just pick any move out of all the possible moves
 */
public class RandomChess extends Chess {


    public RandomChess(String fen) {
        super(fen);
    }

    public String generate_next_move() {
        List<String> possible_moves = possible_moves();
        System.out.println("possible moves: ");
        System.out.println(possible_moves);

        if (possible_moves.size() < 1) {
            return null;
        } else {
            //pick any random move
            Random r = new Random();
            return possible_moves.get(r.nextInt(possible_moves.size()));
        }
    }
}
