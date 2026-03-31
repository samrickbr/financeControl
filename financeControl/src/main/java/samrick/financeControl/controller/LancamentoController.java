package samrick.financeControl.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samrick.financeControl.dto.LancamentoRequestDTO;
import samrick.financeControl.dto.LancamentoResponseDTO;
import samrick.financeControl.model.Lancamento;
import samrick.financeControl.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

    @Autowired
    private LancamentoService service;

    @PostMapping
    public ResponseEntity<LancamentoResponseDTO> salvarLancamentos(@Valid @RequestBody LancamentoRequestDTO dados) {
        Lancamento novoLancamento = service.salvar(dados);

        LancamentoResponseDTO response = new LancamentoResponseDTO(
                novoLancamento.getId(),
                novoLancamento.getTipo().toString(),
                novoLancamento.getValor(),
                novoLancamento.getDataVencimento(),
                novoLancamento.getDescricao(),
                novoLancamento.getCategoria(),
                novoLancamento.getUsuario().getNome()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lancamento> buscarLancamento(@PathVariable Long id) {
        Lancamento lancamentoEncontrado = service.buscarPorId(id);
        return ResponseEntity.ok(lancamentoEncontrado);
    }
}
