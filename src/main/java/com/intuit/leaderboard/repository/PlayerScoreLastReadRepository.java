package com.intuit.leaderboard.repository;

import com.intuit.leaderboard.entity.PlayerScoreLastRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerScoreLastReadRepository extends JpaRepository<PlayerScoreLastRead, Integer> {

    @Query("SELECT pslr FROM PlayerScoreLastRead pslr ORDER BY pslr.id DESC LIMIT 1")
    Optional<PlayerScoreLastRead> getEntry();
}
