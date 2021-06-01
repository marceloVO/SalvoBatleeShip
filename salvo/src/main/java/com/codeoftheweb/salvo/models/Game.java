package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @JsonFormat(pattern  = "yyyy-MM-dd HH:mm:ss")
    private Date createDate ;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>() ;



    public Game() {

    }

    public List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Game(Date createDate) {
        this.createDate = createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public List<Player> getPlayers() {
        return gamePlayers.stream().map(GamePlayer::getPlayer).collect(toList());
    }




    public long getId() {
        return id;
    }

    public Map<String, Object> gameInfo(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",getId());
        dto.put("createDate",getCreateDate());
        dto.put("gamePlayers",getGamePlayers().stream().map(GamePlayer::playerInfo).collect(toList()));
        return dto;
    }
    public Map<String, Object> getInfo(GamePlayer gp){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",getId());
        dto.put("createDate",getCreateDate());
        dto.put("gamePlayers",getGamePlayers().stream().map(GamePlayer::playerInfo).collect(toList()));
        dto.put("ships",gp.getShips().stream().map(Ship::getInfo).collect(toList()));
        return dto;
    }

    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }
}
