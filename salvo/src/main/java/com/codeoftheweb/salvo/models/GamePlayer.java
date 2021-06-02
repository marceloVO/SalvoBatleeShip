package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @JsonFormat(pattern  = "yyyy-MM-dd HH:mm:ss")
    private Date joinDate;

    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player, Date joinDate) {
        this.game = game;
        this.player = player;
        this.joinDate = joinDate;
    }

    public long getId() {
        return id;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public Date getJoinDate() {
        return joinDate;
    }


    @OneToMany(mappedBy = "gameP", fetch = FetchType.EAGER)
    private Set<Ship> ships ;

    @OneToMany(mappedBy = "gamePlay", fetch = FetchType.EAGER)
    private Set<Salvo> salvo ;

    public Set<Salvo> getSalvo(){ return this.salvo; }

    public Set<Ship> getShips() {
        return this.ships;
    }

    public void addSalvo(Salvo salvo){
        salvo.setGamePlay(this);
        this.salvo.add(salvo);
    }

    public void addShip(Ship ship){
        ship.setGameP(this);
        this.ships.add(ship);
    }

    public Map<String, Object> playerInfo(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id",getId());
        dto.put("player",getPlayer().playerInfo());
        return dto;
    }

}
