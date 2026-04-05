package samrick.financeControl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import samrick.financeControl.model.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Usuario> findByNome(String nome);

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    UserDetails findByEmail(String email);
}
