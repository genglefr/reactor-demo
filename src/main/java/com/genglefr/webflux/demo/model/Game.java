package com.genglefr.webflux.demo.model;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import lombok.Data;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

@Document
@Data
public class Game implements Entity {
    private static final int max_score = 5;
    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    String id;
    @Field
    String teamHome;
    @Field
    Integer teamHomeScore;
    @Field
    String teamAway;
    @Field
    Integer teamAwayScore;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Game reset() {
        this.teamHomeScore = 0;
        this.teamAwayScore = 0;
        return this;
    }

    public Game randomize() {
        Random rand = new Random();
        this.teamHomeScore = rand.nextInt(max_score);
        this.teamAwayScore = rand.nextInt(max_score);
        return this;
    }

    public boolean isFavorite(List<String> teams) {
        if (CollectionUtils.isEmpty(teams)) {
            return false;
        }
        return teams.contains(this.teamHome) || teams.contains(this.teamAway);
    }
}
