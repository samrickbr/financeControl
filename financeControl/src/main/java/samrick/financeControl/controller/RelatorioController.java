package samrick.financeControl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import samrick.financeControl.dto.RelatorioFinanceiroDTO;
import samrick.financeControl.model.Usuario;
import samrick.financeControl.service.LancamentoService;
import samrick.financeControl.service.RelatorioService;

@RestController
@RequestMapping("/relatorios")
@Tag(name = "Realtórios", description = "Endpoints para geração de resumos e estatísticas financeiras.")
public class RelatorioController {
    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/mensal")
    @Operation(summary = "Exibe o total mensal de entradas, saidas e o saldo atual")
    public ResponseEntity<RelatorioFinanceiroDTO> obterResumoMensal(
            @RequestParam int mes,
            @RequestParam int ano){
        Usuario logado = (Usuario) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        var relatorio = relatorioService.gerarResumoGeral(logado.getId(), mes, ano);
        return ResponseEntity.ok(relatorio);
    }
}
