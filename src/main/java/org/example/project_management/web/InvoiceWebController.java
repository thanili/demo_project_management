package org.example.project_management.web;

import jakarta.validation.Valid;
import org.example.project_management.entity.Invoice;
import org.example.project_management.entity.Project;
import org.example.project_management.service.InvoiceService;
import org.example.project_management.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hub")
public class InvoiceWebController {

    private final InvoiceService invoiceService;
    private final ProjectService projectService;

    public InvoiceWebController(InvoiceService invoiceService, ProjectService projectService) {
        this.invoiceService = invoiceService;
        this.projectService = projectService;
    }

    @GetMapping("/invoices/{id}/edit")
    public String editInvoice(Model model, @PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        model.addAttribute("invoice", invoice);
        return "edit-invoice";
    }

    @PostMapping("/invoices/handleSubmit")
    public String submitForm(@Valid Invoice invoice, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "edit-invoice";

        if(invoice.getId() != null) {
            Invoice existingInvoice = invoiceService.getInvoiceById(invoice.getId());
            if (existingInvoice != null) {
                invoice.setProject(existingInvoice.getProject());
            }
        } else {
            // New invoice
            Project project = projectService.getProjectById(invoice.getProject().getId());
            invoice.setProject(project);
        }
        invoiceService.saveInvoice(invoice);
        return "redirect:/hub/projects/" + invoice.getProject().getId() + "/invoices";
    }

    @PostMapping("/invoices/{id}/delete")
    public String deleteInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        Project project = invoice.getProject();
        invoiceService.deleteInvoice(id);
        return "redirect:/hub/projects/" + project.getId() + "/invoices";
    }

    @GetMapping("/projects/{projectId}/invoices/new")
    public String newInvoiceForm(Model model, @PathVariable long projectId) {
        Project project = projectService.getProjectById(projectId);
        Invoice newInvoice = new Invoice();
        newInvoice.setProject(project);
        model.addAttribute("invoice", newInvoice);
        return "edit-invoice";
    }
}
