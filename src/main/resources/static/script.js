function renderSuccess(game){
    $('button').remove();
    const newBoardDiv = $(renderBoard(game));
    $(document).ready(function (){
        savePlayerId(game)
    })
    $(".parent").append(newBoardDiv);
    if(localStorage.getItem("playerForNextMove") !== localStorage.getItem("playerId")){
        doPoll();
    }
    $(document).ready(function(){
        $(".pot").click(function(){
            $.ajax({
                url: "http://localhost:8080/mancala/move",
                type: "POST",
                data: "pitId=" + $(this).attr("id"),
                success: renderBoardAfterMove
            });
        });
    });
}

function savePlayerId(game){
    if(game.playerB==="EMPTY_PLAYER"){
        localStorage.setItem("playerId", game.playerA);
    } else {
        localStorage.setItem("playerId", game.playerB);
    }
}

function doPoll(){
    $.ajax({
        url: "http://localhost:8080/mancala/game",
        type: "GET",
        success: renderBoardAfterMove
    });
}

function renderBoardAfterMove(game){
    $('.board').remove();
    const boardAfterMove = $(renderBoard(game));
    $(".parent").append(boardAfterMove);
    $(document).ready(function(){
        $(".pot").click(function(){
            $.ajax({
                url: "http://localhost:8080/mancala/move",
                type: "POST",
                data: "pitId=" + $(this).attr("id"),
                success: renderBoardAfterMove
            });
        });
    });
    if(localStorage.getItem("playerForNextMove") !== localStorage.getItem("playerId")){
        doPoll();
    }
}

function renderBoard(game) {
    localStorage.setItem("playerForNextMove", game.playerForNextMove);
    return "<div class=\"board\">\n" +
        "  <div class=\"section endsection\">\n" +
        "      <div class=\"pot\" id=\"14\">"+game.pits[13].stones+"</div> \n" +
        "  </div>\n" +
        "  <div class=\"section midsection\">\n" +
        "    <div class=\"midrow topmid\">\n" +
        "      <div class=\"pot\" id=\"13\">"+game.pits[12].stones+"</div>\n" +
        "      <div class=\"pot\" id=\"12\">"+game.pits[11].stones+"</div>\n" +
        "      <div class=\"pot\" id=\"11\">"+game.pits[10].stones+"</div>\n" +
        "      <div class=\"pot\" id=\"10\">"+game.pits[9].stones+"</div>\n" +
        "      <div class=\"pot\" id=\"9\">"+game.pits[8].stones+"</div>\n" +
        "      <div class=\"pot\" id=\"8\">"+game.pits[7].stones+"</div>\n" +
        "    </div>\n" +
        "    <div class=\"midrow botmid\">\n" +
        "      <div class=\"pot\" id=\"1\">"+game.pits[0].stones+"</div>\n" +
        "      <div class=\"pot\" id=\"2\">"+game.pits[1].stones+"</div>\n" +
        "      <div class=\"pot\" id=\"3\">"+game.pits[2].stones+"</div>\n" +
        "      <div class=\"pot\" id=\"4\">"+game.pits[3].stones+"</div>\n" +
        "      <div class=\"pot\" id=\"5\">"+game.pits[4].stones+"</div>\n" +
        "      <div class=\"pot\" id=\"6\">"+game.pits[5].stones+"</div>\n" +
        "    </div>\n" +
        "  </div>\n" +
        "  <div class=\"section endsection\">\n" +
        "      <div class=\"pot\" id=\"7\">"+game.pits[6].stones+"</div>        \n" +
        "  </div>\n" +
        "</div>";
}


