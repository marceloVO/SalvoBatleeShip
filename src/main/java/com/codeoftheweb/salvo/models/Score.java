package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game gamesScore;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player playerScore;

    private double score;

    private Date finishDate;

    public Score() {
    }

    public Score(Game gamesScore, Player playerScore, double score) {
        this.gamesScore = gamesScore;
        this.playerScore = playerScore;
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public Game getGamesScore() {
        return gamesScore;
    }

    public void setGamesScore(Game gamesScore) {
        this.gamesScore = gamesScore;
    }

    public Player getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(Player players) {
        this.playerScore = players;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = new Date();
    }




    public Map<String, Object> scoreInfo(){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("score",getScore());
        dto.put("player",getPlayerScore().getId());
        return dto;
    }
}
