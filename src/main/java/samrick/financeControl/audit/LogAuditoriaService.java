package samrick.financeControl.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDateTime;

@Service
public class LogAuditoriaService {
    @Autowired
    private LogAuditoriaRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public void registrarLog(String entidade, Long entidadeId, String acao, Object objetoAntigo, String justificativa, String responsavel){
        LogAuditoria log = new LogAuditoria();
        log.setEntidade(entidade);
        log.setEntidadeId(entidadeId);
        log.setAcao(acao);
        log.setUsuarioResposavel(responsavel);
        log.setDataHora(LocalDateTime.now());
        log.setJustificativa(justificativa);

        try {
            String jsonAntigo = objectMapper.writeValueAsString(objetoAntigo);
            log.setDadosAnteriores(jsonAntigo);
        } catch (Exception e){
            log.setDadosAnteriores("Erro ao converter dados: " + e.getMessage());
        }

        repository.save(log);
    }
}
