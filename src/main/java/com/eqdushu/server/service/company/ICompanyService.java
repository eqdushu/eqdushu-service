package com.eqdushu.server.service.company;

import java.util.List;
import com.eqdushu.server.domain.company.Company;

public interface ICompanyService {

	public void create(Company company);

	public Company getById(Long companyId);
	
	public Company getByCode(String code);

	public void update(Company company);

	public List<Company> listByIds(List<Long> companyIds);
}
