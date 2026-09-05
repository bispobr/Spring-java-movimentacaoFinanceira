package Sprint.boot.MovimentacaoFinanceira.dto;



import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record RespostaDTO(long id,  String tipo, String natureza,  String sinal,  LocalDate data,
                          BigDecimal valor,  String cpf,  String cartao,  LocalTime hora,
                           String proprietarioLoja,  String nomeLoja) {
}
