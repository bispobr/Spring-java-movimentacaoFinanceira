package Sprint.boot.MovimentacaoFinanceira.controller;

import Sprint.boot.MovimentacaoFinanceira.dto.RequisicaoDTO;
import Sprint.boot.MovimentacaoFinanceira.dto.RespostaDTO;
import Sprint.boot.MovimentacaoFinanceira.service.TransferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/transferencia")
public class TransferenciaController {

    @Autowired
    TransferenciaService transferenciaService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Endpoint responsável por receber arquivo")
    @ApiResponse(responseCode = "200", description = "Processamento concluído com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de Requisição")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<String> uploadFile(@Parameter(description = "arquivo para upload", required = true) @RequestPart MultipartFile arquivo){
        log.info("Requisição de upload recebida");
        transferenciaService.processarArquivo(arquivo);
        return ResponseEntity.ok("Processamento do arquivo Bem Sucedido") ;
    }

    @GetMapping("/{id}")
    @Operation(description = "Endpoint responsável por listar transações por id")
    @ApiResponse (responseCode = "200", description = "Listagem bem sucedida")
    @ApiResponse(responseCode = "400", description = "Erro de Requisição")
    @ApiResponse(responseCode = "404", description = "não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<RespostaDTO> listarById(@PathVariable("id")Long id){
        log.info("solicitação de busca por id recebida;");
        return ResponseEntity.ok().body(transferenciaService.listarporid(id));
    }

    @GetMapping("/listagem")
    @Operation(description = "Endpoint responsável por listar todas as transações")
    @ApiResponse(responseCode = "200", description = "Listagem concluída")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<List<RespostaDTO>> listarTransacao(){
        log.info("Requisição de listagem recebida");
        return ResponseEntity.ok().body(transferenciaService.listarTransacao()) ;
    }

    @PutMapping("/atualizar/{id}")
    @Operation(description = "Endpoint responsável por atualizar transações")
    @ApiResponse(responseCode = "200", description = "transação atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de Requisição, dados enviados não atendem os requisitos")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<RespostaDTO> atualizarProduto(@PathVariable("id")Long id , @RequestBody RequisicaoDTO data){
        log.info("Solicitação de Atualização de transação recebida");
        return ResponseEntity.ok().body(transferenciaService.atualizarTransacao(id, data));
    }

    @DeleteMapping("/removerTodos")
    @Operation(description = "Endpoint responsável por apagar todas as transações")
    @ApiResponse(responseCode = "204", description = "operação concluída")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<Void> removerTodosDados(){
        log.info("Solicitação de remoção de todos os dados recebia");
        transferenciaService.removerTodosDados();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/remover/{id}")
    @Operation(description = "Endpoint responsável por remover transação identificada pelo id")
    @ApiResponse(responseCode = "204", description = "Produto removido com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de Requisição, dados enviados não atendem os requisitos")
    @ApiResponse(responseCode = "404", description = "transação não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno")
    public ResponseEntity<Void> deletarProduto(@PathVariable("id")Long id){
        log.info("Solicitação de remoção de transação recebida");
        transferenciaService.removerPorid(id);
        return ResponseEntity.noContent().build();
    }


}
