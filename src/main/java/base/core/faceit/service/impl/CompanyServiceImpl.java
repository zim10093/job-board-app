package base.core.faceit.service.impl;

import base.core.faceit.model.Company;
import base.core.faceit.repository.CompanyRepository;
import base.core.faceit.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company findByName(String name) {
        return companyRepository.findByName(name);
    }
}
