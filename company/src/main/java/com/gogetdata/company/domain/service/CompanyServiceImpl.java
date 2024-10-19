package com.gogetdata.company.domain.service;

import com.gogetdata.company.application.CompanyService;
import com.gogetdata.company.application.UserService;
import com.gogetdata.company.application.dto.MessageResponse;
import com.gogetdata.company.application.dto.company.CompanyResponse;
import com.gogetdata.company.application.dto.company.CreateCompanyRequest;
import com.gogetdata.company.application.dto.company.UpdateCompanyRequest;
import com.gogetdata.company.application.dto.feignclient.RegistrationResult;
import com.gogetdata.company.domain.entity.AffiliationStatus;
import com.gogetdata.company.domain.entity.Company;
import com.gogetdata.company.domain.entity.CompanyUser;
import com.gogetdata.company.domain.entity.CompanyUserType;
import com.gogetdata.company.domain.repository.company.CompanyRepository;
import com.gogetdata.company.domain.repository.companyuser.CompanyUserRepository;
import com.gogetdata.company.infrastructure.filter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyUserRepository companyUserRepository;
    private final UserService userService;
    @Override
    @Transactional
    public CompanyResponse createCompany(CustomUserDetails customUserDetails, CreateCompanyRequest createCompanyRequest) {
        Company company = Company.create(
                createCompanyRequest.getCompanyName(),
                UUID.randomUUID().toString()
        );
        companyRepository.save(company);
        RegistrationResult result = userService.registerUser(customUserDetails.userId(),company.getCompanyId());
        if (!result.getIsSuccess()) {
            throw new IllegalAccessError("이미 소속되어 있습니다.");
            }
        CompanyUser companyUser = CompanyUser.create(
                company.getCompanyId(),
                customUserDetails.userId(),
                AffiliationStatus.APPROVED,
                CompanyUserType.ADMIN,
                result.getUserName(),
                result.getEmail()
        );
        companyUserRepository.save(companyUser);

        return CompanyResponse.from(company);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "companies", key = "#companyId")
    public CompanyResponse readCompany(CustomUserDetails customUserDetails, Long companyId) {
        Company company = getReadableCompany(customUserDetails, companyId);
        return CompanyResponse.from(company);
    }

    @Override
    @Transactional
    @CachePut(value = "companies", key = "#companyId")
    public CompanyResponse updateCompany(CustomUserDetails customUserDetails, Long companyId, UpdateCompanyRequest updateCompanyRequest) {
        Company company = getAdminAccessibleCompany(customUserDetails, companyId);
        company.updateCompany(updateCompanyRequest.getCompanyName());
        companyRepository.save(company);
        return CompanyResponse.from(company);
    }

    @Override
    @Transactional
    @CacheEvict(value = "companies", key = "#companyId")
    public MessageResponse deleteCompany(CustomUserDetails customUserDetails,Long companyId) {
        Company company = getAdminAccessibleCompany(customUserDetails, companyId);
        company.delete();
        return MessageResponse.from("삭제 완료되었습니다.");
    }

    @Override
    public List<CompanyResponse> searchCompany(String companyName) {
        List<CompanyResponse> companyResponses = new ArrayList<>();
        List<Company> companies = companyUserRepository.getSearchCompany(companyName);
        for (Company company : companies) {
            companyResponses.add(CompanyResponse.from(company));
        }
        return companyResponses;
    }

    /**
     * 읽기 작업을 위한 접근 가능한 회사를 반환합니다.
     * Admin 사용자 또는 해당 회사에 소속된 사용자만 접근 가능합니다.
     *
     * @param customUserDetails 사용자 정보
     * @param companyId         접근하려는 회사 ID
     * @return 접근 가능한 회사 엔티티
     */
    private Company getReadableCompany(CustomUserDetails customUserDetails, Long companyId) {
        if (isAdmin(customUserDetails.getAuthorities().toString())) {
            return validateCompanyNotDeleted(findCompany(companyId));
        } else {
            return verifyUserAffiliation(customUserDetails.userId(), companyId);
        }
    }

    /**
     * 읽기 작업을 위한 Admin 접근 가능한 회사를 반환합니다.
     * 회사에 소속된 Admin 사용자만 접근 가능합니다.
     *
     * @param customUserDetails 사용자 정보
     * @param companyId         접근하려는 회사 ID
     * @return 접근 가능한 회사 엔티티
     */
    private Company getAdminAccessibleCompany(CustomUserDetails customUserDetails, Long companyId) {
        if (isAdmin(customUserDetails.getAuthorities().toString())) {
            return validateCompanyNotDeleted(findCompany(companyId));
        } else {
            return verifyAdminAffiliation(customUserDetails.userId(), companyId);
        }
    }

    /**
     * Admin 사용자의 소속을 확인하고 회사를 반환합니다.
     *
     * @param userId 사용자 정보
     * @param companyId         회사 ID
     * @return Admin이 소속된 회사 엔티티
     */
    private Company verifyAdminAffiliation(Long userId, Long companyId) {
        Company company = companyUserRepository.getCompanyUserAdmin(userId, companyId);
        if (company == null) {
            throw new IllegalAccessError("권한이 없습니다.");
        }
        return company;
    }

    /**
     * 사용자가 특정 회사에 소속되어 있는지 확인하고 회사를 반환합니다.
     *
     * @param userId 사용자 정보
     * @param companyId         회사 ID
     * @return 소속된 회사 엔티티
     */
    private Company verifyUserAffiliation(Long userId, Long companyId) {
        Company company = companyUserRepository.getCompanyUser(userId, companyId);
        if (company == null) {
            throw new IllegalAccessError("권한이 없습니다.");
        }
        return company;
    }

    /**
     * 사용자가 Admin 권한을 가지고 있는지 확인합니다.
     *
     * @param role 사용자 정보
     * @return Admin 여부
     */

    private boolean isAdmin(String role) {
        return role.equals("ADMIN");
    }

    /**
     * 회사가 존재하는지 확인하고 반환합니다.
     *
     * @param companyId 회사 ID
     * @return 회사 엔티티
     */
        public Company findCompany(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID로 회사를 찾을 수 없습니다: " + companyId));
    }

    /**
     * 회사가 삭제되지 않았는지 검증합니다.
     *
     * @param company 회사 엔티티
     * @return 삭제되지 않은 회사 엔티티
     */
    private Company validateCompanyNotDeleted(Company company) {
        if (company.isDeleted()) {
            throw new NoSuchElementException("삭제된 회사입니다: " + company.getCompanyId());
        }
        return company;
    }
}
