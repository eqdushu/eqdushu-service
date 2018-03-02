package com.eqdushu.server.service.company;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eqdushu.server.domain.company.Company;
import com.eqdushu.server.mapper.company.CompanyMapper;

@Service
public class CompanyServiceImpl implements ICompanyService {

	@Autowired
	private CompanyMapper companyMapper;

	@Override
	public void create(Company company) {
		company.setCreateTime(new Date());
		companyMapper.create(company);
	}

	@Override
	public Company getById(Long companyId) {
		return companyMapper.getById(companyId);
	}
	
	@Override
	public Company getByCode(String code) {
		return companyMapper.getByCode(code);
	}

	@Override
	public void update(Company company) {
		company.setUpdateTime(new Date());
		companyMapper.update(company);
	}

	@Override
	public List<Company> listByIds(List<Long> companyIds) {
		return companyMapper.listByIds(companyIds);
	}

}
