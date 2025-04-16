package org.example.pokemonapi.integration.controller;

import org.example.pokemonapi.controller.PokemonController;
import org.example.pokemonapi.exception.PokemonNotFoundException;
import org.example.pokemonapi.model.Pokemon;
import org.example.pokemonapi.service.PokemonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PokemonController.class)
public class PokemonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;

    private final Pokemon pikachu = new Pokemon("pikachu", 60, 4, 112);
    private final List<Pokemon> top5Heaviest = List.of(
            new Pokemon("snorlax", 4600, 21, 189),
            new Pokemon("charizard", 905, 17, 240)
    );

    @Test
    void getPokemon_ReturnsPokemon_WhenNameValid() throws Exception {
        when(pokemonService.getPokemon("pikachu")).thenReturn(pikachu);

        mockMvc.perform(get("/api/pokemon/pikachu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("pikachu"))
                .andExpect(jsonPath("$.weight").value(60));
    }

    @Test
    void getPokemon_Returns404_WhenPokemonNotFound() throws Exception {
        when(pokemonService.getPokemon("missing")).thenThrow(
                new PokemonNotFoundException("Pokémon not found")
        );

        mockMvc.perform(get("/api/pokemon/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pokémon not found"));
    }

    @Test
    void getTop5Heaviest_ReturnsSortedList() throws Exception {
        when(pokemonService.getHeaviestPokemons(5)).thenReturn(top5Heaviest);

        mockMvc.perform(get("/api/pokemon/heaviest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("snorlax"))
                .andExpect(jsonPath("$[1].weight").value(905));
    }
}