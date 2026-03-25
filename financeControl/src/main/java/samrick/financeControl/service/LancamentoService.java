package samrick.financeControl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import samrick.financeControl.model.Lancamento;
import samrick.financeControl.repository.LancamentoRepository;

import java.util.List;

@Service
public class LancamentoService {
    @Autowired
    private LancamentoRepository repository;

    public Lancamento salvar(Lancamento lancamento) {
        //Lançamento precisa ter data de vencimento
        if (lancamento.getDataVencimento() == null) {
            throw new RuntimeException("Data de vencimento é obrigatória!");
        }
        return repository.save(lancamento);
    }

    public List<Lancamento> listarTodos() {
        return repository.findAll();
    }

    public Lancamento buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }
}
