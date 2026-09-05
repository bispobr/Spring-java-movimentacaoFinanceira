package Sprint.boot.MovimentacaoFinanceira.service;

import Sprint.boot.MovimentacaoFinanceira.dto.RequisicaoDTO;
import Sprint.boot.MovimentacaoFinanceira.dto.RespostaDTO;
import Sprint.boot.MovimentacaoFinanceira.mapper.TransferenciaMapper;
import Sprint.boot.MovimentacaoFinanceira.model.TransferenciaTipo;
import Sprint.boot.MovimentacaoFinanceira.model.Transferencia;
import Sprint.boot.MovimentacaoFinanceira.repository.TransferenciaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class TransferenciaService {

    @Autowired
    TransferenciaRepository transferenciaRepository;

    @Autowired
    TransferenciaMapper mapper;


    public void processarArquivo(MultipartFile arquivo){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))){
            log.info("Processando Arquivo"); String linha;
            while ((linha = br.readLine()) != null){
                Transferencia transferencia = parseLinha(linha);
                transferenciaRepository.save(transferencia);
            }
        } catch (Exception e) {
            log.error("Erro ao processar arquivo");
            e.printStackTrace();
        }
    }

    private Transferencia parseLinha(String linha) throws IllegalAccessException {
        Transferencia transferencia = new Transferencia();
        int tipoCodigo = Integer.parseInt(linha.substring(0,1));
        TransferenciaTipo tipo = TransferenciaTipo.fromCodigo(tipoCodigo);
        transferencia.setTipo(tipo.getDescricao());
        transferencia.setNatureza(tipo.getNatureza());
        transferencia.setSinal(tipo.getSinal());
        transferencia.setData(LocalDate.parse(linha.substring(1,9), DateTimeFormatter.BASIC_ISO_DATE));
        transferencia.setValor(new BigDecimal(linha.substring(9,19)).divide(new BigDecimal(100)));
        transferencia.setCpf(linha.substring(19,30));
        transferencia.setCartao(linha.substring(30,42));
        transferencia.setHora(LocalTime.parse(linha.substring(42,48), DateTimeFormatter.ofPattern("HHmmss")));
        transferencia.setProprietarioLoja(linha.substring(48,62).trim());
        transferencia.setNomeLoja(linha.substring(62).trim());
        log.info("Processamento conluido");
        return transferencia;
    }

    public List<RespostaDTO> listarTransacao(){
        log.info("Listando todas as transações");
        return mapper.paraRespostaList(transferenciaRepository.findAll());
    }

    public RespostaDTO listarporid( Long id){
        log.info("Listando por id");
        return mapper.paraRespostaDTO(transferenciaRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Transactional
    public void removerPorid(Long id){
        log.info("Removendo transação por id");
        transferenciaRepository.delete(transferenciaRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Transactional
    public void removerTodosDados(){
        log.info("Removendo Todos as transações da base de dados");
        transferenciaRepository.deleteAll();
    }

    @Transactional
    public RespostaDTO atualizarTransacao (Long id, RequisicaoDTO dto) {
        Transferencia t = transferenciaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        log.info("Atualizando dados da transação");
        if (dto.tipo() != null) t.setTipo(dto.tipo());
        if (dto.natureza() != null) t.setNatureza(dto.natureza());
        if (dto.sinal() != null) t.setSinal(dto.sinal());
        if (dto.data() != null) t.setData(dto.data());
        if (dto.valor() != null) t.setValor(dto.valor());
        if (dto.cpf() != null) t.setCpf(dto.cpf());
        if (dto.cartao() != null) t.setCartao(dto.cartao());
        if (dto.hora() != null) t.setHora(dto.hora());
        if (dto.proprietarioLoja() != null) t.setProprietarioLoja(dto.proprietarioLoja());
        if (dto.nomeLoja() != null) t.setNomeLoja(dto.nomeLoja());
        return mapper.paraRespostaDTO(transferenciaRepository.saveAndFlush(t));
    }


}
