package Sprint.boot.MovimentacaoFinanceira.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record RequisicaoDTO(@NotBlank String tipo, @NotBlank String natureza,@NotBlank String sinal,@NotNull LocalDate data,
                            @NotNull  BigDecimal valor, @NotBlank String cpf, @NotBlank String cartao, @NotNull  LocalTime hora,
                            @NotBlank String proprietarioLoja, @NotBlank String nomeLoja) {
}
