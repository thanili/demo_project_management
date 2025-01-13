package org.example.project_management.web;

import jakarta.validation.Valid;
import org.example.project_management.entity.Invoice;
import org.example.project_management.service.InvoiceService;
import org.example.project_management.service.ProjectCoordinatorService;
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
    private final ProjectCoordinatorService projectCoordinatorService;

    public InvoiceWebController(InvoiceService invoiceService, ProjectCoordinatorService projectCoordinatorService) {
        this.invoiceService = invoiceService;
        this.projectCoordinatorService = projectCoordinatorService;
    }

    @GetMapping("/invoices/{id}/edit")
    public String editInvoice(Model model, @PathVariable Long id) {
        model.addAttribute("invoice", invoiceService.getInvoiceById(id));
        return "edit-invoice";
    }

    @PostMapping("/invoices/save")
    public String saveInvoice(@Valid Invoice invoice, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "edit-invoice";

        projectCoordinatorService.handleSubmitProjectInvoice(invoice);
        return "redirect:/hub/projects/" + invoice.getProject().getId() + "/invoices";
    }

    @PostMapping("/invoices/{id}/delete")
    public String deleteInvoice(@PathVariable Long id) {
        Long projectId = invoiceService.getInvoiceById(id).getProject().getId();
        invoiceService.deleteInvoice(id);
        return "redirect:/hub/projects/" + projectId + "/invoices";
    }

    @GetMapping("/projects/{projectId}/invoices/new")
    public String newInvoiceForm(Model model, @PathVariable long projectId) {
        model.addAttribute("invoice", projectCoordinatorService.createProjectInvoice(projectId));
        return "edit-invoice";
    }
}
