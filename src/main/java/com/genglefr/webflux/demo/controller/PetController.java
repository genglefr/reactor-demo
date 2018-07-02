package com.genglefr.webflux.demo.controller;

import com.genglefr.webflux.demo.model.EmptyEntity;
import com.genglefr.webflux.demo.model.Event;
import com.genglefr.webflux.demo.model.OperationType;
import com.genglefr.webflux.demo.model.Pet;
import com.genglefr.webflux.demo.service.EventService;
import com.genglefr.webflux.demo.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PetController {

    @Autowired
    private EventService eventService;
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

    @PostMapping(value = "pet/event/{id}")
    public void handle(@PathVariable("id") final String id, @RequestBody Pet pet) {
        this.eventService.handleEvent(id, pet);
    }

    @DeleteMapping(value = "pet/{id}")
    public void delete(@PathVariable("id") String id) {
        this.petService.delete(id);
    }
}
