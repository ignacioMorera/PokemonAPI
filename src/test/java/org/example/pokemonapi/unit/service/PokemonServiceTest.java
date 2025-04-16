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

    private static class TestablePokemonService extends PokemonService {
        private final RestTemplate testRestTemplate;

        public TestablePokemonService(RestTemplate restTemplate) {
            super(restTemplate);
            this.testRestTemplate = restTemplate;
        }

        protected RestTemplate getRestTemplate() {
            return testRestTemplate;
        }
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
}