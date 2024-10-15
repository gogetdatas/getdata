package com.gogetdata.datamanagemant.domain.repository;

import com.gogetdata.datamanagemant.domain.entity.CompanyData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyDataRepository extends MongoRepository<CompanyData, Long> , CompanyDataRepositoryCustom{
}
