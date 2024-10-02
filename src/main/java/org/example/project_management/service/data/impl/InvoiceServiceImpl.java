package org.example.project_management.service.data.impl;

import jakarta.transaction.Transactional;
import org.example.project_management.entity.Invoice;
import org.example.project_management.exception.InvoiceNotFoundException;
import org.example.project_management.repository.InvoiceRepository;
import org.example.project_management.service.data.InvoiceService;
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
        else
            invoice.setAmount(invoice.getAmount());

        if(invoiceDetails.getStatus() != null)
            invoice.setStatus(invoiceDetails.getStatus());
        else
            invoice.setStatus(invoice.getStatus());

        if(invoiceDetails.getDueDate() != null)
            invoice.setDueDate(invoiceDetails.getDueDate());
        else
            invoice.setDueDate(invoice.getDueDate());

        if(invoiceDetails.getProject() != null)
            invoice.setProject(invoiceDetails.getProject());
        else
            invoice.setProject(invoice.getProject());

        return invoiceRepository.save(invoice);
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        logger.info("Deleting an invoice with Id: {}", id);
        Invoice invoice = getInvoiceById(id);
        invoiceRepository.delete(invoice);
    }
}
