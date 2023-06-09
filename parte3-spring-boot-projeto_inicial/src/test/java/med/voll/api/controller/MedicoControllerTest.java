package med.voll.api.controller;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters

class MedicoControllerTest {

    @Autowired
    private MockMvc mvc;

    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJason;
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJason;

    @MockBean
    private MedicoRepository repository;
    @Test
    @DisplayName("Deveria devolver codigo http 400 quando as informacoes estao invalidas")
    @WithMockUser
    void cadastrar_cenario1() throws Exception {
        var response = mvc.perform(post("/medicos"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }
    @Test
    @DisplayName("Deveria devolver codigo http 200 quando as informacoes estao validas")
    @WithMockUser
    void cadastrar_cenario2() throws Exception {
        var dadosCadastro = new DadosCadastroMedico(
                "Medico",
                "medico@voll.med",
                "1199996666",
                "70454",
                Especialidade.CARDIOLOGIA,
                dadosEndereco()
        );
        when(repository.save(any())).thenReturn(new Medico(dadosCadastro));


        var response = mvc
                .perform(post("/medicos").contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroMedicoJason.write(dadosCadastro).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var dadosDetalhamento = new DadosDetalhamentoMedico(
                null,
                dadosCadastro.nome(),
                dadosCadastro.email(),
                dadosCadastro.crm(),
                dadosCadastro.telefone(),
                dadosCadastro.especialidade(),
                new Endereco(dadosCadastro.endereco())
        );

        var jsonEsperado = dadosDetalhamentoMedicoJason.write(dadosDetalhamento).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);

    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco(
                "rua xpto",
                "bairro",
                "00000000",
                "Brasilia",
                "DF",
                null,
                null
        );
    }


}