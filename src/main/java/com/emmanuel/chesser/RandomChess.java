package com.emmanuel.chesser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

class RandomChess extends BaseChess {


    RandomChess(String fen) {
        super(fen);
    }

    RandomChess() {
        super();
    }

    @Override
    protected String generate_next_move() {
        Map<String, List<Coordinate>> possible_moves = possible_moves();
//        System.out.println(possible_moves);
        if (possible_moves.size() < 1) {
            return null;
        } else {
            //pick any random move
            List<String> keysAsArray = new ArrayList<>(possible_moves.keySet());
            Random r = new Random();
            String piece_coordinate = keysAsArray.get(r.nextInt(keysAsArray.size()));

            List<Coordinate> coordinates = possible_moves.get(piece_coordinate);
            String selected_coordinate = coordinates.get(r.nextInt(coordinates.size())).toString();

            return String.format("%s%s", piece_coordinate, selected_coordinate);
        }
    }

    @Override
    protected Character promote_pawn() {
        //pick a random piece to promote to
        Random r = new Random();
        return promotion_pieces.get(r.nextInt(promotion_pieces.size()));
    }
}
