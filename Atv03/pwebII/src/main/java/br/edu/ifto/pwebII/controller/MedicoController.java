package br.edu.ifto.pwebII.controller;

import br.edu.ifto.pwebII.model.entity.Medico;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioConsulta;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioMedico;
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
@RequestMapping("medico")
public class MedicoController {

    private final RepositorioMedico repository;
    private final RepositorioConsulta consultaRepository;
    private final RepositorioPessoa pessoaRepository;

    public MedicoController(RepositorioMedico repository, RepositorioConsulta consultaRepository,
                            RepositorioPessoa pessoaRepository) {
        this.repository = repository;
        this.consultaRepository = consultaRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @GetMapping("form")
    public ModelAndView form(Medico medico) {
        return new ModelAndView("medico/form");
    }

    @GetMapping("/list")
    public ModelAndView list(ModelMap model) {
        model.addAttribute("medicos", repository.medicos());
        return new ModelAndView("medico/list", model);
    }

    @Transactional
    @PostMapping("save")
    public ModelAndView save(@Valid Medico medico, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("medico/form");
        }

        if (pessoaRepository.existeCpf(medico.getCpf(), null)) {
            result.rejectValue("cpf", "error.medico", "Ja existe uma pessoa cadastrada com este CPF.");
            return new ModelAndView("medico/form");
        }

        if (pessoaRepository.existeEmail(medico.getEmail(), null)) {
            result.rejectValue("email", "error.medico", "Ja existe uma pessoa cadastrada com este e-mail.");
            return new ModelAndView("medico/form");
        }

        if (repository.existeCrm(medico.getCrm(), null)) {
            result.rejectValue("crm", "error.medico", "Ja existe um medico cadastrado com este CRM.");
            return new ModelAndView("medico/form");
        }

        repository.save(medico);
        return new ModelAndView("redirect:/medico/list");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("medico", repository.medico(id));
        return new ModelAndView("medico/form", model);
    }

    @Transactional
    @PostMapping("update")
    public ModelAndView update(@Validated({Default.class, Edicao.class}) Medico medico,
                               BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("medico/form");
        }

        if (pessoaRepository.existeCpf(medico.getCpf(), medico.getId())) {
            result.rejectValue("cpf", "error.medico", "Ja existe uma pessoa cadastrada com este CPF.");
            return new ModelAndView("medico/form");
        }

        if (pessoaRepository.existeEmail(medico.getEmail(), medico.getId())) {
            result.rejectValue("email", "error.medico", "Ja existe uma pessoa cadastrada com este e-mail.");
            return new ModelAndView("medico/form");
        }

        if (repository.existeCrm(medico.getCrm(), medico.getId())) {
            result.rejectValue("crm", "error.medico", "Ja existe um medico cadastrado com este CRM.");
            return new ModelAndView("medico/form");
        }

        repository.update(medico);
        return new ModelAndView("redirect:/medico/list");
    }

    @Transactional
    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        repository.remove(id);
        return new ModelAndView("redirect:/medico/list");
    }

    // Requisito: Visualizar consultas de um médico
    @GetMapping("/consultas/{id}")
    public ModelAndView consultas(@PathVariable("id") Long id, ModelMap model) {
        Medico medico = repository.medico(id);
        model.addAttribute("medico", medico);
        model.addAttribute("consultas", consultaRepository.consultasPorMedico(id));
        return new ModelAndView("medico/consultas", model);
    }
}
