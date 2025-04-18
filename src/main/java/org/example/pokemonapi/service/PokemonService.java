package org.example.pokemonapi.service;

import org.example.pokemonapi.exception.PokemonNotFoundException;
import org.example.pokemonapi.model.Pokemon;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
    private RestTemplate restTemplate; // = new RestTemplate();
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public PokemonService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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
        try {
            PokemonResponse response = restTemplate.getForObject(url, PokemonResponse.class);

            if (response == null) {
                throw new PokemonNotFoundException("Pokémon with name or ID '" + nameOrId + "' not found.");
            }

            return new Pokemon(
                    response.getName(),
                    response.getWeight(),
                    response.getHeight(),
                    response.getBaseExperience()
            );
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new PokemonNotFoundException("Pokémon with name or ID '" + nameOrId + "' not found.");
            }
            throw e; // rethrow other unexpected errors
        }
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

    public static class PokemonResponse {
        public String name;
        public int weight;
        public int height;
        public int base_experience;

        public PokemonResponse() {}

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

    public static class PokemonListResponse {
        public List<PokemonResult> results;

        public PokemonListResponse() {}

        public PokemonListResponse(List<PokemonResult> results) {
            this.results = results;
        }

        public List<PokemonResult> getResults() { return results; }
    }

    public static class PokemonResult {
        private String name;
        private String url;

        public PokemonResult() {}

        public PokemonResult(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() { return name; }
        public String getUrl() { return url; }
    }
}