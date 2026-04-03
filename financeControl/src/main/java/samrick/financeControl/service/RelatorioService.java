package samrick.financeControl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import samrick.financeControl.dto.*;
import samrick.financeControl.model.Lancamento;
import samrick.financeControl.model.Usuario;
import samrick.financeControl.repository.LancamentoRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RelatorioService {
    @Autowired
    private LancamentoRepository repository;

    //Método que cria o relatório financeiro
    public RelatorioFinanceiroDTO gerarResumoGeral(Long usuarioId, int mes, int ano){
        BigDecimal receitas = repository.somarEntradasPorUsuario(usuarioId, mes, ano);
        BigDecimal despesas = repository.somarSaidasPorUsuario(usuarioId, mes, ano);

        //Trata valores nulos caso o usuário não tenha lançamentos
        receitas = (receitas != null) ? receitas : BigDecimal.ZERO;
        despesas = (despesas != null) ? despesas : BigDecimal.ZERO;

        BigDecimal saldo = receitas.subtract(despesas);

        return new RelatorioFinanceiroDTO(receitas, despesas, saldo);
    }

    public List<RelatorioCategoriaDTO> gerarRelatorioPorCategoria(Long usuarioId, int mes, int ano){
        return repository.buscarGastosPorCategoria(usuarioId, mes, ano);
    }

    public List<Lancamento> findByCategoriaIgnoreCaseAndUsuarioId(String categoria, Long usuarioId){
        if (categoria == null || categoria.isBlank()){
            return List.of();
        }

        return repository.findByCategoriaIgnoreCaseAndUsuarioId(categoria.trim(), usuarioId);
    }

    public  RelatorioAnualCompletoDTO obterResumoAnual(int ano){
        Usuario logado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<RelatorioMensalDTO> meses = repository.buscarResumoAnual(logado.getId(), ano);

        return new RelatorioAnualCompletoDTO(meses);
    }

}
