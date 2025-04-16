package org.example.pokemonapi.unit.model;

import org.example.pokemonapi.model.Pokemon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {

    @Test
    void testDefaultConstructor() {
        Pokemon pokemon = new Pokemon();

        assertNull(pokemon.getName());
        assertEquals(0, pokemon.getWeight());
        assertEquals(0, pokemon.getHeight());
        assertEquals(0, pokemon.getBaseExperience());
    }

    @Test
    void testParameterizedConstructor() {
        Pokemon pokemon = new Pokemon("charmander", 85, 5, 0);

        assertEquals("charmander", pokemon.getName());
        assertEquals(85, pokemon.getWeight());
        assertEquals(5, pokemon.getHeight());
        assertEquals(0, pokemon.getBaseExperience());
    }

    @Test
    void testSettersAndGetters() {
        Pokemon pokemon = new Pokemon();

        pokemon.setName("venusaur");
        pokemon.setWeight(1000);
        pokemon.setHeight(20);
        pokemon.setBaseExperience(0);

        assertEquals("venusaur", pokemon.getName());
        assertEquals(1000, pokemon.getWeight());
        assertEquals(20, pokemon.getHeight());
        assertEquals(0, pokemon.getBaseExperience());
    }

    @Test
    void testToString() {
        Pokemon pokemon = new Pokemon("bulbasaur", 69, 7, 0);
        String expected = "Name: bulbasaur\nWeight: 69\nHeight: 7\nBase Experience: 0";

        assertEquals(expected, pokemon.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        Pokemon pokemon1 = new Pokemon("ivysaur", 130, 10, 0);
        Pokemon pokemon2 = new Pokemon("ivysaur", 130, 10, 0);
        Pokemon pokemon3 = new Pokemon("charmander", 85, 6, 0);

        assertEquals(pokemon1, pokemon2);
        assertNotEquals(pokemon1, pokemon3);
        assertEquals(pokemon1.hashCode(), pokemon2.hashCode());
        assertNotEquals(pokemon1.hashCode(), pokemon3.hashCode());
    }
}