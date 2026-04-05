package samrick.financeControl.audit;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name= "log_auditoria")
public class LogAuditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entidade; // "USUARIO" ou "LANÇAMENTO"
    private Long entidadeId; // O ID do registro alterado
    private String acao; // "ALTERAR", "EXCLUIR"
    private String usuarioResposavel; // Quem estava logado
    private LocalDateTime dataHora;

    @Column(columnDefinition = "TEXT")
    private String dadosAnteriores; // Json do que era antes

    @Column(columnDefinition = "TEXT")
    private String justificativa; //obrigatório

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntidade() {
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public Long getEntidadeId() {
        return entidadeId;
    }

    public void setEntidadeId(Long entidadeId) {
        this.entidadeId = entidadeId;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getUsuarioResposavel() {
        return usuarioResposavel;
    }

    public void setUsuarioResposavel(String usuarioResposavel) {
        this.usuarioResposavel = usuarioResposavel;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getDadosAnteriores() {
        return dadosAnteriores;
    }

    public void setDadosAnteriores(String dadosAnteriores) {
        this.dadosAnteriores = dadosAnteriores;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}
