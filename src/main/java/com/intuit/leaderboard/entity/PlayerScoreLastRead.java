package com.intuit.leaderboard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "PLAYER_SCORE_LAST_READ")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PlayerScoreLastRead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "LAST_SCORE_ID")
    private int lastScoreId;

    @Column(name = "UPDATED_ON")
    @UpdateTimestamp
    private Timestamp updatedOn;
}
