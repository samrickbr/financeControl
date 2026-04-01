package samrick.financeControl.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samrick.financeControl.dto.ExclusaoRequestDTO;
import samrick.financeControl.dto.LancamentoRequestDTO;
import samrick.financeControl.dto.LancamentoResponseDTO;
import samrick.financeControl.dto.LancamentoUpdateDTO;
import samrick.financeControl.model.Lancamento;
import samrick.financeControl.service.LancamentoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {
    @Autowired
    private LancamentoService service;
    @PostMapping
    public ResponseEntity<LancamentoResponseDTO> salvarLancamentos(@Valid @RequestBody LancamentoRequestDTO dados) {
        LancamentoResponseDTO response = service.salvar(dados);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<LancamentoResponseDTO>> listarTodos(){
        return ResponseEntity.ok(service.listarTodos());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Lancamento> buscarLancamento(@PathVariable Long id) {
        Lancamento lancamentoEncontrado = service.buscarPorId(id);
        return ResponseEntity.ok(lancamentoEncontrado);
    }
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<LancamentoResponseDTO> lancamentos =service.listarPorUsuario(usuarioId);

        Map<String, Object> resposta = new HashMap<>();

        if (lancamentos.isEmpty()){
            resposta.put("mensagem", "Este usuário ainda não possui nenhum lançamento cadastrado!");
        } else {
            resposta.put("mensagem", "Lançamentos encontrados com sucesso!");
        }
        resposta.put("totalRegistros", lancamentos.size());
        resposta.put("dados", lancamentos);

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(@PathVariable Long id, @RequestBody @Valid LancamentoUpdateDTO dto){
        LancamentoResponseDTO atualizado = service.atualizar(id, dto);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mesnagem", "Lançamento ID " + id + " atualizado com sucesso!");
        resposta.put("mensagem", atualizado);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> excluir(@PathVariable Long id, @Valid @RequestBody ExclusaoRequestDTO request){
        service.excluir(id, request.justificativa());

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("messagem", "Lançamento ID " + id + " excluído com sucesso!");
        return ResponseEntity.ok(resposta);
    }
}
