package Sprint.boot.MovimentacaoFinanceira.controller;

import Sprint.boot.MovimentacaoFinanceira.dto.RequisicaoDTO;
import Sprint.boot.MovimentacaoFinanceira.dto.RespostaDTO;
import Sprint.boot.MovimentacaoFinanceira.service.TransferenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class TransferenciaControllerTest {

    @Mock
    private TransferenciaService transferenciaService;


    @InjectMocks
    private TransferenciaController transferenciaController;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(transferenciaController).build();
    }

    @Test
    void uploadFile_arquivoValido_ProcessamentoBemSucedido() throws Exception {
        MockMultipartFile file = new MockMultipartFile("arquivo", "arquivo.txt", "text/plain",
                "3201903010000014200096206760174753****3153153453JOÃO MACEDO   BAR DO JOÃO".getBytes());

        mockMvc.perform(multipart("/transferencia/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Processamento do arquivo Bem Sucedido"));

        verify(transferenciaService, times(1)).processarArquivo(any(MultipartFile.class));
    }

    @Test
    void listarById_idExistente_TransacaoRetornada() throws Exception {
        RespostaDTO dto = new RespostaDTO(1L,"Débito", "Entrada", "+", LocalDate.now(), new BigDecimal("10.00"),
                "12345678900", "1234****5678", LocalTime.NOON, "João", "Bar do João");
        when(transferenciaService.listarporid(1L)).thenReturn(dto);

        mockMvc.perform(get("/transferencia/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    @Test
    void listarTransacao_transacoesExistem_ListaRetornada() throws Exception {
        List<RespostaDTO> lista = List.of(
                new RespostaDTO(1L,"Crédito", "Entrada", "+", LocalDate.now(), new BigDecimal("100.00"),
                        "99999999999", "0000****0000", LocalTime.NOON, "Maria", "Loja X"));
        when(transferenciaService.listarTransacao()).thenReturn(lista);

        mockMvc.perform(get("/transferencia/listagem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeLoja").value("Loja X"));
    }

    @Test
    void atualizarProduto_idExistente_DadosAtualizados() throws Exception {
        RequisicaoDTO dto = new RequisicaoDTO("Crédito", "Entrada", "+", LocalDate.now(), new BigDecimal("200.00"),
                "88888888888", "1111****1111", LocalTime.NOON, "Carlos", "Mercado X");

        RespostaDTO resposta = new RespostaDTO(1L,dto.tipo(), dto.natureza(), dto.sinal(), dto.data(),
                dto.valor(), dto.cpf(), dto.cartao(), dto.hora(), dto.proprietarioLoja(), dto.nomeLoja());

        when(transferenciaService.atualizarTransacao(eq(1L), any(RequisicaoDTO.class))).thenReturn(resposta);

        mockMvc.perform(put("/transferencia/atualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeLoja").value("Mercado X"));
    }



    @Test
    void atualizarProduto_idInexistente_RetornarNotFound() throws Exception {
        RequisicaoDTO dto = new RequisicaoDTO("Débito", "Saída", "-", LocalDate.now(), new BigDecimal("30.00"),
                "00000000000", "0000****0000", LocalTime.MIDNIGHT, "Fulano", "Loja Y");



        when(transferenciaService.atualizarTransacao(eq(999L), any(RequisicaoDTO.class)))
                .thenThrow(new EntityNotFoundException());

        assertThrows(ServletException.class, () -> {
            mockMvc.perform(put("/transferencia/atualizar/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(dto)))
                    .andExpect(status().isNotFound());

        });


    }

    @Test
    void removerTodosDados_baseContendoDados_RemocaoConcluida() throws Exception {
        mockMvc.perform(delete("/transferencia/removerTodos"))
                .andExpect(status().isNoContent());

        verify(transferenciaService, times(1)).removerTodosDados();
    }

    @Test
    void deletarProduto_idExistente_RemocaoBemSucedida() throws Exception {
        mockMvc.perform(delete("/transferencia/remover/1"))
                .andExpect(status().isNoContent());

        verify(transferenciaService).removerPorid(1L);
    }

    @Test
    void deletarProduto_idInexistente_RetornarNotFound() throws Exception {
        doThrow(new EntityNotFoundException()).when(transferenciaService).removerPorid(999L);

        assertThrows(ServletException.class, () -> {
            mockMvc.perform(delete("/transferencia/remover/999"))
                    .andExpect(status().isNotFound());

        });



    }




}