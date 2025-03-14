$(function () {
    var game = new Chess();

    // only pick up pieces for the side to move
    function onDragStart(source, piece, position, orientation) {
        // do not pick up pieces if the game is over
        if (game.game_over() === true ||
            (game.turn() === 'w' && piece.search(/^b/) !== -1) ||
            (game.turn() === 'b' && piece.search(/^w/) !== -1)) {
            return false;
        }
    }

    function makeRandomMove() {
        var possibleMoves = game.moves();

        // game over
        if (possibleMoves.length === 0) return;
        var fen = game.fen();

        $.ajax({
            url: "/level_random",
            type: "GET",
            data: {
                fen: fen
            },
            dataType: "json",
            // Fetch the stored token from localStorage and set in the header
            async: true,
            beforeSend: function (xhr, settings) {

            },
            error: function (error) {

            },
            success: function (data) {

                var result = game.move(data["suggested_move"], {sloppy: true});
                if (result) {
                    console.log(fen, data["suggested_move"]);
                    board.position(game.fen());
                }
                else{
                    console.log(data["suggested_move"], "caused an error");
                    console.log(fen, result);
                    throw new DOMException();
                }

                //uncomment this for autoplay
                setTimeout(function () {
                    makeRandomMove();
                }, 1000);
            },
            complete: function () {

            }
        });

    }

    function onDrop(source, target) {
        // see if the move is legal
        var move = game.move({
            from: source,
            to: target,
            promotion: 'q' // NOTE: always promote to a queen for example simplicity
        });

        // illegal move
        if (move === null) return 'snapback';

        // make random legal move for black
        window.setTimeout(makeRandomMove, 250)
    }

// update the board position after the piece snap
// for castling, en passant, pawn promotion
    function onSnapEnd() {
        board.position(game.fen())
    }

    var config = {
        draggable: true,
        position: 'start',
        onDragStart: onDragStart,
        onDrop: onDrop,
        onSnapEnd: onSnapEnd,
        pieceTheme: 'static/img/chesspieces/wikipedia/{piece}.png'
    };

    var board = Chessboard('myBoard', config);
    // makeRandomMove();
});