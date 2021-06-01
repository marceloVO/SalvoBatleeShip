package com.codeoftheweb.salvo.models;

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
    @Column(name = "location")
    private List<String> Locations = new ArrayList<>();



    private String type;

    public Ship() {
    }

    public Ship(GamePlayer gameP, List<String> locations, String type) {
        this.gameP = gameP;
        Locations = locations;
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

    public List<String> getLocations() {
        return Locations;
    }

    public void setLocations(List<String> locations) {
        Locations = locations;
    }



    public Map<String, Object> getInfo(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type",getType());
        dto.put("locations",getLocations());
        return dto;
    }
}
