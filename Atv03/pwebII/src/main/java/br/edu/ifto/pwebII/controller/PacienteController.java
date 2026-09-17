package br.edu.ifto.pwebII.controller;

import br.edu.ifto.pwebII.model.entity.Paciente;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioConsulta;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioPaciente;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

    public PacienteController(RepositorioPaciente repository, RepositorioConsulta consultaRepository) {
        this.repository = repository;
        this.consultaRepository = consultaRepository;
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
    public ModelAndView save(Paciente paciente) {
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
    public ModelAndView update(Paciente paciente) {
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