package samrick.financeControl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samrick.financeControl.model.Lancamento;
import samrick.financeControl.service.LancamentoService;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

    @Autowired
    private LancamentoService service;

    @PostMapping
    public ResponseEntity<Lancamento> salvarLancamentos(@RequestBody Lancamento lancamento){
        Lancamento novoLancamento = service.salvar(lancamento);
        return new ResponseEntity<>(novoLancamento, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lancamento> buscarLancamento(@PathVariable Long id){
        Lancamento lancamentoEncontrado = service.buscarPorId(id);
        return ResponseEntity.ok(lancamentoEncontrado);
    }
}
