package org.example.pokemonapi.service;

import org.example.pokemonapi.model.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PokemonServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PokemonService pokemonService;

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
        PokemonService.PokemonResponse mockResponse = new PokemonService.PokemonResponse();
        mockResponse.name = "bulbasaur";
        mockResponse.weight = 69;
        mockResponse.height = 7;
        mockResponse.base_experience = 0;

        when(restTemplate.getForObject(anyString(), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(mockResponse);

        Pokemon result = pokemonService.getPokemon("bulbasaur");

        assertEquals("bulbasaur", result.getName());
        assertEquals(69, result.getWeight());
        assertEquals(7, result.getHeight());
        assertEquals(0, result.getBaseExperience());
    }

    @Test
    void getHeaviestPokemons_ShouldReturnCorrectOrder() {
        PokemonService.PokemonListResponse mockListResponse = new PokemonService.PokemonListResponse(
                Arrays.asList(
                        new PokemonService.PokemonResult("venusaur", ""),
                        new PokemonService.PokemonResult("charizard", ""),
                        new PokemonService.PokemonResult("blastoise", ""),
                        new PokemonService.PokemonResult("pidgeot", ""),
                        new PokemonService.PokemonResult("butterfree", "")
                )
        );

        when(restTemplate.getForObject(anyString(), eq(PokemonService.PokemonListResponse.class)))
                .thenReturn(mockListResponse);

        when(restTemplate.getForObject(contains("venusaur"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(new PokemonService.PokemonResponse("venusaur", 1000, 20, 0));
        when(restTemplate.getForObject(contains("charizard"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(new PokemonService.PokemonResponse("charizard", 905, 17, 0));
        when(restTemplate.getForObject(contains("blastoise"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(new PokemonService.PokemonResponse("blastoise", 855, 16, 0));
        when(restTemplate.getForObject(contains("pidgeot"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(new PokemonService.PokemonResponse("pidgeot", 395, 15, 0));
        when(restTemplate.getForObject(contains("butterfree"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(new PokemonService.PokemonResponse("butterfree", 320, 11, 0));

        List<Pokemon> result = pokemonService.getHeaviestPokemons(5);

        assertEquals(5, result.size());
        assertEquals("venusaur", result.get(0).getName());
        assertEquals("charizard", result.get(1).getName());
        assertEquals("blastoise", result.get(2).getName());
        assertEquals("pidgeot", result.get(3).getName());
        assertEquals("butterfree", result.get(4).getName());

        assertEquals(1000, result.get(0).getWeight());
        assertEquals(905, result.get(1).getWeight());
        assertEquals(855, result.get(2).getWeight());
        assertEquals(395, result.get(3).getWeight());
        assertEquals(320, result.get(4).getWeight());
    }

    @Test
    void getHighestPokemons_ShouldReturnCorrectOrder() {
        PokemonService.PokemonListResponse mockListResponse = new PokemonService.PokemonListResponse(
                Arrays.asList(
                        new PokemonService.PokemonResult("venusaur", ""),
                        new PokemonService.PokemonResult("charizard", ""),
                        new PokemonService.PokemonResult("blastoise", ""),
                        new PokemonService.PokemonResult("pidgeot", ""),
                        new PokemonService.PokemonResult("charmeleon", "")
                )
        );

        when(restTemplate.getForObject(anyString(), eq(PokemonService.PokemonListResponse.class)))
                .thenReturn(mockListResponse);

        when(restTemplate.getForObject(contains("venusaur"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(new PokemonService.PokemonResponse("venusaur", 1000, 20, 0));
        when(restTemplate.getForObject(contains("charizard"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(new PokemonService.PokemonResponse("charizard", 905, 17, 0));
        when(restTemplate.getForObject(contains("blastoise"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(new PokemonService.PokemonResponse("blastoise", 855, 16, 0));
        when(restTemplate.getForObject(contains("pidgeot"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(new PokemonService.PokemonResponse("pidgeot", 395, 15, 0));
        when(restTemplate.getForObject(contains("charmeleon"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(new PokemonService.PokemonResponse("charmeleon", 190, 11, 0));

        List<Pokemon> result = pokemonService.getHighestPokemons(5);

        assertEquals(5, result.size());
        assertEquals("venusaur", result.get(0).getName());
        assertEquals(20, result.get(0).getHeight());
        assertEquals("charizard", result.get(1).getName());
        assertEquals(17, result.get(1).getHeight());
        assertEquals("blastoise", result.get(2).getName());
        assertEquals(16, result.get(2).getHeight());
        assertEquals("pidgeot", result.get(3).getName());
        assertEquals(15, result.get(3).getHeight());
        assertEquals("charmeleon", result.get(4).getName());
        assertEquals(11, result.get(4).getHeight());
    }

    @Test
    void getPokemonsByBaseExperience_ShouldReturnAll() {
        PokemonService.PokemonListResponse mockListResponse = new PokemonService.PokemonListResponse();
        mockListResponse.results = Arrays.asList(
                new PokemonService.PokemonResult("bulbasaur", ""),
                new PokemonService.PokemonResult("ivysaur", ""),
                new PokemonService.PokemonResult("venusaur", ""),
                new PokemonService.PokemonResult("charmander", ""),
                new PokemonService.PokemonResult("charmeleon", "")
        );

        when(restTemplate.getForObject(anyString(), eq(PokemonService.PokemonListResponse.class)))
                .thenReturn(mockListResponse);

        when(restTemplate.getForObject(contains("bulbasaur"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(createMockResponse("bulbasaur", 69, 7, 0));
        when(restTemplate.getForObject(contains("ivysaur"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(createMockResponse("ivysaur", 130, 10, 0));
        when(restTemplate.getForObject(contains("venusaur"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(createMockResponse("venusaur", 1000, 20, 0));
        when(restTemplate.getForObject(contains("charmander"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(createMockResponse("charmander", 85, 6, 0));
        when(restTemplate.getForObject(contains("charmeleon"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(createMockResponse("charmeleon", 190, 11, 0));

        List<Pokemon> result = pokemonService.getPokemonsByBaseExperience(5);

        assertEquals(5, result.size());
        assertEquals(0, result.get(0).getBaseExperience());
        assertEquals(0, result.get(4).getBaseExperience());
    }

    private PokemonService.PokemonResponse createMockResponse(String name, int weight, int height, int baseExperience) {
        PokemonService.PokemonResponse response = new PokemonService.PokemonResponse();
        response.name = name;
        response.weight = weight;
        response.height = height;
        response.base_experience = baseExperience;
        return response;
    }
}