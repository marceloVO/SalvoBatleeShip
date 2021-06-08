package com.codeoftheweb.salvo.service.implementacion;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GamePlayerService implements GamePlayerServie  {

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Override
    public GamePlayer saveGamePlayer(GamePlayer gamePlayer){
        return gamePlayerRepository.save(gamePlayer);
    }

    @Override

}
