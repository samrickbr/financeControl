package samrick.financeControl.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import samrick.financeControl.dto.ExclusaoRequestDTO;
import samrick.financeControl.dto.LancamentoRequestDTO;
import samrick.financeControl.dto.LancamentoResponseDTO;
import samrick.financeControl.dto.LancamentoUpdateDTO;
import samrick.financeControl.model.Lancamento;
import samrick.financeControl.model.TipoLancamento;
import samrick.financeControl.model.Usuario;
import samrick.financeControl.service.LancamentoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {
    @Autowired
    private LancamentoService service;

    @PostMapping
    public ResponseEntity<LancamentoResponseDTO> salvarLancamentos(
            @Valid @RequestBody LancamentoRequestDTO dados,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        LancamentoResponseDTO response = service.salvar(dados, usuarioLogado);

        // Retornar 201 Created com a URI do novo recurso
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LancamentoResponseDTO>> listarTodos() {
        List<LancamentoResponseDTO> lista = service.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lancamento> buscarLancamento(@PathVariable Long id) {
        Lancamento lancamentoEncontrado = service.buscarPorId(id);
        return ResponseEntity.ok(lancamentoEncontrado);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<LancamentoResponseDTO> lancamentos = service.listarPorUsuario(usuarioId);

        Map<String, Object> resposta = new HashMap<>();

        if (lancamentos.isEmpty()) {
            resposta.put("mensagem", "Este usuário ainda não possui nenhum lançamento cadastrado!");
        } else {
            resposta.put("mensagem", "Lançamentos encontrados com sucesso!");
        }
        resposta.put("totalRegistros", lancamentos.size());
        resposta.put("dados", lancamentos);

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid LancamentoUpdateDTO dto,
                                                         @AuthenticationPrincipal Usuario usuarioLogado) {
        LancamentoResponseDTO atualizado = service.atualizar(id, dto, usuarioLogado);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mesnagem", "Lançamento ID " + id + " atualizado com sucesso!");
        resposta.put("mensagem", atualizado);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> excluir(@PathVariable Long id,
                                                       @Valid @RequestBody ExclusaoRequestDTO request,
                                                       @AuthenticationPrincipal Usuario usuarioLogado) {
        service.excluir(id, request.justificativa(), usuarioLogado);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("messagem", "Lançamento ID " + id + " excluído com sucesso!");
        return ResponseEntity.ok(resposta);
    }

    /*-----------------------------------------------------------------*/

    @GetMapping("/tipos")
    public ResponseEntity<List<String>> listarTiposLancamentos(){
        List<String> tipos = Stream.of(TipoLancamento.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(tipos);
    }
}