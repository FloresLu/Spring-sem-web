package br.com.flores.screenmetch.principal;

import br.com.flores.screenmetch.model.DadosEpsodio;
import br.com.flores.screenmetch.model.DadosSerie;
import br.com.flores.screenmetch.model.DadosTemporada;
import br.com.flores.screenmetch.service.ConsumoApi;
import br.com.flores.screenmetch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private Scanner leitura = new Scanner (System.in);

    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=d072e4fd";
    public void exibeMenu (){
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i=1; i<= dados.totalDeTemporadas(); i++){
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+")+"&Season="+ i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(System.out::println);
        temporadas.forEach(t -> t.epsodios().forEach(e -> System.out.println(e.titulo())));
    }

}
