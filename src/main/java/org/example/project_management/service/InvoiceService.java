package org.example.project_management.service;

import org.example.project_management.entity.Invoice;

import java.util.List;

public interface InvoiceService {
    Invoice saveInvoice(Invoice invoice);
    List<Invoice> getAllInvoices();
    Invoice getInvoiceById(Long id);
    Invoice updateInvoice(Long id, Invoice invoiceDetails);
    void deleteInvoice(Long id);
}
