package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
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

    @OneToMany(mappedBy = "gamesScore",fetch = FetchType.EAGER)
    private List<Score> score = new ArrayList<Score>();

    public Game() {

    }

    public List<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public List<Score> getScore(){ return score; }

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
        dto.put("created",getCreateDate());
        dto.put("gamePlayers",getGamePlayers().stream().map(GamePlayer::scoreInfo).collect(toList()));
        dto.put("scores",getScore().stream().map(Score::scoreInfo).collect(toList()));
        return dto;
    }
    public Map<String, Object> getInfo(GamePlayer gp){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        Map<String, Object> hits = new LinkedHashMap<String, Object>();


        hits.put("self", this.getHits(gp, gp.getOpponent()));
        hits.put("opponent", this.getHits(gp.getOpponent(),gp));

        dto.put("id",getId());
        dto.put("created",getCreateDate());
        dto.put("gameState",getState(gp));
        dto.put("gamePlayers",getGamePlayers().stream().map(GamePlayer::playerInfo).collect(toList()));
        dto.put("ships",gp.getShips().stream().map(Ship::getInfo).collect(toList()));
        dto.put("salvoes",getGamePlayers().stream().map(GamePlayer::getSalvoesInfo).flatMap(Collection::stream).collect(toList()));
        dto.put("hits", hits);
        return dto;
    }

    private String getState(GamePlayer gamePlayer){


        if(gamePlayer.getGame().getPlayers().size() == 1){
            return "WAITINGFOROPP";
        }

        if(gamePlayer.getShips().isEmpty()){
            return "PLACESHIPS";
        }

        long pTurno = gamePlayer.getSalvo().size();
        long oTurno = gamePlayer.getOpponent().getSalvo().size();
        if(pTurno > oTurno ){
            return "WAIT";
        }

        if(gamePlayer.getGame().getGamePlayers().size() == 2 && pTurno == oTurno){

            int pDamage = getDamage(gamePlayer);
            int oDamage = getDamage(gamePlayer.getOpponent());
            if(pDamage == 17 && oDamage == 17){
                return  "TIE";
            }else if(pDamage == 17){
                return "LOST";
            }
            else if(oDamage == 17){
                return "WON";
            }


        }

        return "PLAY";
    }

    private int getDamage(GamePlayer s){

        int totalD = 0;

        GamePlayer oponnent = s.getOpponent();
        for (Salvo salvo: oponnent.getSalvo()){

            for (String location: salvo.getSalvoLocations()){
                for (Ship ship: s.getShips()){
                    for (String shipLocation: ship.getShipLocations()){
                        if(location.equals(shipLocation)){
                            if (ship.getType().equals("carrier")) {
                                totalD++;
                            }
                            if (ship.getType().equals("battleship")) {
                                totalD++;
                            }
                            if (ship.getType().equals("submarine")) {
                                totalD++;
                            }
                            if (ship.getType().equals("destroyer")) {
                                totalD++;
                            }
                            if (ship.getType().equals("patrolboat")) {
                                totalD++;
                            }
                        }

                    }
                }


            }

        }



        return totalD;
    }

    private Set<Map<String, Object>> getHits(GamePlayer self, GamePlayer opponent){

        Set<Map<String, Object>> hits = new LinkedHashSet<>();


        int carrierDamage = 0;
        int battleshipDamage = 0;
        int submarineDamage = 0;
        int destroyerDamage = 0;
        int patrolboatDamage = 0;

        if(opponent.getSalvo() != null){
            for (Salvo salvo: opponent.getSalvo()){
                Map<String, Object> dto = new LinkedHashMap<>();
                Map<String, Object> damage = new LinkedHashMap<>();

                List<String> hitLocations = new ArrayList<>();
                int carrierHits = 0;
                int battleshipHits = 0;
                int submarineHits = 0;
                int destroyerHits = 0;
                int patrolboatHits = 0;

                int missed = salvo.getSalvoLocations().size();


                for (String location: salvo.getSalvoLocations()){
                    for (Ship ship: self.getShips()){
                        for (String shipLocation: ship.getShipLocations()){
                            if(location.equals(shipLocation)){
                                hitLocations.add(location);

                                if(ship.getType().equals("carrier")){
                                    carrierHits++;
                                    carrierDamage++;
                                    missed--;
                                }
                                if(ship.getType().equals("battleship")){
                                    battleshipHits++;
                                    battleshipDamage++;
                                    missed--;
                                }
                                if(ship.getType().equals("submarine")){
                                    submarineHits++;
                                    submarineDamage++;
                                    missed--;
                                }
                                if(ship.getType().equals("destroyer")){
                                    destroyerHits++;
                                    destroyerDamage++;
                                    missed--;
                                }
                                if(ship.getType().equals("patrolboat")){
                                    patrolboatHits++;
                                    patrolboatDamage++;
                                    missed--;
                                }
                            }

                        }
                    }


                }
                dto.put("turn", salvo.getTurn());
                damage.put("carrierHits", carrierHits);
                damage.put("battleshipHits", battleshipHits);
                damage.put("submarineHits", submarineHits);
                damage.put("destroyerHits", destroyerHits);
                damage.put("patrolboatHits", patrolboatHits);
                damage.put("carrier",carrierDamage);
                damage.put("battleship",battleshipDamage);
                damage.put("submarine",submarineDamage);
                damage.put("destroyer",destroyerDamage);
                damage.put("patrolboat",patrolboatDamage);


                dto.put("hitLocations", hitLocations);
                dto.put("damages", damage);
                dto.put("missed", missed);
                hits.add(dto);
            }
        }
        return hits;
    }


    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }
}
