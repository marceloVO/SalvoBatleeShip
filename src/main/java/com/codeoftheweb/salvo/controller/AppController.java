package com.codeoftheweb.salvo.controller;

import com.codeoftheweb.salvo.models.*;

import com.codeoftheweb.salvo.repository.*;
import com.codeoftheweb.salvo.service.GamePlayerService;
import com.codeoftheweb.salvo.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

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
    public ResponseEntity<Map<String, Object>> getGameViews(@PathVariable long nn, Authentication authentication) {

        Player user = playerRepository.findByUserName(authentication.getName());

        GamePlayer gameP = gamePlayerRepository.findById(nn).get();

        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error","no autenticado"), HttpStatus.UNAUTHORIZED);
        }
        if(gameP.getPlayer().getId() != user.getId()){
            return new ResponseEntity<>(makeMap("error"," no esta autorizado"),HttpStatus.FORBIDDEN);

        }


        return new ResponseEntity<>(gameP.getGame().getInfo(gameP),HttpStatus.ACCEPTED);
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

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> registerGames(Authentication authentication){
        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error","Missing daa"), HttpStatus.UNAUTHORIZED);
        }

        Player user = playerRepository.findByUserName(authentication.getName());

        Game gameNew = new Game(new Date());
        gameRepository.save(gameNew);
        GamePlayer gpNew = new GamePlayer(gameNew,user,new Date());

        gamePlayerRepository.save(gpNew);
        return new ResponseEntity<>(makeMap("gpid",gpNew.getId()),HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping(path = "/game/{nn}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable long nn,Authentication authentication ){
        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error","Missing daa"), HttpStatus.UNAUTHORIZED);
        }

        Player user = playerRepository.findByUserName(authentication.getName());

        Game game = gameRepository.findById(nn).get();

        if(user == null){
            return new ResponseEntity<>(makeMap("error","error"),HttpStatus.FORBIDDEN);
        }
        if(game == null){
            return new ResponseEntity<>(makeMap("error","error"),HttpStatus.FORBIDDEN);
        }

        if(game.getGamePlayers().size() == 1){
            GamePlayer gpNew = gamePlayerRepository.save(new GamePlayer(game,user,new Date()));
            return new ResponseEntity<>(makeMap("gpid",gpNew.getId()),HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(makeMap("error","El juego esta lleno"),HttpStatus.FORBIDDEN);
        }


    }

    @Autowired
    ShipRepository shipR;

    @PostMapping(path = "/games/players/{nn}/ships")
    public ResponseEntity<Map<String, Object>> addShip(@PathVariable long nn, @RequestBody Set<Ship> ships, Authentication authentication) {

        if(isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "No Autorizado"), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName());
        GamePlayer gp = gamePlayerRepository.findById(nn).get();

        if (player  == null) {
            return new ResponseEntity<>(makeMap("error", "no hay players"), HttpStatus.UNAUTHORIZED);
        }

        if (gp == null) {
            return new ResponseEntity<>(makeMap("error", "No hay datos en game player"), HttpStatus.UNAUTHORIZED);
        }
        if(gp.getGame().getGamePlayers().size() == 1){
            return new ResponseEntity<>(makeMap("error", "Falta un jugador para poder crear los barcos"), HttpStatus.FORBIDDEN);
        }

        if(gp.getShips().size() == 5){
            return new ResponseEntity<>(makeMap("error", "ya hay barcos creados para este juego"), HttpStatus.UNAUTHORIZED);
        }

        ships.stream().forEach((p)->{
            p.setGameP(gp);
            shipR.save(p);
        });

        return new ResponseEntity<>(makeMap("OK","OK"),HttpStatus.CREATED);

    }

    @Autowired
    private SalvoRepository salvoR;


    @PostMapping(path = "/games/players/{nn}/salvoes")
    public ResponseEntity<Map<String, Object>> addSalvos(@PathVariable long nn, @RequestBody Salvo salvo, Authentication authentication) {

        if(isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "No Autorizado"), HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByUserName(authentication.getName());
        GamePlayer gp = gamePlayerRepository.findById(nn).get();



        if(gp.getPlayer().getId() != player.getId()){
            return new ResponseEntity<>(makeMap("Cuidado", "no es tu juego"), HttpStatus.UNAUTHORIZED);
        }


        if (player  == null) {
            return new ResponseEntity<>(makeMap("error", "no hay players"), HttpStatus.UNAUTHORIZED);
        }

        if (gp == null) {
            return new ResponseEntity<>(makeMap("error", "No hay datos en game player"), HttpStatus.UNAUTHORIZED);
        }

        if(salvo.getSalvoLocations().size() <=1 && salvo.getSalvoLocations().size() >= 5){
            return new ResponseEntity<>(makeMap("error", "... "), HttpStatus.UNAUTHORIZED);
        }

        int oponnent = gp.getGame().getGamePlayers().stream().filter(gameP -> gameP.getPlayer().getId() != player.getId()).findFirst().get().getSalvo().size();

        if(gp.getSalvo().size() > oponnent){
            return new ResponseEntity<>(makeMap("error", "su turno es mayor que el del oponente "), HttpStatus.FORBIDDEN);
        }
        salvo.setTurn(gp.getSalvo().size() + 1);
        salvo.setGamePlay(gp);
        salvoR.save(salvo);


        return new ResponseEntity<>(makeMap("OK","OK"),HttpStatus.CREATED);

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
