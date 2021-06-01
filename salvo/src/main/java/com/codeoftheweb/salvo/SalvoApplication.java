package com.codeoftheweb.salvo;


import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}


	@Bean
    public CommandLineRunner initData(PlayerRepository PlayerRep, GameRepository GameRep, GamePlayerRepository GamePlayer, ShipRepository ship) {
        return (args) -> {
            Date fecha =  new Date();

            Player p1 = new Player("Marcelo@gmail.com");
            Player p2 = new Player("Matias@gmail.com");
            Player p3 = new Player("Anakim@gmail.com");

            PlayerRep.save(p1);
            PlayerRep.save(p2);
            PlayerRep.save(p3);

            Game game_1 = new Game(new Date());
            Game game_2 = new Game(fecha.from(fecha.toInstant().plusSeconds(3600)));
            Game game_3 = new Game(fecha.from(fecha.toInstant().plusSeconds(7200)));

            GameRep.save(game_1);
            GameRep.save(game_2);
            GameRep.save(game_3);

            GamePlayer gamePlayers1 = new GamePlayer(game_1,p1, new Date());
            GamePlayer gamePlayers2 = new GamePlayer(game_1,p1, new Date());
            GamePlayer gamePlayers3 = new GamePlayer(game_2,p3, new Date());
            GamePlayer gamePlayers4 = new GamePlayer(game_2,p2, new Date());
            GamePlayer gamePlayers5 = new GamePlayer(game_3,p3, new Date());
            GamePlayer gamePlayers6 = new GamePlayer(game_3,p1, new Date());

            GamePlayer.save(gamePlayers1);
            GamePlayer.save(gamePlayers2);
            GamePlayer.save(gamePlayers3);
            GamePlayer.save(gamePlayers4);
            GamePlayer.save(gamePlayers5);
            GamePlayer.save(gamePlayers6);

            Ship ship1 = new Ship(gamePlayers1,Arrays.asList("H2","H3","H4"),"Destroyer");
            Ship ship2 = new Ship(gamePlayers1,Arrays.asList("E1","E2"),"Patrol Boat");
            Ship ship3 = new Ship(gamePlayers2,Arrays.asList("H2","H3","H4"),"Destroyer");
            Ship ship4 = new Ship(gamePlayers2,Arrays.asList("G1","G2","G3","G4"),"Submarine");
            Ship ship5 = new Ship(gamePlayers3,Arrays.asList("H2","H3","H4"),"Destroyer");
            Ship ship6 = new Ship(gamePlayers3,Arrays.asList("E1","F2","G3"),"Battleship");
            Ship ship7 = new Ship(gamePlayers4,Arrays.asList("H2","H3","H4"),"Destroyer");
            Ship ship8 = new Ship(gamePlayers4,Arrays.asList("G1","G2","G3","G4"),"Submarine");
            Ship ship9 = new Ship(gamePlayers5,Arrays.asList("H2","H3","H4"),"Destroyer");
            Ship ship10 = new Ship(gamePlayers5,Arrays.asList("E1","F2","G3"),"Battleship");
            Ship ship11 = new Ship(gamePlayers6,Arrays.asList("H2","H3","H4"),"Destroyer");
            Ship ship12 = new Ship(gamePlayers6,Arrays.asList("G1","G2","G3","G4"),"Submarine");

            ship.save(ship1);
            ship.save(ship2);
            ship.save(ship3);
            ship.save(ship4);
            ship.save(ship5);
            ship.save(ship6);
            ship.save(ship7);
            ship.save(ship8);
            ship.save(ship9);
            ship.save(ship10);
            ship.save(ship11);
            ship.save(ship12);




        };
    }
}

