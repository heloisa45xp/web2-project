package br.edu.ifto.pwebII.controller;

import static br.edu.ifto.pwebII.config.SecurityUtils.redirectAfterOperation;

import br.edu.ifto.pwebII.config.SecurityUtils;
import br.edu.ifto.pwebII.model.entity.Paciente;
import br.edu.ifto.pwebII.model.entity.Perfil;
import br.edu.ifto.pwebII.model.entity.Pessoa;
import br.edu.ifto.pwebII.model.entity.Usuario;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioConsulta;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioPaciente;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioPerfil;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioPessoa;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioUsuario;
import br.edu.ifto.pwebII.model.validation.Edicao;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("paciente")
public class PacienteController {

    private final RepositorioPaciente repository;
    private final RepositorioConsulta consultaRepository;
    private final RepositorioPessoa pessoaRepository;
    private final RepositorioUsuario usuarioRepository;
    private final RepositorioPerfil perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public PacienteController(RepositorioPaciente repository,
                              RepositorioConsulta consultaRepository,
                              RepositorioPessoa pessoaRepository,
                              RepositorioUsuario usuarioRepository,
                              RepositorioPerfil perfilRepository,
                              PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.consultaRepository = consultaRepository;
        this.pessoaRepository = pessoaRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("form")
    public ModelAndView form(Paciente paciente) {
        if (paciente.getUsuario() == null) {
            paciente.setUsuario(new Usuario());
        }
        return new ModelAndView("paciente/form");
    }

    @GetMapping("/list")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("pacientes", repository.pacientes());
        return new ModelAndView("paciente/list", model);
    }

    @Transactional
    @PostMapping("save")
    public ModelAndView save(@Valid Paciente paciente, BindingResult result, Authentication authentication) {
        if (pessoaRepository.existeCpf(paciente.getCpf(), null)) {
            result.rejectValue("cpf", "error.paciente", "Ja existe uma pessoa cadastrada com este CPF.");
        }

        if (pessoaRepository.existeEmail(paciente.getEmail(), null)) {
            result.rejectValue("email", "error.paciente", "Ja existe uma pessoa cadastrada com este e-mail.");
        }

        Usuario usuario = paciente.getUsuario();
        if (usuario == null || usuario.getUsuario() == null || usuario.getUsuario().isBlank()) {
            result.rejectValue("usuario.usuario", "error.paciente", "Informe o nome de usuário.");
        } else if (usuarioRepository.existeUsuario(usuario.getUsuario(), null)) {
            result.rejectValue("usuario.usuario", "error.paciente", "Ja existe um usuário com este login.");
        }

        if (usuario == null || usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            result.rejectValue("usuario.senha", "error.paciente", "Informe a senha de acesso.");
        }

        if (result.hasErrors()) {
            return new ModelAndView("paciente/form");
        }

        // Codifica a senha e atribui o papel inicial PACIENTE (e USER)
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Perfil perfilPaciente = perfilRepository.buscarOuCriar("PACIENTE");
        usuario.adicionarPerfil(perfilPaciente);
        Perfil perfilUser = perfilRepository.buscarOuCriar("USER");
        usuario.adicionarPerfil(perfilUser);

        repository.save(paciente);

        if (authentication != null && SecurityUtils.hasAnyRole(authentication, "ADMIN", "SECRETARIO")) {
            return new ModelAndView("redirect:/paciente/list");
        }
        return new ModelAndView("redirect:/login?cadastrado=true");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        Paciente paciente = repository.paciente(id);
        if (paciente != null && paciente.getUsuario() == null) {
            paciente.setUsuario(new Usuario());
        }
        model.addAttribute("paciente", paciente);
        return new ModelAndView("paciente/form", model);
    }

    @Transactional
    @PostMapping("update")
    public ModelAndView update(@Validated({Default.class, Edicao.class}) Paciente paciente,
                               BindingResult result, Authentication authentication) {
        if (pessoaRepository.existeCpf(paciente.getCpf(), paciente.getId())) {
            result.rejectValue("cpf", "error.paciente", "Ja existe uma pessoa cadastrada com este CPF.");
        }

        if (pessoaRepository.existeEmail(paciente.getEmail(), paciente.getId())) {
            result.rejectValue("email", "error.paciente", "Ja existe uma pessoa cadastrada com este e-mail.");
        }

        Paciente existente = repository.paciente(paciente.getId());
        Long usuarioIdIgnorado = (existente != null && existente.getUsuario() != null)
                ? existente.getUsuario().getId() : null;

        Usuario usuario = paciente.getUsuario();
        if (usuario != null && usuario.getUsuario() != null && !usuario.getUsuario().isBlank()) {
            if (usuarioRepository.existeUsuario(usuario.getUsuario(), usuarioIdIgnorado)) {
                result.rejectValue("usuario.usuario", "error.paciente", "Ja existe um usuário com este login.");
            }
        }

        if (result.hasErrors()) {
            return new ModelAndView("paciente/form");
        }

        // Se uma nova senha foi preenchida, codifica com BCrypt; senao o repositorio mantem a atual
        if (usuario != null && usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        repository.update(paciente);
        return new ModelAndView(redirectAfterOperation(authentication, "/paciente/list"));
    }

    @Transactional
    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id, Authentication authentication) {
        repository.remove(id);
        return new ModelAndView(redirectAfterOperation(authentication, "/paciente/list"));
    }

    // Requisito: Visualizar consultas de um paciente
    @GetMapping("/consultas/{id}")
    public ModelAndView consultas(@PathVariable("id") Long id, ModelMap model) {
        Paciente paciente = repository.paciente(id);
        model.addAttribute("paciente", paciente);
        model.addAttribute("consultas", consultaRepository.consultasPorPaciente(id));
        return new ModelAndView("paciente/consultas", model);
    }

    // Atalho: Minhas consultas (para o paciente autenticado)
    @GetMapping("/minhas-consultas")
    public ModelAndView minhasConsultas(Authentication authentication) {
        if (authentication != null) {
            Pessoa pessoa = pessoaRepository.buscarPorUsuario(authentication.getName());
            if (pessoa instanceof Paciente paciente) {
                return new ModelAndView("redirect:/paciente/consultas/" + paciente.getId());
            }
        }
        return new ModelAndView("redirect:/");
    }
}
