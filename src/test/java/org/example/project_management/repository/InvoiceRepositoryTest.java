package org.example.project_management.repository;

import org.example.project_management.entity.Client;
import org.example.project_management.entity.Invoice;
import org.example.project_management.entity.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class InvoiceRepositoryTest {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void testSaveInvoice() {
        Client client = new Client();
        client.setName("ABC Corp");
        client.setEmail("test1@email.net");
        clientRepository.save(client);

        Project project = new Project();
        project.setTitle("Website Redesign");
        project.setStatus("in-progress");
        project.setClient(client);
        projectRepository.save(project);

        Invoice invoice = new Invoice();
        invoice.setAmount(500.0);
        invoice.setDueDate(LocalDate.now());
        invoice.setStatus("unpaid");
        invoice.setProject(project);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        assertThat(savedInvoice.getId()).isNotNull();

        assertThat(savedInvoice.getAmount()).isEqualTo(500.0);
    }

    @Test
    void testFindAllInvoices() {
        Client client = new Client();
        client.setName("ABC Corp");
        client.setEmail("test@test.com");
        clientRepository.save(client);

        Project project = new Project();
        project.setTitle("Website Redesign");
        project.setStatus("in-progress");
        project.setClient(client);
        projectRepository.save(project);

        Invoice invoice1 = new Invoice();
        invoice1.setAmount(500.0);
        invoice1.setStatus("unpaid");
        invoice1.setProject(project);

        Invoice invoice2 = new Invoice();
        invoice2.setAmount(1000.0);
        invoice2.setStatus("unpaid");
        invoice2.setProject(project);

        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);

        List<Invoice> invoices = invoiceRepository.findAll();
        assertThat(invoices).hasSize(2);
    }

    @Test
    void testFindInvoiceById() {
        Client client = new Client();
        client.setName("ABC Corp");
        client.setEmail("test1@test.com");
        clientRepository.save(client);

        Project project = new Project();
        project.setTitle("Website Redesign");
        project.setStatus("in-progress");
        project.setClient(client);
        projectRepository.save(project);

        Invoice invoice = new Invoice();
        invoice.setStatus("unpaid");
        invoice.setAmount(500.0);
        invoice.setProject(project);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        Invoice foundInvoice = invoiceRepository.findById(savedInvoice.getId()).orElse(null);
        assertThat(foundInvoice).isNotNull();
        assertThat(foundInvoice.getAmount()).isEqualTo(500.0);
    }

    @Test
    void testDeleteInvoice() {
        Client client = new Client();
        client.setName("ABC Corp");
        client.setEmail("test1@test.com");
        clientRepository.save(client);

        Project project = new Project();
        project.setTitle("Website Redesign");
        project.setClient(client);
        project.setStatus("in-progress");
        projectRepository.save(project);

        Invoice invoice = new Invoice();
        invoice.setAmount(500.0);
        invoice.setStatus("unpaid");
        invoice.setProject(project);

        Invoice savedInvoice = invoiceRepository.save(invoice);
        invoiceRepository.delete(savedInvoice);

        assertThat(invoiceRepository.findById(savedInvoice.getId())).isEmpty();
    }
}
