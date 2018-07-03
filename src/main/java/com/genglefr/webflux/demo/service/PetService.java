package com.genglefr.webflux.demo.service;

import com.genglefr.webflux.demo.model.Pet;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Cacheable("pets")
public class PetService extends GenericService<Pet> {
}
