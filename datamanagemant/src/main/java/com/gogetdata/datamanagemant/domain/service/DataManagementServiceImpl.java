package com.gogetdata.datamanagemant.domain.service;

import com.gogetdata.datamanagemant.application.DataManagementService;
import com.gogetdata.datamanagemant.application.dto.data.QueryRequest;
import com.gogetdata.datamanagemant.application.dto.data.QueryResponse;
import com.gogetdata.datamanagemant.application.dto.types.*;
import com.gogetdata.datamanagemant.domain.entity.KeySet;
import com.gogetdata.datamanagemant.domain.entity.KeySetReference;
import com.gogetdata.datamanagemant.domain.entity.SubTypeSet;
import com.gogetdata.datamanagemant.domain.entity.TypeSet;
import com.gogetdata.datamanagemant.domain.repository.CompanyDataRepository;
import com.gogetdata.datamanagemant.domain.repository.TypeSetRepository;
import com.gogetdata.datamanagemant.infrastructure.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@RequiredArgsConstructor
@Service
public class DataManagementServiceImpl implements DataManagementService {
    private final TypeSetRepository repository;
    private final CompanyDataRepository companyDataRepository;
    @Override
    public List<QueryResponse> findChannelData(QueryRequest queryRequest,Long companyId) {
        return companyDataRepository.findChannelData(queryRequest,companyId);
    }
    public TypeResponse findType(CustomUserDetails customUserDetails, Long companyId) {
        getAccessibleAdmin(customUserDetails,companyId);
        List<TypeSet> typeSets = repository.getTypeSet(companyId);
        List<TypeList> typeLists = new ArrayList<>();
        for (TypeSet typeSet : typeSets) {
            typeLists.add(TypeList.from(typeSet));
        }
        return TypeResponse.from(typeLists);
    }
    @Override
    public SubTypeResponse findSubType(CustomUserDetails customUserDetails, Long typeSetId, Long companyId) {
        getAccessibleAdmin(customUserDetails,companyId);
        List<SubTypeSet> subTypeSets = repository.getSubTypeSet(typeSetId);
        List<SubTypeList> subTypeLists = new ArrayList<>();
        for (SubTypeSet subType : subTypeSets) {
            subTypeLists.add(SubTypeList.from(subType));
        }
        return SubTypeResponse.from(subTypeLists);
    }

    @Override
    public KeySetResponse findKeySet(CustomUserDetails customUserDetails, Long subTypeSetId, Long companyId) {
        getAccessibleAdmin(customUserDetails,companyId);
        List<KeySetReference> keySetReferences = repository.getKeySetReference(subTypeSetId);
        List<KeySetMapping> keySetMappings = new ArrayList<>();
        for (KeySetReference keySetReference : keySetReferences) {
            List<KeySetList> keySetLists = new ArrayList<>();
            List<KeySet> keySets = repository.getKeySet(keySetReference.getKeySetReferenceId());
            for (KeySet keySet : keySets) {
                keySetLists.add(KeySetList.from(keySet));
            }
            keySetMappings.add(KeySetMapping.from(keySetLists, keySetReference.getKeySetHash()));
        }
        return KeySetResponse.from(keySetMappings);
    }

    private void getAccessibleAdmin(CustomUserDetails customUserDetails, Long companyId) {
        if (isAdmin(customUserDetails.getAuthorities().toString())) {
            return;
        }
        isCompany(companyId, customUserDetails.companyId());
    }

    private boolean isAdmin(String role) {
        return role.equals("ADMIN");
    }

    private void isCompany(Long companyId, Long loginCompanyId) {
        if (Objects.equals(loginCompanyId, companyId)) {
            return;
        } else {
            throw new IllegalAccessError("권한없음");
        }
    }

}
