package com.intuit.leaderboard.service;

import com.intuit.leaderboard.dto.PlayerScoreDTO;

import java.util.List;

public interface LeaderBoardService {

    boolean savePlayerScore(String payload);

    List<PlayerScoreDTO> getTopScorerDetails();

    List<PlayerScoreDTO> getPreviousTopScorers();

    void saveNewTopScorers(List<PlayerScoreDTO> playerScoreDTOS);
}
