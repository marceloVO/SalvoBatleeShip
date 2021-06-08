package com.codeoftheweb.salvo.service.implementacion;

import com.codeoftheweb.salvo.models.Game;

import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
@Service
public class gameServiceImp implements GameService {

    @Autowired

    GameRepository gameRepository;


    @Override
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public List<Game> getGame() {
        return null;
    }

    @Override
    public Game updateGame(Game game) {
        return null;
    }

    @Override
    public boolean deleteGame(long id) {
        return false;
    }

    @Override
    public Map<String, Object> findGameAll() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("games",gameRepository.findAll().stream().map(Game::gameInfo).collect(toList()));
        return  dto;
    }

}
