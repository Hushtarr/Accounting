package com.cydeo.repository;

import com.cydeo.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Long, Invoice> {
}
