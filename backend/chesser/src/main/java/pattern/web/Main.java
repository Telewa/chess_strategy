package pattern.web;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pattern.app.strategies.Level;
import pattern.app.strategies.MaterialRandomChess;
import pattern.app.strategies.RandomChess;

import java.util.HashMap;
import java.util.Map;


@RestController
public class Main {

    //play against a random player - level 0

    @GetMapping(path = "/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> random(@RequestParam(value = "fen", defaultValue = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1") String fen) {
        RandomChess game = new RandomChess(fen);

        String move = game.generate_next_move();

        System.out.println("received: =>" + fen + ": suggested: =>" + move);

        Map<String, String> response = new HashMap<>();
        response.put("suggested_move", move);

        return response;
    }

    //play against a very greedy player - level 0.1

    @GetMapping(path = "/material_random", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> material_random(@RequestParam(value = "fen", defaultValue = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1") String fen) {
        MaterialRandomChess game = new MaterialRandomChess(fen);

        String move = game.generate_next_move();

        System.out.println("received: =>" + fen + ": suggested: =>" + move);

        Map<String, String> response = new HashMap<>();
        response.put("suggested_move", move);

        return response;
    }

    @GetMapping(path = "/level_random", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> level_random(@RequestParam(value = "fen", defaultValue = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1") String fen) {
        Level game = new Level(fen);

        String move = game.generate_next_move();

        System.out.println("received: =>" + fen + ": suggested: =>" + move);

        Map<String, String> response = new HashMap<>();
        response.put("suggested_move", move);

        return response;
    }

    //TODO: Add more strategies
}






