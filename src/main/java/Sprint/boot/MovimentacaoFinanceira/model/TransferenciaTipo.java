package Sprint.boot.MovimentacaoFinanceira.model;

import lombok.Getter;


@Getter
public enum TransferenciaTipo {
    DEBITO(1,"débito","Entrada","+"),
    BOLETO(2,"Boleto","Saída","-"),
    FINANCIAMENTO(3,"Financiamento","Saída","-"),
    CREDITO(4,"Crédito","Entrada","+"),
    RECEBIMENTO_EMPRESTIMO(5,"Recebimento Emprestimo","Entrada","+"),
    VENDAS(6,"vendas","entrada","+"),
    RECEBIMENTO_TED(7,"Recebimento ted","entrada","+"),
    RECEBIMENTO_DOC(8,"Recebimento doc","entrada","+"),
    ALUGUEL(9,"Aluguel","Saída","-");

    private final int codigo;
    private final String descricao;
    private final String natureza;
    private final String sinal;

    TransferenciaTipo(int codigo, String descricao, String natureza, String sinal) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.natureza = natureza;
        this.sinal = sinal;
    }

    public static TransferenciaTipo fromCodigo(int codigo) throws IllegalAccessException {
        for(TransferenciaTipo tipo: TransferenciaTipo.values()){
            if (tipo.codigo == codigo){
                return tipo;
            }
        }
        throw new IllegalAccessException("Transação Invalida: " + codigo);
    }
}
