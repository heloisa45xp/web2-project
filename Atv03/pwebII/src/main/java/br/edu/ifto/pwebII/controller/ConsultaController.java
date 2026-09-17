package br.edu.ifto.pwebII.controller;

import br.edu.ifto.pwebII.model.entity.Consulta;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioConsulta;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioMedico;
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
@RequestMapping("consulta")
public class ConsultaController {

    private final RepositorioConsulta repository;
    private final RepositorioPaciente pacienteRepository;
    private final RepositorioMedico medicoRepository;

    public ConsultaController(RepositorioConsulta repository, RepositorioPaciente pacienteRepository, RepositorioMedico medicoRepository) {
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    @GetMapping("form")
    public ModelAndView form(Consulta consulta, ModelMap model) {
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
    public ModelAndView save(Consulta consulta) {
        repository.save(consulta);
        return new ModelAndView("redirect:/consulta/list");
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
    public ModelAndView update(Consulta consulta) {
        repository.update(consulta);
        return new ModelAndView("redirect:/consulta/list");
    }

    @Transactional
    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable("id") Long id) {
        repository.remove(id);
        return new ModelAndView("redirect:/consulta/list");
    }
}