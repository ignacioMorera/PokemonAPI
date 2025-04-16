package org.example.pokemonapi.unit.controller;

import org.example.pokemonapi.controller.PokemonController;
import org.example.pokemonapi.model.Pokemon;
import org.example.pokemonapi.service.PokemonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PokemonControllerTest {

    @Mock
    private PokemonService pokemonService;

    @InjectMocks
    private PokemonController pokemonController;

    private final List<Pokemon> testPokemons = Arrays.asList(
            new Pokemon("bulbasaur", 69, 7, 0),
            new Pokemon("ivysaur", 130, 10, 0),
            new Pokemon("venusaur", 1000, 20, 0),
            new Pokemon("charmander", 85, 6, 0),
            new Pokemon("charmeleon", 190, 11, 0)
    );

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPokemon_ShouldReturnPokemon_WhenValidNameProvided() {
        when(pokemonService.getPokemon("bulbasaur")).thenReturn(testPokemons.get(0));

        Pokemon result = pokemonController.getPokemon("bulbasaur");

        assertEquals("bulbasaur", result.getName());
        assertEquals(69, result.getWeight());
        assertEquals(7, result.getHeight());
        assertEquals(0, result.getBaseExperience());
    }

    @Test
    void getTop5HeaviestPokemons_ShouldReturnCorrectOrder() {
        List<Pokemon> expectedOrder = Arrays.asList(
                testPokemons.get(2),
                testPokemons.get(4),
                testPokemons.get(1),
                testPokemons.get(3),
                testPokemons.get(0)
        );
        when(pokemonService.getHeaviestPokemons(5)).thenReturn(expectedOrder);

        List<Pokemon> result = pokemonController.getTop5HeaviestPokemons();

        assertEquals(1000, result.get(0).getWeight());
        assertEquals(190, result.get(1).getWeight());
        assertEquals(69, result.get(4).getWeight());
    }

    @Test
    void getTop5HighestPokemons_ShouldReturnCorrectOrder() {
        List<Pokemon> expectedOrder = Arrays.asList(
                testPokemons.get(2),
                testPokemons.get(4),
                testPokemons.get(1),
                testPokemons.get(0),
                testPokemons.get(3)
        );
        when(pokemonService.getHighestPokemons(5)).thenReturn(expectedOrder);

        List<Pokemon> result = pokemonController.getTop5HighestPokemons();

        assertEquals(20, result.get(0).getHeight());
        assertEquals(11, result.get(1).getHeight());
        assertEquals(6, result.get(4).getHeight());
    }

    @Test
    void getTop5MostExperiencedPokemons_ShouldReturnAllWithSameExperience() {
        when(pokemonService.getPokemonsByBaseExperience(5)).thenReturn(testPokemons);

        List<Pokemon> result = pokemonController.getTop5MostExperiencedPokemons();

        assertEquals(5, result.size());
        assertEquals(0, result.get(0).getBaseExperience());
        assertEquals(0, result.get(4).getBaseExperience());
    }

    @Test
    void getPokemon_ShouldHandleServiceException() {
        when(pokemonService.getPokemon("missing")).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            pokemonController.getPokemon("missing");
        });
    }
}