package com.emmanuel.chesser;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Main {

    @GetMapping(path = "/suggest", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> main(@RequestParam(value = "fen", defaultValue = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1") String fen) {
        RandomChess game = new RandomChess(fen);
        assert fen.equals(game.fen()) : "Game misinterpreted";

        String move = game.generate_next_move();
//        System.out.println(move);

        Map<String, String> response = new HashMap<>();
        response.put("suggested_move", move);

        return response;
    }
}


