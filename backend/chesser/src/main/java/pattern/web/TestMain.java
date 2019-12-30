package pattern.web;

import pattern.app.strategies.MaterialRandomChess;


public class TestMain {

    public static void main(String[] args) {
        MaterialRandomChess game = new MaterialRandomChess("r3kbnr/3qpp2/6pp/p2p4/7P/2PP3P/PP2PP2/RN2KB1R w KQkq - 0 11");

        System.out.println(game);
        System.out.println(game.generate_next_move());

    }
}







