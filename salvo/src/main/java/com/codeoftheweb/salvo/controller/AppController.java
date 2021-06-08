package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.models.GamePlayer;

import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.service.GamePlayerService;
import com.codeoftheweb.salvo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private GameService repo;

    @Autowired
    private GamePlayerService gpr;

    @Autowired
    private PlayerRepository playR;

    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication)
    {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player", !isGuest(authentication)?playerInfo(playR.findByUserName(authentication.getName())):"Guest");
        dto.put("games",repo.findGameAll());
        return dto;
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> getGameViews(@PathVariable long nn) {

        Optional<GamePlayer> gp = Optional.ofNullable(gpr.findGamePlayerById(nn));
        if(gp.isPresent()){
            return gp.get().getGame().getInfo(gp.get());
        }else{
            return null;
        }
    }


    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String username, @RequestParam String password){
        if (username.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>("Missing daa", HttpStatus.FORBIDDEN);

        }

        if(playerRepository.findByUserName(username) != null){
            return new ResponseEntity<>("Name already in use",HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(username, passwordEncoder.encode(password)));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public Map<String, Object> playerInfo(Player p){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",p.getId());
        dto.put("email",p.getUserName());
        return dto;

    }

}
