package Sprint.boot.MovimentacaoFinanceira.mapper;

import Sprint.boot.MovimentacaoFinanceira.dto.RespostaDTO;
import Sprint.boot.MovimentacaoFinanceira.model.Transferencia;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransferenciaMapper {

    public RespostaDTO paraRespostaDTO(Transferencia transferencia){
        return new RespostaDTO(transferencia.getId(), transferencia.getTipo(), transferencia.getNatureza(), transferencia.getSinal(), transferencia.getData(), transferencia.getValor(), transferencia.getCpf(), transferencia.getCartao(), transferencia.getHora(), transferencia.getProprietarioLoja(), transferencia.getNomeLoja());
    }

    public List<RespostaDTO> paraRespostaList(List<Transferencia> transacoes){
        return  transacoes.stream().map(this::paraRespostaDTO).collect(Collectors.toList());
    }
}
