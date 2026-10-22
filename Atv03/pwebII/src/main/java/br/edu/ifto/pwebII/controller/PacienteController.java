package br.edu.ifto.pwebII.controller;

import br.edu.ifto.pwebII.model.entity.Paciente;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioConsulta;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioPaciente;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioPessoa;
import br.edu.ifto.pwebII.model.validation.Edicao;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
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

    public PacienteController(RepositorioPaciente repository, RepositorioConsulta consultaRepository,
                             RepositorioPessoa pessoaRepository) {
        this.repository = repository;
        this.consultaRepository = consultaRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @GetMapping("form")
    public ModelAndView form(Paciente paciente) {
        return new ModelAndView("paciente/form");
    }

    @GetMapping("/list")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("pacientes", repository.pacientes());
        return new ModelAndView("paciente/list", model);
    }

    @Transactional
    @PostMapping("save")
    public ModelAndView save(@Valid Paciente paciente, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("paciente/form");
        }

        if (pessoaRepository.existeCpf(paciente.getCpf(), null)) {
            result.rejectValue("cpf", "error.paciente", "Ja existe uma pessoa cadastrada com este CPF.");
            return new ModelAndView("paciente/form");
        }

        if (pessoaRepository.existeEmail(paciente.getEmail(), null)) {
            result.rejectValue("email", "error.paciente", "Ja existe uma pessoa cadastrada com este e-mail.");
            return new ModelAndView("paciente/form");
        }

        repository.save(paciente);
        return new ModelAndView("redirect:/paciente/list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("paciente", repository.paciente(id));
        return new ModelAndView("paciente/form", model);
    }

    @Transactional
    @PostMapping("update")
    public ModelAndView update(@Validated({Default.class, Edicao.class}) Paciente paciente,
                               BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("paciente/form");
        }

        if (pessoaRepository.existeCpf(paciente.getCpf(), paciente.getId())) {
            result.rejectValue("cpf", "error.paciente", "Ja existe uma pessoa cadastrada com este CPF.");
            return new ModelAndView("paciente/form");
        }

        if (pessoaRepository.existeEmail(paciente.getEmail(), paciente.getId())) {
            result.rejectValue("email", "error.paciente", "Ja existe uma pessoa cadastrada com este e-mail.");
            return new ModelAndView("paciente/form");
        }

        repository.update(paciente);
        return new ModelAndView("redirect:/paciente/list");
    }

    @Transactional
    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        repository.remove(id);
        return new ModelAndView("redirect:/paciente/list");
    }

    // Requisito: Visualizar consultas de um paciente
    @GetMapping("/consultas/{id}")
    public ModelAndView consultas(@PathVariable("id") Long id, ModelMap model) {
        Paciente paciente = repository.paciente(id);
        model.addAttribute("paciente", paciente);
        model.addAttribute("consultas", consultaRepository.consultasPorPaciente(id));
        return new ModelAndView("paciente/consultas", model);
    }
}
