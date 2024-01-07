package br.com.flores.screenmetch;

import br.com.flores.screenmetch.model.DadosSerie;
import br.com.flores.screenmetch.service.ConsumoApi;
import br.com.flores.screenmetch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmetchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmetchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoApi consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("http://www.omdbapi.com/?t=lost&apikey=d072e4fd");

		System.out.println(json);

		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
