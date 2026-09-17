package br.edu.ifto.pwebII.controller;

import br.edu.ifto.pwebII.model.entity.Medico;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioConsulta;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioMedico;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

    public MedicoController(RepositorioMedico repository, RepositorioConsulta consultaRepository) {
        this.repository = repository;
        this.consultaRepository = consultaRepository;
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
    public ModelAndView save(Medico medico) {
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
    public ModelAndView update(Medico medico) {
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