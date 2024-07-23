package com.cydeo.repository;

import com.cydeo.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByIdAndIsDeleted (Long id, Boolean deleted);
    List<Company> findAllByIsDeleted (Boolean deleted);
}
