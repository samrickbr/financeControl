package samrick.financeControl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samrick.financeControl.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional <Categoria> findBynomeCategoriaIgnoreCase(String categoria);

    List<Categoria> findAllByAtivoTrue();

}
