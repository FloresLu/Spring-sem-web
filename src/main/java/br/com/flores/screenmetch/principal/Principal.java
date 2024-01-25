package br.com.flores.screenmetch.principal;

import br.com.flores.screenmetch.model.DadosEpsodio;
import br.com.flores.screenmetch.model.DadosSerie;
import br.com.flores.screenmetch.model.DadosTemporada;
import br.com.flores.screenmetch.model.Epsodio;
import br.com.flores.screenmetch.service.ConsumoApi;
import br.com.flores.screenmetch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

        List<DadosEpsodio> dadosEpsodios = temporadas.stream()
                .flatMap(t -> t.epsodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 epsódios");
        dadosEpsodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpsodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Epsodio> epsodios = temporadas.stream()
                .flatMap(t -> t.epsodios().stream()
                        .map(d -> new Epsodio(t.numero(),d ))
                ).collect(Collectors.toList());

        epsodios.forEach(System.out::println);

        System.out.println("Digita o nome do epsódio: ");

        var trechoTitulo = leitura.nextLine();
        Optional<Epsodio> episodioBuscado = epsodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()){
            System.out.println("Episódio encontrado");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        }else {
            System.out.println("Episódio não encontrado");
        }

        /*System.out.println("A partir de qual ano você deseja ver os epsódios? ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyy");

        epsodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Epsódio: " + e.getTitulo() +
                                " Data Lançamento: " +e.getDataLancamento().format(formatador)
                ));*/

        Map<Integer, Double> avaliacoesPorTemporada = epsodios.stream()
                .filter(e ->e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Epsodio::getTemporada,
                        Collectors.averagingDouble(Epsodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = epsodios.stream()
                .filter(e -> e.getAvaliacao()>0)
                .collect(Collectors.summarizingDouble(Epsodio::getAvaliacao));
        System.out.println("Média: " + est.getAverage() +
                "Melhor episódio: " + est.getMax() +
                "Pior episódio: " + est.getMin() +
                "Quandidade de episódios: " + est.getCount());
    }

}
