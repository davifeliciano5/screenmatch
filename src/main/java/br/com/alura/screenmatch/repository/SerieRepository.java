package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
   Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);
    List<Serie> findTop5ByOrderByAvaliacaoDesc();
    List<Serie> findByGenero(Categoria categoria);
    List<Serie> findBytotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int Temporada, Double avaliacao);

    // JPQL JAVA PERSISTENCE QUERY LANGUAGE
    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> serieFiltradas(int totalTemporadas, double avaliacao);
   @Query("SELECT e FROM Serie s JOIN s.episodio e WHERE e.titulo ILIKE %:treco%")
    List<Episodio> episodiosPorTrecho(String treco);
}
