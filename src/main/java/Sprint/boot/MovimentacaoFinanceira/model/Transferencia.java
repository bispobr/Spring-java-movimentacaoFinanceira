package Sprint.boot.MovimentacaoFinanceira.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@Entity
public class Transferencia {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long Id;

    private String tipo;
    private String natureza;
    private String sinal;
    private LocalDate data;
    private BigDecimal valor;
    private String cpf;
    private String cartao;
    private LocalTime hora;
    private String proprietarioLoja;
    private String nomeLoja;
}
