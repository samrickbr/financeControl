package samrick.financeControl.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samrick.financeControl.dto.CategoriaRequestDTO;
import samrick.financeControl.model.Categoria;
import samrick.financeControl.repository.CategoriaRepository;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository repository;

    @GetMapping
    public ResponseEntity<List<Categoria>> listar(){
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Categoria> salvar(@RequestBody @Valid CategoriaRequestDTO dto){
        Categoria novaCategoria = new Categoria();
        novaCategoria.setNomeCategoria(dto.categoria().toUpperCase());

        repository.save(novaCategoria);
        return ResponseEntity.ok(novaCategoria);
    }
}
