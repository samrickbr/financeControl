package samrick.financeControl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import samrick.financeControl.dto.RelatorioFinanceiroDTO;
import samrick.financeControl.repository.LancamentoRepository;

import java.math.BigDecimal;

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
}
