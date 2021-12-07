package com.codeoftheweb.salvo.service.implementacion;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.service.GamePlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamePlayerServiceImp implements GamePlayerService {

    @Autowired
    GamePlayerRepository gamePlayerRepository;


    public GamePlayer saveGamePlayer(GamePlayer gamePlayer){
        return gamePlayerRepository.save(gamePlayer);
    }

    @Override
    public List<GamePlayer> getGamePlayer() {
        return null;
    }

    @Override
    public GamePlayer updateGamePlayer(GamePlayer gamePlayer) {
        return null;
    }

    @Override
    public boolean deleteGamePlayer(long id) {
        return false;
    }

    @Override
    public GamePlayer findGamePlayerById(long id) {
        return gamePlayerRepository.findById(id).get();
    }


}
