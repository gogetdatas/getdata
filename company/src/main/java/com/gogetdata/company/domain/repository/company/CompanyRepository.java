package com.gogetdata.company.domain.repository.company;

import com.gogetdata.company.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository <Company,Long> {
}
