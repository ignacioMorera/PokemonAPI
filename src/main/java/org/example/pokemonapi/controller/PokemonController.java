package org.example.pokemonapi.controller;

import org.example.pokemonapi.model.Pokemon;
import org.example.pokemonapi.service.PokemonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/{nameOrId}")
    public Pokemon getPokemon(@PathVariable String nameOrId) {
        return pokemonService.getPokemon(nameOrId);
    }

    @GetMapping("/heaviest")
    public List<Pokemon> getTop5HeaviestPokemons() {
        return pokemonService.getHeaviestPokemons(5);
    }

    @GetMapping("/highest")
    public List<Pokemon> getTop5HighestPokemons() {
        return pokemonService.getHighestPokemons(5);
    }

    @GetMapping("/most-experienced")
    public List<Pokemon> getTop5MostExperiencedPokemons() {
        return pokemonService.getPokemonsByBaseExperience(5);
    }
}