package com.codeoftheweb.salvo.service;

import com.codeoftheweb.salvo.models.GamePlayer;

import java.util.List;

public interface GamePlayerService {
    GamePlayer saveGamePlayer(GamePlayer gamePlayer);

    List<GamePlayer> getGamePlayer();

    GamePlayer updateGamePlayer(GamePlayer gamePlayer);

    boolean deleteGamePlayer(long id);

    GamePlayer findGamePlayerById(long id);


}
