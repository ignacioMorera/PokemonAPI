package org.example.pokemonapi.integration.service;

import org.example.pokemonapi.model.Pokemon;
import org.example.pokemonapi.service.PokemonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PokemonServiceIntegrationTest {

    @Autowired
    private PokemonService pokemonService;

    @Test
    void getPokemon_ShouldReturnPokemon_WhenValidNameProvided() {
        Pokemon result = pokemonService.getPokemon("pikachu");

        assertNotNull(result);
        assertEquals("pikachu", result.getName());
        assertTrue(result.getWeight() > 0);
        assertTrue(result.getHeight() > 0);
        assertTrue(result.getBaseExperience() > 0);
    }

    @Test
    void getHeaviestPokemons_ShouldReturnCorrectOrder() {
        List<Pokemon> result = pokemonService.getHeaviestPokemons(5);

        assertEquals(5, result.size());

        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).getWeight() >= result.get(i + 1).getWeight(),
                    "Pokemon at position " + i + " (" + result.get(i).getName() +
                            ") should be heavier than position " + (i + 1) + " (" +
                            result.get(i + 1).getName() + ")");
        }
    }

    @Test
    void getHighestPokemons_ShouldReturnCorrectOrder() {
        List<Pokemon> result = pokemonService.getHighestPokemons(5);

        assertEquals(5, result.size());

        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).getHeight() >= result.get(i + 1).getHeight(),
                    "Pokemon at position " + i + " (" + result.get(i).getName() +
                            ") should be taller than position " + (i + 1) + " (" +
                            result.get(i + 1).getName() + ")");
        }
    }

    @Test
    void getPokemonsByBaseExperience_ShouldReturnMostExperiencedFirst() {
        List<Pokemon> result = pokemonService.getPokemonsByBaseExperience(5);

        assertEquals(5, result.size());

        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).getBaseExperience() >= result.get(i + 1).getBaseExperience(),
                    "Pokemon at position " + i + " (" + result.get(i).getName() +
                            ") should have more experience than position " + (i + 1) + " (" +
                            result.get(i + 1).getName() + ")");
        }
    }
}