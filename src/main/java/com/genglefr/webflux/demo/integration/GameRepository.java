package com.genglefr.webflux.demo.integration;

import com.genglefr.webflux.demo.model.Game;
import com.genglefr.webflux.demo.model.Pet;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;

@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "Game")
public interface GameRepository extends CouchbasePagingAndSortingRepository<Game, String> {
}
