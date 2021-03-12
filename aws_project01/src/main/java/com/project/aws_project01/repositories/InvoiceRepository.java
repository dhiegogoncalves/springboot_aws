package com.project.aws_project01.repositories;

import java.util.List;
import java.util.Optional;

import com.project.aws_project01.models.Invoice;

import org.springframework.data.repository.CrudRepository;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
    List<Invoice> findAllByCustomerName(String customerName);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
