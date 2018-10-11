package com.genglefr.webflux.demo.integration;

import com.genglefr.webflux.demo.model.Game;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseSortingRepository;

@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "Game")
public interface GameRepository extends ReactiveCouchbaseSortingRepository<Game, String> {
}
