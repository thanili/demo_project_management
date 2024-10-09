package org.example.project_management.api;

import jakarta.validation.Valid;
import org.example.project_management.dto.InvoiceDto;
import org.example.project_management.entity.Invoice;
import org.example.project_management.helper.EntityDtoConverter;
import org.example.project_management.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    private final InvoiceService invoiceService;
    private final EntityDtoConverter entityDtoConverter;

    @Autowired
    public InvoiceController(final InvoiceService invoiceService,
                            final EntityDtoConverter entityDtoConverter) {
        this.invoiceService = invoiceService;
        this.entityDtoConverter = entityDtoConverter;
    }

    @PostMapping
    public ResponseEntity<InvoiceDto> createInvoice(@Valid @RequestBody InvoiceDto invoiceDto) {
        logger.info("Request to create a new invoice with amount {} received", invoiceDto.getAmount());
        Invoice savedInvoice = invoiceService.saveInvoice(entityDtoConverter.convertInvoiceDtoToInvoice(invoiceDto));
        return ResponseEntity.ok(entityDtoConverter.convertInvoiceToInvoiceDto(savedInvoice));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDto>> getAllInvoices() {
        logger.info("Request to get all invoices received");
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(entityDtoConverter.convertInvoicesToInvoiceDtos(invoices));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDto> getInvoiceById(@PathVariable Long id) {
        logger.info("Request to get invoice by Id received: {}", id);
        Invoice invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(entityDtoConverter.convertInvoiceToInvoiceDto(invoice));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDto> updateInvoice(@PathVariable Long id, @Valid @RequestBody InvoiceDto invoiceDtoDetails) {
        logger.info("Request to update an existing invoice received");
        Invoice invoiceDetails = entityDtoConverter.convertInvoiceDtoToInvoice(invoiceDtoDetails);
        Invoice updatedInvoice = invoiceService.updateInvoice(id, invoiceDetails);
        return ResponseEntity.ok(entityDtoConverter.convertInvoiceToInvoiceDto(updatedInvoice));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Long id) {
        logger.info("Request to delete invoice by Id received: {}", id);
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok("Invoice deleted successfully");
    }
}
