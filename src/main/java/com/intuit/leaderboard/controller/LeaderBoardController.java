package com.intuit.leaderboard.controller;

import com.intuit.leaderboard.dto.PlayerScoreDTO;
import com.intuit.leaderboard.scheduler.TopScorerScheduler;
import com.intuit.leaderboard.service.LeaderBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@Slf4j
public class LeaderBoardController {

    @Autowired
    private LeaderBoardService leaderBoardService;

    @Autowired
    private TopScorerScheduler topScorerScheduler;

    @PostMapping("/player-score")
    public ResponseEntity<Boolean> publishPlayerScore(@RequestBody String payload){
        log.info("Got payload: {} for storing player score", payload);
        return new ResponseEntity<>(leaderBoardService.savePlayerScore(payload), HttpStatus.OK);
    }

    @GetMapping("/top-5-players")
    public ResponseEntity<List<PlayerScoreDTO>> getCurrentTop5Players(){
        log.info("Got request to get top 5 players");
        return new ResponseEntity<>(leaderBoardService.getTopScorerDetails(), HttpStatus.OK);
    }

    @GetMapping("/test")
    public void test(){
        topScorerScheduler.calculateTopScorers();
    }
}
