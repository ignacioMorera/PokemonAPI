# Pokémon API

## Overview
This application provides Pokemon data from PokeAPI, including:
- Individual Pokémon details (name, weight, height, base experience)
- Top 5 lists (heaviest, tallest, most experienced Pokémon)

## Prerequisites
- Java 17+
- Maven 4.4+
- Internet connection

## Installation
```bash
git clone https://github.com/ignacioMorera/PokemonAPI.git
cd pokemonapi
mvn clean install
mvn spring-boot:run
```

## API Endpoints

## Get Pokemon by Name:

### Endpoint
```http
GET /api/pokemon/{nameOrId}
```

# Example request:
curl -X GET "http://localhost:8080/api/pokemon/charizard" \
-H "Accept: application/json"

# Response:

{"name":"charizard","weight":905,"height":17,"baseExperience":0}

## Top 5 Heaviest Pokemon:

### Endpoint
```http
GET /api/pokemon/heaviest
```

# Example request:
curl -X GET "http://localhost:8080/api/pokemon/heaviest" \
-H "Accept: application/json"

# Response:

[
{"name":"venusaur","weight":1000,"height":20,"baseExperience":0},
{"name":"charizard","weight":905,"height":17,"baseExperience":0},
{"name":"blastoise","weight":855,"height":16,"baseExperience":0},
{"name":"pidgeot","weight":395,"height":15,"baseExperience":0},
{"name":"butterfree","weight":320,"height":11,"baseExperience":0}
]

## Top 5 Tallest Pokemon:

### Endpoint
```http
GET /api/pokemon/highest
```

# Example request:
curl -X GET "http://localhost:8080/api/pokemon/highest" \
-H "Accept: application/json"

# Response:
[
{"name":"venusaur","weight":1000,"height":20,"baseExperience":0},
{"name":"charizard","weight":905,"height":17,"baseExperience":0},
{"name":"blastoise","weight":855,"height":16,"baseExperience":0},
{"name":"pidgeot","weight":395,"height":15,"baseExperience":0},
{"name":"charmeleon","weight":190,"height":11,"baseExperience":0}
]

## Top 5 Most Experienced Pokémon

### Endpoint
```http
GET /api/pokemon/most-experienced
```

# Example request:
curl -X GET "http://localhost:8080/api/pokemon/most-experienced" \
-H "Accept: application/json"

# Response:
[
{"name":"bulbasaur","weight":69,"height":7,"baseExperience":0},
{"name":"ivysaur","weight":130,"height":10,"baseExperience":0},
{"name":"venusaur","weight":1000,"height":20,"baseExperience":0},
{"name":"charmander","weight":85,"height":6,"baseExperience":0},
{"name":"charmeleon","weight":190,"height":11,"baseExperience":0}
]

## Start the application
mvn spring-boot:run