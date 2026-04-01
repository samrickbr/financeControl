package samrick.financeControl.exceptions;

public class RecursoNaoEncontradoException extends RegraNegocioException{
    public RecursoNaoEncontradoException(String recurso, Long id){
        super(recurso + " com ID " + id + " não foi encontrado em nosso sistema.");
    }
}
