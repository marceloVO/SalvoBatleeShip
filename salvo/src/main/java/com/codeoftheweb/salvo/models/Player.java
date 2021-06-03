package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String userName;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();


    @OneToMany(mappedBy = "playerScore",fetch = FetchType.EAGER)
    private Set<Score> score ;



    public Player() {

    }

    public long getId() {
        return id;
    }

    public Player(String userName){
        this.userName = userName;
    }



    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String toString() {
        return userName;
    }
    @JsonIgnore
    public List<Game> getGames() {
        return gamePlayers.stream().map(GamePlayer::getGame).collect(toList());
    }

    public Set<Score> getScore() {
        return score;
    }

    public Score getScore(Game g){
        Optional<Score> s =  this.getScore().stream().filter(score -> score.getGamesScore().equals(g)).findFirst();
        if(s.isPresent()){
            return s.get();
        }else{
            return null;
        }
    }

    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setPlayer(this);
        gamePlayers.add(gamePlayer);
    }



    public Map<String, Object> playerInfo(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",getId());
        dto.put("userName",getUserName());
        return dto;

    }





}
