package org.example.project_management.service;

import org.example.project_management.entity.Invoice;
import org.example.project_management.entity.Project;
import org.example.project_management.exception.InvoiceNotFoundException;
import org.example.project_management.repository.InvoiceRepository;
import org.example.project_management.service.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvoiceServiceImplTest {
    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Project project = new Project();
        project.setId(1L);
        project.setStartDate(java.time.LocalDate.now().minusMonths(1));
        project.setDeadline(java.time.LocalDate.now().plusMonths(1));
        project.setStatus("IN_PROGRESS");
        project.setTitle("Project 1");

        invoice = new Invoice();
        invoice.setId(1L);
        invoice.setAmount(1000.0);
        invoice.setStatus("PAID");
        invoice.setDueDate(java.time.LocalDate.now());
        invoice.setProject(project);
    }

    @Test
    public void testSaveInvoice() {
        when(invoiceRepository.save(invoice)).thenReturn(invoice);

        Invoice savedInvoice = invoiceService.saveInvoice(invoice);

        assertNotNull(savedInvoice);
        assertEquals(1000.0, savedInvoice.getAmount());
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    public void testGetAllInvoices() {
        when(invoiceRepository.findAll()).thenReturn(Arrays.asList(invoice));

        var invoices = invoiceService.getAllInvoices();

        assertEquals(1, invoices.size());
        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    public void testGetInvoiceById_Success() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        Invoice foundInvoice = invoiceService.getInvoiceById(1L);

        assertNotNull(foundInvoice);
        assertEquals(1L, foundInvoice.getId());
        verify(invoiceRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetInvoiceById_NotFound() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.getInvoiceById(1L));
        verify(invoiceRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateInvoice() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(invoice)).thenReturn(invoice);

        Invoice updatedInvoice = invoiceService.updateInvoice(1L, invoice);

        assertNotNull(updatedInvoice);
        assertEquals(1000.0, updatedInvoice.getAmount());
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    public void testDeleteInvoice() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        invoiceService.deleteInvoice(1L);

        verify(invoiceRepository, times(1)).delete(invoice);
    }
}
