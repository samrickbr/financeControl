package samrick.financeControl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samrick.financeControl.model.Usuario;
import samrick.financeControl.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody Usuario usuario){
        System.out.println("Usuário recebido: " + usuario);
        Usuario novoUsuario = service.salvar(usuario);
        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuario(@PathVariable Long id){
        System.out.println("Buscando usuário: " + id);
        Usuario usuarioEncontrado = service.buscarPorId(id);
        if (usuarioEncontrado == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioEncontrado);
    }

}
