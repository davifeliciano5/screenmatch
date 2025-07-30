package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//JsonAlias serialização
//JsonProperty serialização e desserialização

/*
* @JsonAlias e @JsonProperty são anotações em Jackson,
* uma biblioteca Java para processar JSON, que ajudam a mapear propriedades
*  de classe para campos JSON.
* */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ObterDados(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao
                            ) {
}
