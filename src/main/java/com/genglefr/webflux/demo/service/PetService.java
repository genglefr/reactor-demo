package com.genglefr.webflux.demo.service;

import com.genglefr.webflux.demo.integration.PetRepository;
import com.genglefr.webflux.demo.model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PetService extends GenericService<Pet> {

    @Autowired
    PetRepository petRepository;

    @Override
    public Pet save(Pet pet) {
        return this.petRepository.save(pet);
    }

    @Override
    public void delete(String id) {
        this.petRepository.deleteById(id);
    }

    public Optional<Pet> findById(String id) {
        return this.petRepository.findById(id);
    }
}
