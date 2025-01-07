package org.example.project_management.web;

import org.example.project_management.api.ClientController;
import org.example.project_management.entity.Client;
import org.example.project_management.entity.Project;
import org.example.project_management.service.ClientService;
import org.example.project_management.service.InvoiceService;
import org.example.project_management.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/hub")
public class ClientWebController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;
    private final ProjectService projectService;
    private final InvoiceService invoiceService;

    public ClientWebController(ClientService clientService, ProjectService projectService, InvoiceService invoiceService) {
        this.clientService = clientService;
        this.projectService = projectService;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/clients")
    public String getClients(Model model) {
        List<Client> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @GetMapping("/clients/{id}/edit")
    public String editClient(Model model, @PathVariable Long id) {
        Client client = clientService.getClientById(id);
        model.addAttribute("client", client);
        return "edit-client";
    }

    @GetMapping("/clients/{id}/projects")
    public String getClientProjects(Model model, @PathVariable Long id) {
        List<Project> projects = projectService.getClientProjectsByClientId(id);
        Client client = clientService.getClientById(id);
        model.addAttribute("projects", projects);
        model.addAttribute("client",client);
        return "client-projects";
    }

    @GetMapping("/clients/{id}/invoices")
    public String getClientInvoices(Model model, @PathVariable Long id) {
        return "project-invoices";
    }

    @PostMapping("/clients/handleSubmit")
    public String submitForm(Client client) {
        if(client.getId() != null) {
            Client existingClient = clientService.getClientById(client.getId());
            if (existingClient != null)
                client.setProjects(existingClient.getProjects());
        }
        clientService.saveClient(client);
        return "redirect:/hub/clients";
    }

    @PostMapping("/clients/{id}/delete")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "redirect:/hub/clients";
    }

    @GetMapping("/clients/new")
    public String newClientForm(Model model) {
        Client newClient = new Client();
        model.addAttribute("client", newClient);
        return "/edit-client";
    }
}
