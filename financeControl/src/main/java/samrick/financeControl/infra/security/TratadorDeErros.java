package samrick.financeControl.infra.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import samrick.financeControl.dto.DadosErroValidacao;
import samrick.financeControl.exceptions.EmailJaCadastradoException;
import samrick.financeControl.exceptions.EntidadeEmUsoException;
import samrick.financeControl.exceptions.RecursoNaoEncontradoException;
import samrick.financeControl.exceptions.RegraNegocioException;

@RestControllerAdvice
public class TratadorDeErros {

    //Erro 404 - Quando não encontra algo no banco
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity tratarErro404(){
        return ResponseEntity.notFound().build();
    }

    //Erro 400 - Falha de validação (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex){
        var erros = ex.getFieldErrors().stream().map(DadosErroValidacao::new).toList();
        return ResponseEntity.badRequest().body(erros);
    }

    //Erro 404 - Regras de negócio específicas (exceptions personalizadas)
    @ExceptionHandler({EmailJaCadastradoException.class, EntidadeEmUsoException.class, RegraNegocioException.class})
    public ResponseEntity tratarErroRegraDeNegocio(RuntimeException ex){
        return ResponseEntity.badRequest().body(new DadosErroDetalhado(ex.getMessage()));
    }

    //DTO interno para mensagem simples
    private record DadosErroDetalhado(String mensagem){}
}
