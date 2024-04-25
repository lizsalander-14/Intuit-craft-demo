package com.intuit.leaderboard.scheduler;

import com.intuit.leaderboard.dto.PlayerScoreDTO;
import com.intuit.leaderboard.entity.PlayerScoreLastRead;
import com.intuit.leaderboard.entity.PlayerScores;
import com.intuit.leaderboard.repository.PlayerScoreLastReadRepository;
import com.intuit.leaderboard.repository.PlayerScoresRepository;
import com.intuit.leaderboard.service.LeaderBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class TopScorerScheduler {

    @Autowired
    private LeaderBoardService leaderBoardService;

    @Autowired
    private PlayerScoreLastReadRepository playerScoreLastReadRepository;

    @Autowired
    private PlayerScoresRepository playerScoresRepository;

//    @Scheduled(cron="0 */3 * ? * *")
    public void calculateTopScorers(){
        log.info("Starting scheduler to calculate top scorers");
        Optional<PlayerScoreLastRead> playerScoreLastRead = playerScoreLastReadRepository.getEntry();
        Integer lastPlayerScoreEntryId = playerScoresRepository.getLatestEntryId();
        List<PlayerScores> newPlayerScoreEntries=playerScoresRepository.
                getTop5ScorersByLastReadIndex(
                        playerScoreLastRead.map(PlayerScoreLastRead::getLastScoreId).orElse(0),
                        lastPlayerScoreEntryId
                );
        List<PlayerScoreDTO> previousEntries = leaderBoardService.getPreviousTopScorers();
        newPlayerScoreEntries.forEach(entry->{
            previousEntries.add(new PlayerScoreDTO(entry.getPlayerId(),null, entry.getScore(),
                    entry.getCreatedOn()));
        });
        Comparator<PlayerScoreDTO> comparator=Comparator.comparingDouble(PlayerScoreDTO::getScore)
                        .thenComparing(PlayerScoreDTO::getCreatedOn).reversed();
        previousEntries.sort(comparator);
        leaderBoardService.saveNewTopScorers(previousEntries);
        updateLastReadValue(playerScoreLastRead, lastPlayerScoreEntryId);
        log.info("Completed scheduler to calculate top scorers");
    }

    private void updateLastReadValue(Optional<PlayerScoreLastRead> playerScoreLastRead, int lastReadIndex){
        PlayerScoreLastRead lastRead= playerScoreLastRead.orElseGet(PlayerScoreLastRead::new);
        lastRead.setLastScoreId(lastReadIndex);
        playerScoreLastReadRepository.save(lastRead);
    }
}
