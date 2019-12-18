package com.emmanuel.chesser;


public class OldMain {

    public static void main(String[] args) {

        int mate_count = 0;
        int games_count = 1;

        while (mate_count < 10) {

            RandomChess game = new RandomChess();
            String move = "";
            int count = 0;

            move = game.generate_next_move();
            while (move != null) {

                //update the move stack
                game.make_move(move);

                count++;
                // Thread.sleep(10000);
                if (count > 1000) {
                    break;
                }
                move = game.generate_next_move();
            }
            if (move == null) {
                mate_count++;

                System.out.println(String.format("moves_count = %d, games_count = %d, result = '%s', color_to_play = '%s', winning_move = '%s'", count, games_count, game.get_result(), game.turn, game.moves.get(game.moves.size() - 1)));
                System.out.println(game.moves);
                System.out.println(game.fen());
                System.out.println(game);
            }
            games_count++;
        }
    }

}
