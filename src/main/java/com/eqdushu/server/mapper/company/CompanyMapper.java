package com.eqdushu.server.mapper.company;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.eqdushu.server.domain.company.Company;
import java.util.List;

@Repository
public interface CompanyMapper {
	public void create(Company company);

	public Company getById(Long companyId);
	
	public Company getByCode(@Param("code") String code);

	public void update(Company company);

	public List<Company> listByIds(@Param("companyIds")List<Long> companyIds);

}
