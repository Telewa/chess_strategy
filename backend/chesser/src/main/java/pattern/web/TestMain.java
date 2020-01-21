package pattern.web;

import pattern.app.strategies.Level;
import pattern.app.strategies.MaterialRandomChess;

import java.util.HashMap;
import java.util.Map;


public class TestMain {

    public static void main(String[] args) {
//        MaterialRandomChess game = new MaterialRandomChess("r3kbnr/3qpp2/6pp/p2p4/7P/2PP3P/PP2PP2/RN2KB1R w KQkq - 0 11");
        Level game = new Level("r2nkbbr/pq1ppQp1/1p5p/1np1KPp1/P1P1PN2/1P1P1BP1/7P/R1N2B1R b kq - 0 1");
//        System.out.println(game.makeMove("Pe2-e4"));

//        game.best_moves.add(1);
//        game.best_moves.add(2);
//        game.best_moves.add(3);
//
//        System.out.println(game.best_moves.peek());

        System.out.println(game);
        System.out.println(game.generate_next_move());
//        System.out.println(game.black.value());
//        System.out.println(game.white.value());
    }
}







