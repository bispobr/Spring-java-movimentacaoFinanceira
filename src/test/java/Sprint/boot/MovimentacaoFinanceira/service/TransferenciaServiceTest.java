package Sprint.boot.MovimentacaoFinanceira.service;

import Sprint.boot.MovimentacaoFinanceira.dto.RequisicaoDTO;
import Sprint.boot.MovimentacaoFinanceira.dto.RespostaDTO;
import Sprint.boot.MovimentacaoFinanceira.mapper.TransferenciaMapper;
import Sprint.boot.MovimentacaoFinanceira.model.Transferencia;
import Sprint.boot.MovimentacaoFinanceira.repository.TransferenciaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferenciaServiceTest {

    @Mock
    private TransferenciaRepository transferenciaRepository;

    @Mock
    private TransferenciaMapper transferenciaMapper;

    @Autowired
    @InjectMocks
    private TransferenciaService transferenciaService;

    private String conteudoValido;
    private RespostaDTO respostaDTO;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        conteudoValido =
                "3201903010000014200096206760174753****3153153453JOÃO MACEDO   BAR DO JOÃO       \n" +
                        "5201903010000013200556418150633123****7687145607MARIA JOSEFINALOJA DO Ó - MATRIZ\n";

        respostaDTO = new RespostaDTO(1L, "Débito", "Entrada", "+", LocalDate.now(),
                new BigDecimal("123.45"), "12345678901", "1234****5678",
                LocalTime.now(), "João", "Loja do João");

    }

    @Test
    void processarArquivo_arquivoValido_transacoesPersistidas() throws Exception {
        MockMultipartFile arquivo = new MockMultipartFile(
                "file", "cnab.txt", "text/plain", conteudoValido.getBytes()
        );

        transferenciaService.processarArquivo(arquivo);

        verify(transferenciaRepository, atLeastOnce()).save(any(Transferencia.class));
    }

    @Test
    void processarArquivo_arquivoInvalido_excecaoCapturada() throws Exception {
        MultipartFile arquivo = mock(MultipartFile.class);
        when(arquivo.getInputStream()).thenThrow(new RuntimeException("Falha ao ler"));

        assertDoesNotThrow(() -> transferenciaService.processarArquivo(arquivo));
    }

    @Test
    void listarTransacao_transacoesExistem_listaRetornada() {
        when(transferenciaRepository.findAll()).thenReturn(List.of(new Transferencia()));
        when(transferenciaMapper.paraRespostaList(anyList())).thenReturn(List.of(respostaDTO));

        List<RespostaDTO> result = transferenciaService.listarTransacao();

        assertEquals(1, result.size());
        verify(transferenciaRepository).findAll();
    }

    @Test
    void listarporid_idExistente_transacaoRetornada() {
        Transferencia t = new Transferencia();
        when(transferenciaRepository.findById(1L)).thenReturn(Optional.of(t));
        when(transferenciaMapper.paraRespostaDTO(t)).thenReturn(respostaDTO);

        RespostaDTO response = transferenciaService.listarporid(1L);

        assertNotNull(response);
        verify(transferenciaRepository).findById(1L);
    }

    @Test
    void listarporid_idInexistente_excecaoLancada() {
        when(transferenciaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transferenciaService.listarporid(1L));
    }

    @Test
    void removerPorid_idExistente_transacaoRemovida() {
        Transferencia t = new Transferencia();
        when(transferenciaRepository.findById(1L)).thenReturn(Optional.of(t));

        transferenciaService.removerPorid(1L);

        verify(transferenciaRepository).delete(t);
    }

    @Test
    void removerPorid_idInexistente_excecaoLancada() {
        when(transferenciaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transferenciaService.removerPorid(1L));
    }

    @Test
    void removerTodosDados_transacoesExistem_tudoRemovido() {
        transferenciaService.removerTodosDados();
        verify(transferenciaRepository).deleteAll();
    }

    @Test
    void atualizarTransacao_idExistente_dadosAtualizados() {
        Transferencia transferencia = new Transferencia();
        when(transferenciaRepository.findById(1L)).thenReturn(Optional.of(transferencia));
        when(transferenciaRepository.saveAndFlush(any(Transferencia.class))).thenReturn(transferencia);
        when(transferenciaMapper.paraRespostaDTO(transferencia)).thenReturn(respostaDTO);

        RequisicaoDTO dto = new RequisicaoDTO(
                "Débito", "Entrada", "+", LocalDate.now(),
                new BigDecimal("123.45"), "12345678901", "1234****5678",
                LocalTime.now(), "João", "Loja do João"
        );

        RespostaDTO result = transferenciaService.atualizarTransacao(1L, dto);

        assertNotNull(result);
        verify(transferenciaRepository).saveAndFlush(transferencia);
    }

    @Test
    void atualizarTransacao_idInexistente_excecaoLancada() {
        when(transferenciaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> transferenciaService.atualizarTransacao(1L, mock(RequisicaoDTO.class)));
    }


}