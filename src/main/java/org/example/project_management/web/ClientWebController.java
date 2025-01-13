package org.example.project_management.web;

import jakarta.validation.Valid;
import org.example.project_management.api.ClientController;
import org.example.project_management.entity.Client;
import org.example.project_management.service.ClientService;
import org.example.project_management.service.ProjectCoordinatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hub")
public class ClientWebController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;
    private final ProjectCoordinatorService projectCoordinatorService;

    public ClientWebController(ClientService clientService, ProjectCoordinatorService projectCoordinatorService) {
        this.clientService = clientService;
        this.projectCoordinatorService = projectCoordinatorService;
    }

    @GetMapping("/clients")
    public String getClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "clients";
    }

    @GetMapping("/clients/{id}/edit")
    public String editClient(Model model, @PathVariable Long id) {
        model.addAttribute("client", clientService.getClientById(id));
        return "edit-client";
    }

    @GetMapping("/clients/{id}/projects")
    public String getClientProjects(Model model, @PathVariable Long id) {
        model.addAttribute("projects", projectCoordinatorService.getClientProjectsByClientId(id));
        model.addAttribute("client",clientService.getClientById(id));
        return "client-projects";
    }

    @PostMapping("/clients/save")
    public String saveClient(@Valid Client client, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "edit-client";

        clientService.handleSubmitClient(client);
        return "redirect:/hub/clients";
    }

    @PostMapping("/clients/{id}/delete")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "redirect:/hub/clients";
    }

    @GetMapping("/clients/new")
    public String newClientForm(Model model) {
        model.addAttribute("client", new Client());
        return "/edit-client";
    }
}
