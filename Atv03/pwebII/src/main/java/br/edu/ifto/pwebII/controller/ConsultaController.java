package br.edu.ifto.pwebII.controller;

import static br.edu.ifto.pwebII.config.SecurityUtils.redirectAfterOperation;

import br.edu.ifto.pwebII.config.SecurityUtils;
import br.edu.ifto.pwebII.model.entity.Consulta;
import br.edu.ifto.pwebII.model.entity.Paciente;
import br.edu.ifto.pwebII.model.entity.Pessoa;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioConsulta;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioMedico;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioPaciente;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioPessoa;
import br.edu.ifto.pwebII.model.validation.Cadastro;
import br.edu.ifto.pwebII.model.validation.Edicao;
import jakarta.transaction.Transactional;
import jakarta.validation.groups.Default;
import org.springframework.security.core.Authentication;
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
@RequestMapping("consulta")
public class ConsultaController {

    private final RepositorioConsulta repository;
    private final RepositorioPaciente pacienteRepository;
    private final RepositorioMedico medicoRepository;
    private final RepositorioPessoa pessoaRepository;

    public ConsultaController(RepositorioConsulta repository,
                              RepositorioPaciente pacienteRepository,
                              RepositorioMedico medicoRepository,
                              RepositorioPessoa pessoaRepository) {
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @GetMapping("form")
    public ModelAndView form(Consulta consulta, ModelMap model, Authentication authentication) {
        if (authentication != null && SecurityUtils.hasRole(authentication, "PACIENTE")
                && !SecurityUtils.hasAnyRole(authentication, "ADMIN", "SECRETARIO")) {
            Pessoa pessoa = pessoaRepository.buscarPorUsuario(authentication.getName());
            if (pessoa instanceof Paciente p) {
                consulta.setPaciente(p);
                model.addAttribute("pacienteLogado", p);
            }
        }

        model.addAttribute("pacientes", pacienteRepository.pacientes());
        model.addAttribute("medicos", medicoRepository.medicos());
        return new ModelAndView("consulta/form", model);
    }

    @GetMapping("/list")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("consultas", repository.consultas());
        return new ModelAndView("consulta/list", model);
    }

    @Transactional
    @PostMapping("save")
    public ModelAndView save(@Validated({Default.class, Cadastro.class}) Consulta consulta,
                             BindingResult result, ModelMap model, Authentication authentication) {
        if (authentication != null && SecurityUtils.hasRole(authentication, "PACIENTE")
                && !SecurityUtils.hasAnyRole(authentication, "ADMIN", "SECRETARIO")) {
            Pessoa pessoa = pessoaRepository.buscarPorUsuario(authentication.getName());
            if (pessoa instanceof Paciente p) {
                consulta.setPaciente(p);
                model.addAttribute("pacienteLogado", p);
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("pacientes", pacienteRepository.pacientes());
            model.addAttribute("medicos", medicoRepository.medicos());
            return new ModelAndView("consulta/form", model);
        }

        repository.save(consulta);
        return new ModelAndView(redirectAfterOperation(authentication, "/consulta/list"));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("consulta", repository.consulta(id));
        model.addAttribute("pacientes", pacienteRepository.pacientes());
        model.addAttribute("medicos", medicoRepository.medicos());
        return new ModelAndView("consulta/form", model);
    }

    @Transactional
    @PostMapping("update")
    public ModelAndView update(@Validated({Default.class, Edicao.class}) Consulta consulta,
                               BindingResult result, ModelMap model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("pacientes", pacienteRepository.pacientes());
            model.addAttribute("medicos", medicoRepository.medicos());
            return new ModelAndView("consulta/form", model);
        }
        repository.update(consulta);
        return new ModelAndView(redirectAfterOperation(authentication, "/consulta/list"));
    }

    @Transactional
    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id, Authentication authentication) {
        repository.remove(id);
        return new ModelAndView(redirectAfterOperation(authentication, "/consulta/list"));
    }
}
