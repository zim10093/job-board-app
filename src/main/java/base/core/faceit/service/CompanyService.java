package base.core.faceit.service;

import base.core.faceit.model.Company;

public interface CompanyService {
    Company save(Company company);

    Company findByName(String name);
}
