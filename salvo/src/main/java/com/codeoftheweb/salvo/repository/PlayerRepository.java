package com.codeoftheweb.salvo.repository;


import com.codeoftheweb.salvo.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByUserName(String userName);
}
