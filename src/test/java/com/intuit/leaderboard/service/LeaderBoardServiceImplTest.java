package com.intuit.leaderboard.service;

import com.intuit.leaderboard.entity.PlayerScores;
import com.intuit.leaderboard.repository.PlayerScoresRepository;
import com.intuit.leaderboard.service.impl.LeaderBoardServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LeaderBoardServiceImplTest {

    @InjectMocks
    private LeaderBoardServiceImpl leaderBoardService;

    @Mock
    private PlayerScoresRepository playerScoresRepository;

    @Test
    public void savePlayerScoreTest_parsingError(){
        boolean response=leaderBoardService.savePlayerScore("Invalid string");

        Assert.assertFalse(response);
    }

    @Test
    public void savePlayerScoreTest_dbFailure(){
        PlayerScores score = PlayerScores.builder().playerId(1).score(100).build();
        Mockito.when(playerScoresRepository.save(score)).thenThrow(new RuntimeException("some db failure"));

        boolean response=leaderBoardService.savePlayerScore("{\"player_id\":1,\"score\":100}");

        Assert.assertFalse(response);
        Mockito.verify(playerScoresRepository).save(score);
    }

    @Test
    public void savePlayerScoreTest_success(){
        PlayerScores score = PlayerScores.builder().playerId(1).score(100).build();
        boolean response=leaderBoardService.savePlayerScore("{\"player_id\":1,\"score\":100}");

        Assert.assertTrue(response);
        Mockito.verify(playerScoresRepository).save(score);
    }

    @After
    public void destroy(){
        Mockito.verifyNoMoreInteractions(playerScoresRepository);
    }
}
