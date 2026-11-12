package br.edu.ifto.pwebII.controller;

import static br.edu.ifto.pwebII.config.SecurityUtils.redirectAfterOperation;

import br.edu.ifto.pwebII.model.entity.Medico;
import br.edu.ifto.pwebII.model.entity.Perfil;
import br.edu.ifto.pwebII.model.entity.Pessoa;
import br.edu.ifto.pwebII.model.entity.Usuario;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioConsulta;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioMedico;
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
@RequestMapping("medico")
public class MedicoController {

    private final RepositorioMedico repository;
    private final RepositorioConsulta consultaRepository;
    private final RepositorioPessoa pessoaRepository;
    private final RepositorioUsuario usuarioRepository;
    private final RepositorioPerfil perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public MedicoController(RepositorioMedico repository,
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
    public ModelAndView form(Medico medico) {
        if (medico.getUsuario() == null) {
            medico.setUsuario(new Usuario());
        }
        return new ModelAndView("medico/form");
    }

    @GetMapping("/list")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("medicos", repository.medicos());
        return new ModelAndView("medico/list", model);
    }

    @Transactional
    @PostMapping("save")
    public ModelAndView save(@Valid Medico medico, BindingResult result, Authentication authentication) {
        if (pessoaRepository.existeCpf(medico.getCpf(), null)) {
            result.rejectValue("cpf", "error.medico", "Ja existe uma pessoa cadastrada com este CPF.");
        }

        if (pessoaRepository.existeEmail(medico.getEmail(), null)) {
            result.rejectValue("email", "error.medico", "Ja existe uma pessoa cadastrada com este e-mail.");
        }

        if (repository.existeCrm(medico.getCrm(), null)) {
            result.rejectValue("crm", "error.medico", "Ja existe um medico cadastrado com este CRM.");
        }

        Usuario usuario = medico.getUsuario();
        if (usuario == null || usuario.getUsuario() == null || usuario.getUsuario().isBlank()) {
            result.rejectValue("usuario.usuario", "error.medico", "Informe o nome de usuário.");
        } else if (usuarioRepository.existeUsuario(usuario.getUsuario(), null)) {
            result.rejectValue("usuario.usuario", "error.medico", "Ja existe um usuário com este login.");
        }

        if (usuario == null || usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            result.rejectValue("usuario.senha", "error.medico", "Informe a senha de acesso.");
        }

        if (result.hasErrors()) {
            return new ModelAndView("medico/form");
        }

        // Codifica a senha e atribui o papel inicial MEDICO (e USER)
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Perfil perfilMedico = perfilRepository.buscarOuCriar("MEDICO");
        usuario.adicionarPerfil(perfilMedico);
        Perfil perfilUser = perfilRepository.buscarOuCriar("USER");
        usuario.adicionarPerfil(perfilUser);

        repository.save(medico);
        return new ModelAndView(redirectAfterOperation(authentication, "/medico/list"));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        Medico medico = repository.medico(id);
        if (medico != null && medico.getUsuario() == null) {
            medico.setUsuario(new Usuario());
        }
        model.addAttribute("medico", medico);
        return new ModelAndView("medico/form", model);
    }

    @Transactional
    @PostMapping("update")
    public ModelAndView update(@Validated({Default.class, Edicao.class}) Medico medico,
                               BindingResult result, Authentication authentication) {
        if (pessoaRepository.existeCpf(medico.getCpf(), medico.getId())) {
            result.rejectValue("cpf", "error.medico", "Ja existe uma pessoa cadastrada com este CPF.");
        }

        if (pessoaRepository.existeEmail(medico.getEmail(), medico.getId())) {
            result.rejectValue("email", "error.medico", "Ja existe uma pessoa cadastrada com este e-mail.");
        }

        if (repository.existeCrm(medico.getCrm(), medico.getId())) {
            result.rejectValue("crm", "error.medico", "Ja existe um medico cadastrado com este CRM.");
        }

        Medico existente = repository.medico(medico.getId());
        Long usuarioIdIgnorado = (existente != null && existente.getUsuario() != null)
                ? existente.getUsuario().getId() : null;

        Usuario usuario = medico.getUsuario();
        if (usuario != null && usuario.getUsuario() != null && !usuario.getUsuario().isBlank()) {
            if (usuarioRepository.existeUsuario(usuario.getUsuario(), usuarioIdIgnorado)) {
                result.rejectValue("usuario.usuario", "error.medico", "Ja existe um usuário com este login.");
            }
        }

        if (result.hasErrors()) {
            return new ModelAndView("medico/form");
        }

        // Se uma nova senha foi preenchida, codifica; senao o repositorio mantem a atual
        if (usuario != null && usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        repository.update(medico);
        return new ModelAndView(redirectAfterOperation(authentication, "/medico/list"));
    }

    @Transactional
    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id, Authentication authentication) {
        repository.remove(id);
        return new ModelAndView(redirectAfterOperation(authentication, "/medico/list"));
    }

    // Requisito: Visualizar consultas de um médico
    @GetMapping("/consultas/{id}")
    public ModelAndView consultas(@PathVariable("id") Long id, ModelMap model) {
        Medico medico = repository.medico(id);
        model.addAttribute("medico", medico);
        model.addAttribute("consultas", consultaRepository.consultasPorMedico(id));
        return new ModelAndView("medico/consultas", model);
    }

    // Atalho: Minha agenda (para o medico autenticado)
    @GetMapping("/minha-agenda")
    public ModelAndView minhaAgenda(Authentication authentication) {
        if (authentication != null) {
            Pessoa pessoa = pessoaRepository.buscarPorUsuario(authentication.getName());
            if (pessoa instanceof Medico medico) {
                return new ModelAndView("redirect:/medico/consultas/" + medico.getId());
            }
        }
        return new ModelAndView("redirect:/");
    }
}
