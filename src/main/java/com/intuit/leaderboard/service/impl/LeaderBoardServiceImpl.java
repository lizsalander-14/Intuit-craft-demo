package com.intuit.leaderboard.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.leaderboard.dto.PlayerScoreDTO;
import com.intuit.leaderboard.entity.Player;
import com.intuit.leaderboard.entity.PlayerScores;
import com.intuit.leaderboard.repository.PlayerRepository;
import com.intuit.leaderboard.repository.PlayerScoresRepository;
import com.intuit.leaderboard.service.LeaderBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LeaderBoardServiceImpl implements LeaderBoardService {

    @Autowired
    private PlayerScoresRepository playerScoresRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private static final ObjectMapper mapper=new ObjectMapper();

    @Override
    public boolean savePlayerScore(String payload) {
        try {
            PlayerScoreDTO scoreDto = mapper.readValue(payload, PlayerScoreDTO.class);
            PlayerScores score = PlayerScores.builder().playerId(scoreDto.getPlayerId())
                    .score(scoreDto.getScore()).build();
            playerScoresRepository.save(score);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse payload: {} for player score", payload, e);
            return false;
        } catch (Exception e){
            log.error("Cannot store data for payload: {} as it is invalid", payload, e);
            return false;
        }
        return true;
    }

    @Override
    public List<PlayerScoreDTO> getTopScorerDetails() {
        List<PlayerScoreDTO> playerScoreDTOS = getPreviousTopScorers();
        if(!playerScoreDTOS.isEmpty()) {
            List<Player> playerDetails = playerRepository.findByIdIn(
                    playerScoreDTOS.stream().map(PlayerScoreDTO::getPlayerId).collect(Collectors.toList()));
            Map<Integer, String> playerIdNameMap = playerDetails.stream()
                    .collect(Collectors.toMap(Player::getId, Player::getName));
            playerScoreDTOS.forEach(dto -> dto.setName(playerIdNameMap.get(dto.getPlayerId())));
        }
        return playerScoreDTOS;
    }

    @Override
    public List<PlayerScoreDTO> getPreviousTopScorers(){
        List<PlayerScoreDTO> playerScoreDTOS=new ArrayList<>();
        String previousEntries= String.valueOf(redisTemplate.opsForValue().get("top_players"));
        if(!"null".equals(previousEntries)) {
            try {
                playerScoreDTOS = mapper.readValue(previousEntries, new TypeReference<List<PlayerScoreDTO>>() {});
            } catch (JsonProcessingException e) {
                log.error("Error converting {} to list", previousEntries);
                throw new RuntimeException(e);
            }
        }
        return playerScoreDTOS;
    }

    @Override
    public void saveNewTopScorers(List<PlayerScoreDTO> playerScoreDTOS) {
        try {
            redisTemplate.opsForValue().set("top_players",
                    mapper.writeValueAsString(playerScoreDTOS.subList(0,5)));
        } catch (JsonProcessingException e) {
            log.error("Error converting {} to string", playerScoreDTOS);
            throw new RuntimeException(e);
        }
    }
}
