package com.eqdushu.server.service.user;

import java.util.List;
import com.eqdushu.server.domain.user.User;
import com.eqdushu.server.utils.EnumHolder.SceneType;

public interface IUserService {
	public User create(User user);
	
	public User getById(Long id);

	public User update(User user);

	public void deleteById(Long id);
	
	public List<User> listByIds(List<Long> ids);
	
	public User getByTypeAndScene(String type,SceneType sceneType,String account);
	
	public List<User> listByCompanyId(Long companyId);

	public int updateCompanyNull(Long id);

	public int getAdminNum(Long companyId);
}
