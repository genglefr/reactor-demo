package com.genglefr.webflux.demo.service;

import com.genglefr.webflux.demo.model.Game;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Cacheable("games")
public class GameService extends GenericService<Game> {
}
