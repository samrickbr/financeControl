package samrick.financeControl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import samrick.financeControl.dto.RelatorioCategoriaDTO;
import samrick.financeControl.dto.RelatorioMensalDTO;
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

    @Query("SELECT new samrick.financeControl.dto.RelatorioCategoriaDTO(l.categoria.nomeCategoria, SUM(l.valor)) " +
            "FROM Lancamento l " +
            "WHERE l.usuario.id = :usuarioId " +
            "AND l.tipo = 'DESPESA' " +
            "AND MONTH(l.dataLancamento) = :mes " +
            "AND YEAR(l.dataLancamento) = :ano " +
            "GROUP BY l.categoria.nomeCategoria")
    List<RelatorioCategoriaDTO> buscarGastosPorCategoria(
            @Param("usuarioId") Long usuarioId,
            @Param("mes") int mes,
            @Param("ano") int ano);

    // O Spring gera o SQL automaticamente com UPPER(categoria) = UPPER(?)
    List<Lancamento> findByCategoriaNomeCategoriaIgnoreCaseAndUsuarioId(String categoria, Long usuarioId);

    @Query("SELECT new samrick.financeControl.dto.RelatorioMensalDTO(" +
            "MONTH(l.dataLancamento), " +
            "SUM(CASE WHEN l.tipo = 'RECEITA' THEN l.valor ELSE 0 END), " +
            "SUM(CASE WHEN l.tipo = 'DESPESA' THEN l.valor ELSE 0 END)," +
            "SUM(CASE WHEN l.tipo = 'RECEITA' THEN l.valor ELSE -l.valor END)) " +
            "FROM Lancamento l " +
            "WHERE l.usuario.id = :usuarioId AND YEAR(l.dataLancamento) = :ano " +
            "GROUP BY MONTH(l.dataLancamento) " +
            "ORDER BY MONTH(l.dataLancamento)")
    List<RelatorioMensalDTO> buscarResumoAnual(Long usuarioId, int ano);
}

