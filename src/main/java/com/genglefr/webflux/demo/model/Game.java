package com.genglefr.webflux.demo.model;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import lombok.Data;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Random;

@Document
@Data
public class Game implements Entity {
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

    public void reset() {
        this.teamHomeScore = 0;
        this.teamAwayScore = 0;
    }

    public void randomize() {
        Random rand = new Random();
        int bound = 5;
        this.teamHomeScore = rand.nextInt(bound);
        this.teamAwayScore = rand.nextInt(bound);
    }

    public boolean hasScored() {
        return this.teamHomeScore != 0 || this.teamAwayScore != 0;
    }

    public boolean isFavorite(List<String> teams) {
        if (CollectionUtils.isEmpty(teams)) {
            return true;
        }
        return teams.contains(this.teamHome) || teams.contains(this.teamAway);
    }
}
