package com.genglefr.webflux.demo.controller;

import com.genglefr.webflux.demo.model.Pet;
import com.genglefr.webflux.demo.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping(value = "pets")
    public Iterable<Pet> all() {
        return this.petService.findAll();
    }

    @PostMapping(value = "pet")
    public void create(@RequestBody Pet pet) {
        this.petService.save(pet);
    }

    @PostMapping(value = "pet/{id}")
    public void update(@RequestBody Pet pet, @PathVariable("id") String id) {
        pet.setId(id);
        this.petService.save(pet);
    }

    @DeleteMapping(value = "pet/{id}")
    public void delete(@PathVariable("id") String id) {
        this.petService.delete(id);
    }
}
