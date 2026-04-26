package samrick.financeControl.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import samrick.financeControl.audit.LogAuditoriaService;
import samrick.financeControl.dto.CategoriaRequestDTO;
import samrick.financeControl.dto.CategoriaUpdateDTO;
import samrick.financeControl.model.Categoria;
import samrick.financeControl.model.Usuario;
import samrick.financeControl.repository.CategoriaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository repository;
    @Autowired
    private LogAuditoriaService logService;

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(repository.findAllByAtivoTrue());
    }

    @PostMapping
    public ResponseEntity<Categoria> salvar(@RequestBody @Valid CategoriaRequestDTO dto) {
        String nomeUpper = dto.categoria().toUpperCase();

        // 1. Procura se a categoria já existe (mesmo que desativada)
        // Usamos o método que você já tem no Repository
        Optional<Categoria> categoriaExistente = repository.findBynomeCategoriaIgnoreCase(nomeUpper);

        if (categoriaExistente.isPresent()) {
            Categoria cat = categoriaExistente.get();
            if (!cat.isAtivo()) {
                cat.setAtivo(true); // Reativa se estiver desativada
                repository.save(cat);
                return ResponseEntity.ok(cat);
            }
            throw new RuntimeException("Esta categoria já está ativa!");
        }
        // 2. Se não existir, cria uma nova normalmente
        Categoria novaCategoria = new Categoria();
        novaCategoria.setNomeCategoria(nomeUpper);
        repository.save(novaCategoria);

        return ResponseEntity.ok(novaCategoria);
    }

    @PutMapping("/{id}/desativar")
    @Transactional
    public ResponseEntity<Void> desativar(
            @PathVariable Long id,
            @RequestBody Map<String, String> body, // Recebe a justificativa via JSON
            @AuthenticationPrincipal Usuario usuarioLogado) {

        String justificativa = body.get("justificativa");

        // 1. Busca a categoria
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));

        // 2. Registra o Log de Auditoria da EXCLUSÃO (agora como desativação)
        logService.registrarLog(
                "CATEGORIA",
                id,
                "EXCLUIR",
                categoria.getNomeCategoria(),
                justificativa,
                usuarioLogado.getNome()
        );

        // 3. Aplica o Soft Delete
        categoria.setAtivo(false);
        repository.save(categoria);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid CategoriaUpdateDTO dto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        // 1. Busca a categoria no banco
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        // 2. Registra o Log de Auditoria antes de salvar a alteração
        // Padrão: Tabela, ID do Registro, Ação, Novo Valor, Motivo, Quem fez
        logService.registrarLog(
                "CATEGORIA",
                id,
                "ALTERAR",
                dto.categoria().toUpperCase(),
                dto.justificativa(),
                usuarioLogado.getNome()
        );

        // 3. Atualiza e salva o registro
        categoria.setNomeCategoria(dto.categoria().toUpperCase());
        repository.save(categoria);

        return ResponseEntity.ok(categoria);
    }
}