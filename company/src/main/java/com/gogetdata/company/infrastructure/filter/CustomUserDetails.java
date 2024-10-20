package com.gogetdata.company.infrastructure.filter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final Long companyId;
    private final String companyType;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long userId, Collection<? extends GrantedAuthority> authorities,Long companyId, String companyType) {
        this.userId = userId;
        this.authorities = authorities;
        this.companyId = companyId;
        this.companyType = companyType;
    }
    public Long userId() {
        return this.userId;
    }
    public Long companyId() {
        return this.companyId;
    }
    public String getCompanyType() {
        return this.companyType;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
