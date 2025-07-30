package br.com.alura.screenmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		System.out.println("Hello world");
//		var consumoAPI = new ConsumoAPI();
//		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=24b62a63");
//
//		ConverteDados conversor = new ConverteDados();
//		var resposta = conversor.obterDados(json, ObterDados.class);
//		System.out.println(resposta);
//		json = consumoAPI.obterDados("https://omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=24b62a63");
//		DadosEpisodio dadosEpisodio = conversor.obterDados(json,DadosEpisodio.class);
//		System.out.println(dadosEpisodio);
//
//		List<DadosTemporada> temporadas = new ArrayList<>();
//
//		for (int i = 1;i<=resposta.totalTemporadas();i++){
//			json = consumoAPI.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=24b62a63");
//			DadosTemporada dadosTemporada = conversor.obterDados(json,DadosTemporada.class);
//			temporadas.add(dadosTemporada);
//		}
//		temporadas.forEach(System.out::println);
		Principal principal = new Principal();
		principal.exibMenu();
	}
}
