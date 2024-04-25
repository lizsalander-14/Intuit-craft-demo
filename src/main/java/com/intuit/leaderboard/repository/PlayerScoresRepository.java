package com.intuit.leaderboard.repository;

import com.intuit.leaderboard.entity.PlayerScores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerScoresRepository extends JpaRepository<PlayerScores, Integer> {

    @Query("SELECT ps.id FROM PlayerScores ps ORDER BY ps.id DESC LIMIT 1")
    Integer getLatestEntryId();

    @Query("SELECT ps FROM PlayerScores ps WHERE ps.id > :startId AND ps.id<= :endId " +
            "ORDER BY ps.score DESC, ps.createdOn DESC LIMIT 5")
    List<PlayerScores> getTop5ScorersByLastReadIndex(@Param("startId")int startId, @Param("endId")int endId);
}
