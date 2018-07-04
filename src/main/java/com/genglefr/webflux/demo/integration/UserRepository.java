package com.genglefr.webflux.demo.integration;

import com.genglefr.webflux.demo.model.User;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "User")
public interface UserRepository extends CouchbasePagingAndSortingRepository<User, String> {
}
