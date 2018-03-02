package com.eqdushu.server.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.eqdushu.server.domain.user.User;
import java.util.List;

@Repository
public interface UserMapper {
	public Long create(User user);

	public User getById(Long id);

	public void update(User user);

	public void deleteById(Long id);
	
	public List<User> listByIds(@Param("ids")List<Long> ids);
	
	public User getByTypeAndName(@Param("type") String type, @Param("name") String name);
	
	public User getByTypeAndPhone(@Param("type") String type, @Param("phone") String phone);
	
	public User getByTypeAndEmail(@Param("type") String type, @Param("email") String email);
	
	public List<User> listByCompanyId(@Param("companyId") Long companyId);

	int updateCompanyNull(Long id);

	int getAdminNum(@Param("companyId") String companyId);
}
