package com.gogetdata.datamanagemant.domain.repository;

import com.gogetdata.datamanagemant.domain.entity.TypeSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeSetRepository extends JpaRepository<TypeSet,Long> ,TypeSetRepositoryCustom{
}
