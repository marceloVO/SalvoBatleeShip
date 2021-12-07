package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayerID")
    private GamePlayer gameP;


    @ElementCollection
    @Column(name = "shipLocations")
    private List<String> shipLocations = new ArrayList<>();



    private String type;

    public Ship() {
    }

    public Ship(GamePlayer gameP, List<String> shipLocations, String type) {
        this.gameP = gameP;
        shipLocations = shipLocations;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public GamePlayer getGameP() {
        return gameP;
    }

    public void setGameP(GamePlayer gameP) {
        this.gameP = gameP;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getShipLocations() {
        return shipLocations;
    }

    public void setShipLocations(List<String> locations) {
        shipLocations = locations;
    }



    public Map<String, Object> getInfo(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type",getType());
        dto.put("locations",getShipLocations());
        return dto;
    }
}
