package samrick.financeControl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import samrick.financeControl.model.Lancamento;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
    boolean existsByUsuarioId(Long usuarioId);

    List<Lancamento> findByUsuarioId(Long usuarioId);

    @Query("SELECT SUM(l.valor) FROM Lancamento l WHERE l.tipo = 'RECEITA' " +
            "AND l.usuario.id = :usuarioId AND MONTH(l.dataLancamento) = :mes " +
            "AND YEAR(l.dataLancamento) = :ano")
    BigDecimal somarEntradasPorUsuario(Long usuarioId, int mes, int ano);

    @Query("SELECT SUM(l.valor) FROM Lancamento l WHERE l.tipo = 'DESPESA' " +
            "AND l.usuario.id = :usuarioId AND MONTH(l.dataLancamento) = :mes " +
            "AND YEAR(l.dataLancamento) = :ano")
    BigDecimal somarSaidasPorUsuario(Long usuarioId, int mes, int ano);

}
