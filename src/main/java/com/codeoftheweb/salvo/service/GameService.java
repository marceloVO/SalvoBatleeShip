package com.codeoftheweb.salvo.service;

import com.codeoftheweb.salvo.models.Game;


import java.util.List;
import java.util.Map;

public interface GameService {


    Game saveGame(Game game);

    List<Game> getGame();

    Game updateGame(Game game);

    boolean deleteGame(long id);

    Map<String, Object> findGameAll();
}
