package org.example.pokemonapi.unit.service;

import org.example.pokemonapi.exception.PokemonNotFoundException;
import org.example.pokemonapi.model.Pokemon;
import org.example.pokemonapi.service.PokemonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PokemonServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private PokemonService pokemonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pokemonService = new PokemonService(restTemplate);
    }

    @Test
    void testGetPokemon_success() {
        PokemonService.PokemonResponse mockResponse = new PokemonService.PokemonResponse("pikachu", 60, 40, 112);
        when(restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon/pikachu", PokemonService.PokemonResponse.class))
                .thenReturn(mockResponse);

        Pokemon result = pokemonService.getPokemon("pikachu");

        assertNotNull(result);
        assertEquals("pikachu", result.getName());
        assertEquals(60, result.getWeight());
        assertEquals(40, result.getHeight());
        assertEquals(112, result.getBaseExperience());
    }

    @Test
    void testGetPokemon_notFound() {
        when(restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon/alea_pokemon", PokemonService.PokemonResponse.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(PokemonNotFoundException.class, () -> pokemonService.getPokemon("alea_pokemon"));
    }

    @Test
    void testGetAllPokemons_success() {
        List<PokemonService.PokemonResult> results = Arrays.asList(
                new PokemonService.PokemonResult("pikachu", ""),
                new PokemonService.PokemonResult("bulbasaur", "")
        );

        PokemonService.PokemonListResponse listResponse = new PokemonService.PokemonListResponse(results);

        when(restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon/", PokemonService.PokemonListResponse.class))
                .thenReturn(listResponse);

        when(restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon/pikachu", PokemonService.PokemonResponse.class))
                .thenReturn(new PokemonService.PokemonResponse("pikachu", 60, 40, 100));
        when(restTemplate.getForObject("https://pokeapi.co/api/v2/pokemon/bulbasaur", PokemonService.PokemonResponse.class))
                .thenReturn(new PokemonService.PokemonResponse("bulbasaur", 70, 50, 110));

        List<Pokemon> pokemons = pokemonService.getAllPokemons();

        assertEquals(2, pokemons.size());
        assertTrue(pokemons.stream().anyMatch(p -> p.getName().equals("pikachu")));
    }

    @Test
    void testGetHeaviestPokemons() {
        Pokemon p1 = new Pokemon("poke1", 150, 10, 50);
        Pokemon p2 = new Pokemon("poke2", 200, 20, 40);
        Pokemon p3 = new Pokemon("poke3", 180, 25, 60);

        PokemonService spyService = Mockito.spy(pokemonService);
        doReturn(List.of(p1, p2, p3)).when(spyService).getAllPokemons();

        List<Pokemon> result = spyService.getHeaviestPokemons(2);

        assertEquals(2, result.size());
        assertEquals("poke2", result.get(0).getName());
        assertEquals("poke3", result.get(1).getName());
    }

    @Test
    void testGetHighestPokemons() {
        Pokemon p1 = new Pokemon("poke1", 150, 10, 50);
        Pokemon p2 = new Pokemon("poke2", 200, 20, 40);
        Pokemon p3 = new Pokemon("poke3", 180, 25, 60);

        PokemonService spyService = Mockito.spy(pokemonService);
        doReturn(List.of(p1, p2, p3)).when(spyService).getAllPokemons();

        List<Pokemon> result = spyService.getHighestPokemons(1);

        assertEquals(1, result.size());
        assertEquals("poke3", result.get(0).getName());
    }

    @Test
    void testGetPokemonsByBaseExperience() {
        Pokemon p1 = new Pokemon("poke1", 150, 10, 50);
        Pokemon p2 = new Pokemon("poke2", 200, 20, 90);
        Pokemon p3 = new Pokemon("poke3", 180, 25, 70);

        PokemonService spyService = Mockito.spy(pokemonService);
        doReturn(List.of(p1, p2, p3)).when(spyService).getAllPokemons();

        List<Pokemon> result = spyService.getPokemonsByBaseExperience(2);

        assertEquals(2, result.size());
        assertEquals("poke2", result.get(0).getName());
        assertEquals("poke3", result.get(1).getName());
    }

    @Test
    void getPokemon_ShouldReturnPokemon_WhenValidNameProvided() {
        PokemonService.PokemonResponse mockResponse = new PokemonService.PokemonResponse();
        mockResponse.name = "bulbasaur";
        mockResponse.weight = 69;
        mockResponse.height = 7;
        mockResponse.base_experience = 64;

        when(restTemplate.getForObject(anyString(), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(mockResponse);

        Pokemon result = pokemonService.getPokemon("bulbasaur");

        assertEquals("bulbasaur", result.getName());
        assertEquals(69, result.getWeight());
        assertEquals(7, result.getHeight());
        assertEquals(64, result.getBaseExperience());
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
                new PokemonService.PokemonResult("charizard", ""),
                new PokemonService.PokemonResult("blastoise", ""),
                new PokemonService.PokemonResult("venusaur", ""),
                new PokemonService.PokemonResult("pidgeot", ""),
                new PokemonService.PokemonResult("butterfree", "")
        );

        when(restTemplate.getForObject(anyString(), eq(PokemonService.PokemonListResponse.class)))
                .thenReturn(mockListResponse);

        when(restTemplate.getForObject(contains("charizard"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(createMockResponse("charizard", 905, 17, 267));
        when(restTemplate.getForObject(contains("blastoise"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(createMockResponse("blastoise", 855, 16, 265));
        when(restTemplate.getForObject(contains("venusaur"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(createMockResponse("venusaur", 1000, 20, 263));
        when(restTemplate.getForObject(contains("pidgeot"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(createMockResponse("pidgeot", 395, 15, 216));
        when(restTemplate.getForObject(contains("butterfree"), eq(PokemonService.PokemonResponse.class)))
                .thenReturn(createMockResponse("butterfree", 320, 11, 198));

        List<Pokemon> result = pokemonService.getPokemonsByBaseExperience(5);

        assertEquals(5, result.size());
        assertEquals(267, result.get(0).getBaseExperience());
        assertEquals(198, result.get(4).getBaseExperience());
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