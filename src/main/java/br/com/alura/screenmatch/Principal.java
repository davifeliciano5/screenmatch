package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class Principal {
    private Optional<Serie> serieBusca;
    private SerieRepository repository;

    private List<Serie> series = new ArrayList<>();
    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=24b62a63";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    public void exibMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar série
                    2 - Buscar eposódio
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar séries por ator
                    6 - Buscar top 5 séries
                    7 - Buscar séries por categoria
                    8 - Buscar série filtrada por Quantidade de temporada e Avaliação
                    9 - Buscar episódio por trecho
                    10 - Buscar top 5 episódios de uma série
                    11 - Buscar episódios a partir de uma data
                    
                    0 - Sair
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();
            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarSerieQuantiadeTemporadaEaAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTreco();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDeUmaData();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Um erro inesperado aconteceu, reinicie a aplicação e tente novamente");

            }

        }
    }
    private void buscarSerieWeb(){
        DadosSerie dados = getDadosSerie();
        dadosSeries.add(dados);
        Serie serie = new Serie(dados);
        repository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie(){
        System.out.println("Digite o nome da série para buscar");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+")+API_KEY);
        var url = ENDERECO + nomeSerie.replace(" ","+")+API_KEY;
        DadosSerie dados = conversor.obterDados(json,DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serie =  repository.findByTituloContainingIgnoreCase(nomeSerie);

        if(serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++){
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json,DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios =  temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e))
                    ).collect(Collectors.toList());
            serieEncontrada.setEpisodio(episodios);
            repository.save(serieEncontrada);
        } else{
            System.out.println("Série não encontrada!");
        }

    }

    private void listarSeriesBuscadas(){
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo(){
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();
        serieBusca = repository.findByTituloContainingIgnoreCase(nomeSerie);
        if (serieBusca.isPresent()){
            System.out.println("Dados da série: " + serieBusca.get());
        }else {
            System.out.println("Série não encontrada");
        }
    }
    private void buscarSeriePorAtor(){
        System.out.println("Qual o nome do Ator a ser buscado: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de que valor: ");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("O ator participou" + nomeAtor + "das seguintes séries: ");
        seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + "Avaliação: "+s.getAvaliacao()));
    }

    private void buscarTop5Series(){
        List<Serie> serieTop = repository.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s -> System.out.println(s.getTitulo() + " Avaliação: "+ s.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria(){
        System.out.println("Qual categoria/Genero quer buscar: ");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("Séries da categoria " + nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }
    private void buscarSerieQuantiadeTemporadaEaAvaliacao(){
        System.out.println("Qual a quantidade máxima de temporadas: ");
        var quantidade = leitura.nextInt();
        System.out.println("Avaliações a partir de que valor: ");
        var avaliacao = leitura.nextDouble();
        List<Serie> series = repository.serieFiltradas(quantidade,avaliacao);
        series.forEach(System.out::println);
    }
    private void buscarEpisodioPorTreco(){
        System.out.println("Digite um treço do episódio: ");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodios = repository.episodiosPorTrecho(trechoEpisodio);
        episodios.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()) );
    }

    private void topEpisodiosPorSerie(){
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach( e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                    e.getSerie().getTitulo(), e.getTemporada(),
                    e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }
    private void buscarEpisodiosDepoisDeUmaData(){
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento: ");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> eposiodiosAno = repository.buscarEpisodiosDepoisDeUmaData(serie,anoLancamento);
            eposiodiosAno.forEach(System.out::println);
        }
    }
}
