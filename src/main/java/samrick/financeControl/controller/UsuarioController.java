package samrick.financeControl.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import samrick.financeControl.dto.ExclusaoRequestDTO;
import samrick.financeControl.dto.UsuarioRequestDTO;
import samrick.financeControl.dto.UsuarioResponseDTO;
import samrick.financeControl.dto.UsuarioUpdateDTO;
import samrick.financeControl.model.PerfilUsuario;
import samrick.financeControl.model.TipoVinculo;
import samrick.financeControl.model.Usuario;
import samrick.financeControl.service.UsuarioService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvar(@Valid @RequestBody UsuarioRequestDTO dados,
                                                     @AuthenticationPrincipal Usuario usuarioLogado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dados, usuarioLogado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuario(@PathVariable Long id) {
        UsuarioResponseDTO usuarioEncontrado = service.buscarPorId(id);
        return ResponseEntity.ok(usuarioEncontrado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> excluir(@PathVariable Long id,
                                                       @RequestBody @Valid ExclusaoRequestDTO request,
                                                       @AuthenticationPrincipal Usuario usuarioLogado){
        service.excluir(id, request.justificativa(), usuarioLogado);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Usuário ID " + id + " excluído com sucesso!");
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/busca")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<UsuarioResponseDTO> usuarios = service.buscarPorNome(nome);
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(@PathVariable Long id,
                                                         @RequestBody @Valid UsuarioUpdateDTO dados,
                                                         @AuthenticationPrincipal Usuario usuarioLogado) {
        // 1. Chama o service para atualizar e pegar os dados novos
        UsuarioResponseDTO usuarioAtualizado = service.atualizar(id, dados, usuarioLogado);

        // 2. Cria o "pacote" de resposta
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "O Usuário ID " + id + " foi atualizado com sucesso! ✅");
        resposta.put("dados", usuarioAtualizado);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/tipos-vinculo")
    public  ResponseEntity<List<String>> listarTiposVinculo(){
        List<String> tipos = Stream.of(TipoVinculo.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/perfis")
    public ResponseEntity<List<String>> listarPerfis(){
        List<String> perfis = Stream.of(PerfilUsuario.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(perfis);
    }
}
