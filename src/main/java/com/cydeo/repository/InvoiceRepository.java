package com.cydeo.repository;

import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByInvoiceTypeAndCompany_TitleOrderByInvoiceNoDesc(InvoiceType invoiceType, String title);

    Invoice findByInvoiceType(InvoiceType invoiceType);

}
