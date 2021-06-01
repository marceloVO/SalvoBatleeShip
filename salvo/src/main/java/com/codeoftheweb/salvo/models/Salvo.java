package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayerID")
    private GamePlayer gamePlay;

    @ElementCollection
    @Column(name = "location")
    private List<String> Locations = new ArrayList<>();

    private String turn;


    public Salvo() {
    }

    public long getId() {
        return id;
    }

    public Salvo(GamePlayer gamePlay, List<String> locations, String turn) {
        this.gamePlay = gamePlay;
        Locations = locations;
        this.turn = turn;
    }

    public GamePlayer getGamePlay() {
        return gamePlay;
    }

    public void setGamePlay(GamePlayer gamePlay) {
        this.gamePlay = gamePlay;
    }

    public List<String> getLocations() {
        return Locations;
    }

    public void setLocations(List<String> locations) {
        Locations = locations;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public Map<String, Object> getInfo(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn",getTurn());
        dto.put("player",getGamePlay().getPlayer().getId());
        dto.put("locations",getLocations());

        return dto;
    }
}
