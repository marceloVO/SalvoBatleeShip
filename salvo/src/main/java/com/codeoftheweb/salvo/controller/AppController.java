package com.codeoftheweb.salvo.controller;
import com.codeoftheweb.salvo.models.Game;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private GameRepository repo;

    @Autowired
    private GamePlayerRepository gpr;

    @RequestMapping("/games")
    public Map<String, Object> getGames()
    {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("games",repo.findAll().stream().map(Game::gameInfo).collect(toList()));
        return dto;
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> getGameViews(@PathVariable long nn) {

        Optional<GamePlayer> gp = gpr.findById(nn);
        if(gp.isPresent()){
            return gp.get().getGame().getInfo(gp.get());
        }else{
            return null;
        }
    }

}
