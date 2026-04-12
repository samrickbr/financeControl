package samrick.financeControl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import samrick.financeControl.model.PerfilUsuario;
import samrick.financeControl.model.Usuario;
import samrick.financeControl.repository.UsuarioRepository;

import java.time.LocalDateTime;

@Configuration
public class DatabaseSeeder implements CommandLineRunner {
    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){
        //1- Verifica se já existe algum usuário cadastrado
        if (repository.count() == 0){
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@finance.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setPerfil(PerfilUsuario.ADMIN);
            admin.setProfissao("System Admin");
            admin.setCpf("00000000000");
            admin.setAtivo(true);
            admin.setUsuarioUltimaAlteracao("SISTEMA");
            admin.setDataUltimaAlteracao(LocalDateTime.now());

            repository.save(admin);

            System.out.println("--------------------------------------");
            System.out.println("✅ USUÁRIO ADMIN PADRÃO CRIADO!");
            System.out.println("Login: admin@finance.com");
            System.out.println("Senha: admin123");
            System.out.println("--------------------------------------");
        }
    }
}
