package base.core.faceit.service;

import base.core.faceit.model.Company;
import java.util.Optional;

public interface CompanyService {
    Company save(Company company);

    Optional<Company> findByName(String name);
}
