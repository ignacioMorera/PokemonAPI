# Pokemon API

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

## 📂 Project Structure

```
src
├── main
│   ├── java
│   │   └── org.example.pokemonapi
│   │       ├── controller
│   │       │   └── PokemonController.java
│   │       ├── exception
│   │       │   ├── GlobalExceptionHandler.java
│   │       │   └── PokemonNotFoundException.java
│   │       ├── model
│   │       │   └── Pokemon.java
│   │       ├── service
│   │       │   └── PokemonService.java
│   │       └── PokemonApiApplication.java
│   └── resources
└── test
    ├── java
    │   └── org.example.pokemonapi
    │       ├── integration
    │       │   ├── controller
    │       │   │   └── PokemonControllerIntegrationTest.java
    │       │   └── service
    │       │       └── PokemonServiceTest.java
    │       ├── unit
    │       │   ├── controller
    │       │   │   └── PokemonControllerTest.java
    │       │   ├── model
    │       │   │   └── PokemonTest.java
    │       │   └── service
    │       │       └── PokemonServiceTest.java
    │       └── PokemonApiApplicationTests.java

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

## Problems and decisions

- Structured the project following the MVC (Model-View-Controller) pattern by separating concerns into three layers: controller, model, and service. This approach aligns with the SOLID principles.
- Configured Spring Boot annotations such as @RestController, @RequestMapping, and @GetMapping to simplify endpoint creation and improve route readability.
- Attempted to use Lombok to reduce boilerplate code by applying annotations such as @Data, @NoArgsConstructor, and @AllArgsConstructor in the model class, as well as @Getter and @Setter in the service layer. However, encountered integration issues with Lombok in the IDE and build process. As a result, reverted to manually writing constructors and getter/setter methods to ensure stability and compatibility.
- Implemented caching using the @Cacheable annotation on service methods to prevent repeated calls to the external PokeAPI endpoint for the same Pokemon.
- Integrated with an external REST API (PokeAPI) to retrieve Pokémon data based on name or ID. Designed a dedicated service layer method using RestTemplate to encapsulate this logic and keep the controller lean and focused on HTTP concerns.
- Handled external API failures by catching RestClientException and wrapping it in a custom PokemonNotFoundException to improve error readability and facilitate potential global exception handling via @ControllerAdvice.
- Manually tested endpoints using tools like Postman and cURL to verify response data for various edge cases.
