package org.example.pokemonapi.service;

import org.example.pokemonapi.model.Pokemon;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class PokemonService {
    private static final String POKEAPI_URL = "https://pokeapi.co/api/v2/pokemon/";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Cacheable("allPokemons")
    public List<Pokemon> getAllPokemons() {
        PokemonListResponse response = restTemplate.getForObject(POKEAPI_URL, PokemonListResponse.class);
        if (response == null || response.getResults() == null) {
            throw new RuntimeException("Failed to fetch Pokémon list");
        }

        return response.getResults().parallelStream()
                .map(result -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return getPokemon(result.getName());
                    } catch (Exception e) {
                        return null;
                    }
                }, executor))
                .map(CompletableFuture::join)
                .filter(p -> p != null)
                .collect(Collectors.toList());
    }

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

    static class PokemonResponse {
        String name;
        int weight;
        int height;
        int base_experience;

        // No-args constructor
        public PokemonResponse() {}

        // All-args constructor
        public PokemonResponse(String name, int weight, int height, int base_experience) {
            this.name = name;
            this.weight = weight;
            this.height = height;
            this.base_experience = base_experience;
        }

        public String getName() { return name; }
        public int getWeight() { return weight; }
        public int getHeight() { return height; }
        public int getBaseExperience() { return base_experience; }
    }

    static class PokemonListResponse {
        List<PokemonResult> results;

        // No-args constructor
        public PokemonListResponse() {}

        // All-args constructor
        public PokemonListResponse(List<PokemonResult> results) {
            this.results = results;
        }

        public List<PokemonResult> getResults() { return results; }
    }

    static class PokemonResult {
        private String name;
        private String url;

        // No-args constructor
        public PokemonResult() {}

        // All-args constructor
        public PokemonResult(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() { return name; }
        public String getUrl() { return url; }
    }
}