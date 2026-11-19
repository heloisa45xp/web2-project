package br.edu.ifto.pwebII.controller;

import br.edu.ifto.pwebII.model.entity.Perfil;
import br.edu.ifto.pwebII.model.entity.Secretario;
import br.edu.ifto.pwebII.model.entity.Usuario;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioPerfil;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioPessoa;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioSecretario;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioUsuario;
import br.edu.ifto.pwebII.model.validation.Edicao;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
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
@RequestMapping("secretario")
public class SecretarioController {

    private final RepositorioSecretario repository;
    private final RepositorioPessoa pessoaRepository;
    private final RepositorioUsuario usuarioRepository;
    private final RepositorioPerfil perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public SecretarioController(RepositorioSecretario repository,
                                RepositorioPessoa pessoaRepository,
                                RepositorioUsuario usuarioRepository,
                                RepositorioPerfil perfilRepository,
                                PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("form")
    public ModelAndView form(Secretario secretario) {
        if (secretario.getUsuario() == null) {
            secretario.setUsuario(new Usuario());
        }
        return new ModelAndView("secretario/form");
    }

    @GetMapping("/list")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("secretarios", repository.secretarios());
        return new ModelAndView("secretario/list", model);
    }

    @Transactional
    @PostMapping("save")
    public ModelAndView save(@Valid Secretario secretario, BindingResult result) {
        if (pessoaRepository.existeCpf(secretario.getCpf(), null)) {
            result.rejectValue("cpf", "error.secretario", "Ja existe uma pessoa cadastrada com este CPF.");
        }

        if (pessoaRepository.existeEmail(secretario.getEmail(), null)) {
            result.rejectValue("email", "error.secretario", "Ja existe uma pessoa cadastrada com este e-mail.");
        }

        Usuario usuario = secretario.getUsuario();
        if (usuario == null || usuario.getUsuario() == null || usuario.getUsuario().isBlank()) {
            result.rejectValue("usuario.usuario", "error.secretario", "Informe o nome de usuário.");
        } else if (usuarioRepository.existeUsuario(usuario.getUsuario(), null)) {
            result.rejectValue("usuario.usuario", "error.secretario", "Ja existe um usuário com este login.");
        }

        if (usuario == null || usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            result.rejectValue("usuario.senha", "error.secretario", "Informe a senha de acesso.");
        }

        if (result.hasErrors()) {
            return new ModelAndView("secretario/form");
        }

        // Codifica a senha e atribui o papel SECRETARIO (e USER)
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Perfil perfilSecretario = perfilRepository.buscarOuCriar("SECRETARIO");
        usuario.adicionarPerfil(perfilSecretario);
        Perfil perfilUser = perfilRepository.buscarOuCriar("USER");
        usuario.adicionarPerfil(perfilUser);

        repository.save(secretario);
        return new ModelAndView("redirect:/secretario/list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        Secretario secretario = repository.secretario(id);
        if (secretario != null && secretario.getUsuario() == null) {
            secretario.setUsuario(new Usuario());
        }
        model.addAttribute("secretario", secretario);
        return new ModelAndView("secretario/form", model);
    }

    @Transactional
    @PostMapping("update")
    public ModelAndView update(@Validated({Default.class, Edicao.class}) Secretario secretario,
                               BindingResult result) {
        if (pessoaRepository.existeCpf(secretario.getCpf(), secretario.getId())) {
            result.rejectValue("cpf", "error.secretario", "Ja existe uma pessoa cadastrada com este CPF.");
        }

        if (pessoaRepository.existeEmail(secretario.getEmail(), secretario.getId())) {
            result.rejectValue("email", "error.secretario", "Ja existe uma pessoa cadastrada com este e-mail.");
        }

        Secretario existente = repository.secretario(secretario.getId());
        Long usuarioIdIgnorado = (existente != null && existente.getUsuario() != null)
                ? existente.getUsuario().getId() : null;

        Usuario usuario = secretario.getUsuario();
        if (usuario != null && usuario.getUsuario() != null && !usuario.getUsuario().isBlank()) {
            if (usuarioRepository.existeUsuario(usuario.getUsuario(), usuarioIdIgnorado)) {
                result.rejectValue("usuario.usuario", "error.secretario", "Ja existe um usuário com este login.");
            }
        }

        if (result.hasErrors()) {
            return new ModelAndView("secretario/form");
        }

        // Se uma nova senha foi informada, codifica com BCrypt
        if (usuario != null && usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        repository.update(secretario);
        return new ModelAndView("redirect:/secretario/list");
    }

    @Transactional
    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        repository.remove(id);
        return new ModelAndView("redirect:/secretario/list");
    }
}
