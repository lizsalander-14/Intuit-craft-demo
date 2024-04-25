package com.intuit.leaderboard.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "PLAYER_SCORES")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PlayerScores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "PLAYER_ID")
    private int playerId;

    @Column(name = "SCORE")
    private double score;

    @Column(name = "CREATED_ON")
    @CreationTimestamp
    private Timestamp createdOn;
}
