package org.example.project_management.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.project_management.dto.InvoiceDto;
import org.example.project_management.entity.Invoice;
import org.example.project_management.helper.EntityDtoConverter;
import org.example.project_management.security.JwtUtils;
import org.example.project_management.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InvoiceController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class InvoiceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    @MockBean
    private EntityDtoConverter entityDtoConverter;

    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    UserDetailsService userDetailsService;

    @Autowired
    ObjectMapper objectMapper;

    private Invoice invoice;
    private InvoiceDto invoiceDto;

    @BeforeEach
    public void setUp() {
        invoice = new Invoice();
        invoice.setId(1L);
        invoice.setAmount(1000.0);
        invoice.setStatus("PAID");
        invoice.setDueDate(LocalDate.now());

        invoiceDto = new InvoiceDto(invoice.getId(),
                1000.0,
                LocalDate.now(),
                "PAID",
                1L);
    }

    @Test
    public void testCreateInvoice() throws Exception {
        when(invoiceService.saveInvoice(any(Invoice.class))).thenReturn(invoice);
        when(entityDtoConverter.convertInvoiceToInvoiceDto(any(Invoice.class))).thenReturn(invoiceDto);
        when(entityDtoConverter.convertInvoiceDtoToInvoice(any(InvoiceDto.class))).thenReturn(invoice);

        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invoiceDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1000.0));
    }

    @Test
    public void testGetAllInvoices() throws Exception {
        when(invoiceService.getAllInvoices()).thenReturn(Arrays.asList(invoice));
        when(entityDtoConverter.convertInvoicesToInvoiceDtos(anyList())).thenReturn(Arrays.asList(invoiceDto));

        mockMvc.perform(get("/api/invoices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1000.0));
    }

    @Test
    public void testGetInvoiceById() throws Exception {
        when(invoiceService.getInvoiceById(1L)).thenReturn(invoice);
        when(entityDtoConverter.convertInvoiceToInvoiceDto(invoice)).thenReturn(invoiceDto);

        mockMvc.perform(get("/api/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1000.0));
    }

    @Test
    public void testUpdateInvoice() throws Exception {
        when(invoiceService.updateInvoice(anyLong(), any(Invoice.class))).thenReturn(invoice);
        when(entityDtoConverter.convertInvoiceToInvoiceDto(invoice)).thenReturn(invoiceDto);
        when(entityDtoConverter.convertInvoiceDtoToInvoice(invoiceDto)).thenReturn(invoice);

        mockMvc.perform(put("/api/invoices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invoiceDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1000.0));
    }

    @Test
    public void testDeleteInvoice() throws Exception {
        doNothing().when(invoiceService).deleteInvoice(1L);

        mockMvc.perform(delete("/api/invoices/1"))
                .andExpect(status().isOk());
    }
}
