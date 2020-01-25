package pattern.web;

import pattern.app.strategies.Level;
import pattern.app.strategies.MaterialRandomChess;

import java.util.HashMap;
import java.util.Map;


public class TestMain {

    public static void main(String[] args) {
//        MaterialRandomChess game = new MaterialRandomChess("r3kbnr/3qpp2/6pp/p2p4/7P/2PP3P/PP2PP2/RN2KB1R w KQkq - 0 11");
        Level game = new Level("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        System.out.println(game.fen());
        game.makeMove("Pe2-e4");
        System.out.println(game.fen());

        game.makeMove("Pc7-c5");
        System.out.println(game.fen());

        game.makeMove("Ng1-f3");
        System.out.println(game.fen());

//        System.out.println(game.makeMove("Ng8-f6"));
//        System.out.println(game.fen());

//        game.best_moves.add(1);
//        game.best_moves.add(2);
//        game.best_moves.add(3);
//
//        System.out.println(game.best_moves.peek());


//        System.out.println(game.generate_next_move());
//        System.out.println(game.makeMove("O-O-O"));
//        System.out.println(game.fen());
//        System.out.println(game.black.value());
//        System.out.println(game.white.value());
    }
}







