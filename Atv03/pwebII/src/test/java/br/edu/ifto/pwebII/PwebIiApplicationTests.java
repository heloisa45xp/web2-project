package br.edu.ifto.pwebII;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.edu.ifto.pwebII.config.UsuarioDetailsService;
import br.edu.ifto.pwebII.model.entity.Paciente;
import br.edu.ifto.pwebII.model.entity.Pessoa;
import br.edu.ifto.pwebII.model.entity.Usuario;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioPessoa;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioUsuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.MockMvcAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@Import(MockMvcAutoConfiguration.class)
class PwebIiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @Autowired
    private RepositorioUsuario usuarioRepository;

    @Autowired
    private RepositorioPessoa pessoaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Deve carregar os usuarios do banco com seus respectivos papeis")
    void deveCarregarUsuariosDoBancoComPapeis() {
        UserDetails admin = usuarioDetailsService.loadUserByUsername("admin");
        assertThat(admin).isNotNull();
        assertThat(admin.getAuthorities().stream().map(a -> a.getAuthority()).toList())
                .contains("ROLE_ADMIN");

        UserDetails secretario = usuarioDetailsService.loadUserByUsername("secretario");
        assertThat(secretario).isNotNull();
        assertThat(secretario.getAuthorities().stream().map(a -> a.getAuthority()).toList())
                .contains("ROLE_SECRETARIO");

        UserDetails medico = usuarioDetailsService.loadUserByUsername("dr.roberto");
        assertThat(medico).isNotNull();
        assertThat(medico.getAuthorities().stream().map(a -> a.getAuthority()).toList())
                .contains("ROLE_MEDICO");

        UserDetails paciente = usuarioDetailsService.loadUserByUsername("heloisa");
        assertThat(paciente).isNotNull();
        assertThat(paciente.getAuthorities().stream().map(a -> a.getAuthority()).toList())
                .contains("ROLE_PACIENTE");
    }

    @Test
    @DisplayName("Deve autenticar com sucesso usuarios do banco via formLogin")
    void deveAutenticarUsuariosViaFormLogin() throws Exception {
        mockMvc.perform(formLogin().user("admin").password("admin"))
                .andExpect(authenticated().withUsername("admin").withRoles("ADMIN", "USER"));

        mockMvc.perform(formLogin().user("secretario").password("123"))
                .andExpect(authenticated().withUsername("secretario").withRoles("SECRETARIO", "USER"));

        mockMvc.perform(formLogin().user("dr.roberto").password("123"))
                .andExpect(authenticated().withUsername("dr.roberto").withRoles("MEDICO", "USER"));

        mockMvc.perform(formLogin().user("heloisa").password("123"))
                .andExpect(authenticated().withUsername("heloisa").withRoles("PACIENTE", "USER"));
    }

    @Test
    @DisplayName("Deve rejeitar credenciais invalidas")
    void deveRejeitarCredenciaisInvalidas() throws Exception {
        mockMvc.perform(formLogin().user("admin").password("senha_errada"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("Endpoints publicos devem permitir acesso anonimo")
    void endpointsPublicosDevemPermitirAcessoAnonimo() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/paciente/form"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Acesso anonimo a rotas protegidas deve redirecionar para login")
    void acessoAnonimoRotasProtegidasRedirecionaParaLogin() throws Exception {
        mockMvc.perform(get("/paciente/list").accept(org.springframework.http.MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/login")));

        mockMvc.perform(get("/medico/list").accept(org.springframework.http.MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/login")));
    }

    @Test
    @DisplayName("Controle de acesso do perfil PACIENTE: pode ver medicos, mas nao lista de pacientes nem cadastro de medico")
    void controleAcessoPaciente() throws Exception {
        mockMvc.perform(get("/medico/list").with(user("heloisa").roles("PACIENTE")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/paciente/list").with(user("heloisa").roles("PACIENTE")))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/medico/form").with(user("heloisa").roles("PACIENTE")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Controle de acesso do perfil SECRETARIO: pode gerenciar pacientes e consultas, mas nao médicos")
    void controleAcessoSecretario() throws Exception {
        mockMvc.perform(get("/paciente/list").with(user("secretario").roles("SECRETARIO")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/consulta/list").with(user("secretario").roles("SECRETARIO")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/medico/list").with(user("secretario").roles("SECRETARIO")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/medico/form").with(user("secretario").roles("SECRETARIO")))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/paciente/remove/1").with(user("secretario").roles("SECRETARIO")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Controle de acesso do perfil MEDICO: pode ver pacientes, medicos e consultas")
    void controleAcessoMedico() throws Exception {
        mockMvc.perform(get("/paciente/list").with(user("dr.roberto").roles("MEDICO")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/medico/list").with(user("dr.roberto").roles("MEDICO")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/consulta/list").with(user("dr.roberto").roles("MEDICO")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/medico/form").with(user("dr.roberto").roles("MEDICO")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Controle de acesso do perfil ADMIN: acesso completo")
    void controleAcessoAdmin() throws Exception {
        mockMvc.perform(get("/paciente/list").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/medico/list").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/consulta/list").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/medico/form").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Cadastro de paciente deve salvar usuario com papel inicial PACIENTE e senha BCrypt")
    void deveCadastrarPacienteComCredenciaisEPapelInicial() throws Exception {
        mockMvc.perform(post("/paciente/save")
                        .with(csrf())
                        .param("nome", "Carlos Silva")
                        .param("cpf", "987.654.321-00")
                        .param("email", "carlos.silva@exemplo.com")
                        .param("telefone", "(63) 98765-4321")
                        .param("usuario.usuario", "carlossilva")
                        .param("usuario.senha", "senhaSecreta123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?cadastrado=true"));

        Usuario usuarioSalvo = usuarioRepository.buscarPorUsuario("carlossilva");
        assertThat(usuarioSalvo).isNotNull();
        assertThat(passwordEncoder.matches("senhaSecreta123", usuarioSalvo.getSenha())).isTrue();
        assertThat(usuarioSalvo.getPerfis().stream().map(p -> p.getNome()).toList())
                .contains("PACIENTE");

        Pessoa pessoaSalva = pessoaRepository.buscarPorUsuario("carlossilva");
        assertThat(pessoaSalva).isNotNull();
        assertThat(pessoaSalva).isInstanceOf(Paciente.class);
        assertThat(((Paciente) pessoaSalva).getNome()).isEqualTo("Carlos Silva");
    }
}
