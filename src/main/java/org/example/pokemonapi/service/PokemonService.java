package org.example.pokemonapi.service;

import org.example.pokemonapi.model.Pokemon;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonService {

    private static final String POKEAPI_URL = "https://pokeapi.co/api/v2/pokemon/";
    private final RestTemplate restTemplate = new RestTemplate();

    public Pokemon getPokemon(String nameOrId) {
        String url = POKEAPI_URL + nameOrId.toLowerCase();
        PokemonResponse response = restTemplate.getForObject(url, PokemonResponse.class);

        if (response == null) {
            throw new RuntimeException("Pokémon not found!");
        }

        return new Pokemon(
                response.getName(),
                response.getWeight(),
                response.getHeight(),
                response.getBaseExperience()
        );
    }

    @Cacheable("allPokemons")
    public List<Pokemon> getAllPokemons() {
        List<Pokemon> pokemons = new ArrayList<>();
        String[] demoPokemons = {"snorlax", "charizard", "gyarados", "mewtwo", "onix",
                "steelix", "rayquaza", "wailord", "groudon", "kyogre"};

        for (String name : demoPokemons) {
            try {
                pokemons.add(getPokemon(name));
            } catch (Exception e) {
                continue;
            }
        }
        return pokemons;
    }

    public List<Pokemon> getHeaviestPokemons(int limit) {
        return getAllPokemons().stream()
                .sorted(Comparator.comparingInt(Pokemon::getWeight).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Pokemon> getHighestPokemons(int limit) {
        return getAllPokemons().stream()
                .sorted(Comparator.comparingInt(Pokemon::getHeight).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Pokemon> getPokemonsByBaseExperience(int limit) {
        return getAllPokemons().stream()
                .sorted(Comparator.comparingInt(Pokemon::getBaseExperience).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private static class PokemonResponse {
        private String name;
        private int weight;
        private int height;
        private int base_experience;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getBaseExperience() {
            return base_experience;
        }

        public void setBase_experience(int base_experience) {
            this.base_experience = base_experience;
        }
    }
}