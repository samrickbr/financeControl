package samrick.financeControl.exceptions;

public class EmailJaCadastradoException extends RegraNegocioException{
    public EmailJaCadastradoException(){
        super("Este e-mail já está cadastrado em nosso sistema.");
    }
}
