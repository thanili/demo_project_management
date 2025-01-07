package org.example.project_management.service.impl;

import jakarta.transaction.Transactional;
import org.example.project_management.entity.Invoice;
import org.example.project_management.exception.InvoiceNotFoundException;
import org.example.project_management.exception.ProjectNotFoundException;
import org.example.project_management.repository.InvoiceRepository;
import org.example.project_management.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceServiceImpl(final InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    @Transactional
    public Invoice saveInvoice(Invoice invoice) {
        logger.info("Creating a new invoice with amount: {}", invoice.getAmount());
        if(invoice.getProject() == null) {
            throw new ProjectNotFoundException("Project is mandatory for an invoice");
        }
        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        logger.info("Getting all invoices");
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        logger.info("Getting an invoice by Id: {}", id);
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));
    }

    @Override
    @Transactional
    public Invoice updateInvoice(Long id, Invoice invoiceDetails) {
        logger.info("Updating existing invoice");
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));

        if(invoiceDetails.getAmount() != null)
            invoice.setAmount(invoiceDetails.getAmount());

        if(invoiceDetails.getStatus() != null)
            invoice.setStatus(invoiceDetails.getStatus());

        if(invoiceDetails.getDueDate() != null)
            invoice.setDueDate(invoiceDetails.getDueDate());

        if(invoiceDetails.getProject() != null)
            invoice.setProject(invoiceDetails.getProject());

        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        logger.info("Deleting an invoice with Id: {}", id);
        Invoice invoice = getInvoiceById(id);
        invoiceRepository.delete(invoice);
    }

    @Override
    public List<Invoice> getInvoicesByProjectId(Long projectId) {
        logger.info("Getting invoices of project: {}", projectId);
        return invoiceRepository.findAllByProjectId(projectId)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoices not found for project: " + projectId));
    }
}
