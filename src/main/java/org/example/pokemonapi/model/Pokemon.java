package org.example.pokemonapi.model;

public class Pokemon {
    private String name;
    private int weight;
    private int height;
    private int baseExperience;

    public Pokemon() {}

    public Pokemon(String name, int weight, int height, int baseExperience) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.baseExperience = baseExperience;
    }

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
        return baseExperience;
    }

    public void setBaseExperience(int baseExperience) {
        this.baseExperience = baseExperience;
    }

    @Override
    public String toString() {
        return String.format(
                "Name: %s%nWeight: %.1f kg%nHeight: %.1f m%nBase Experience: %d",
                name,
                weight,
                height,
                baseExperience
        );
    }
}