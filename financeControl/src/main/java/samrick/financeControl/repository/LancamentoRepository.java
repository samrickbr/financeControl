package samrick.financeControl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import samrick.financeControl.model.Lancamento;

import java.util.List;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
    boolean existsByUsuarioId(Long usuarioId);

    List<Lancamento> findByUsuarioId(Long usuarioId);
}
