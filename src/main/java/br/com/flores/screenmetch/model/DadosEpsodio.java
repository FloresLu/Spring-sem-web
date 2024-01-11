package br.com.flores.screenmetch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpsodio(@JsonAlias("Title") String titulo,
                           @JsonAlias("Episode") Integer numeroEpsodio,
                           @JsonAlias("imdbRating") String avaliacao,
                           @JsonAlias("Released") String dataLancamento) {
}
