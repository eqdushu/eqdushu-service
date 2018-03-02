package com.eqdushu.server.service.user;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eqdushu.server.domain.user.User;
import com.eqdushu.server.mapper.user.UserMapper;
import com.eqdushu.server.utils.CipherUtil;
import com.eqdushu.server.utils.EnumHolder.SceneType;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public User create(User user){
		user.setCreateTime(new Date());
		user.setDeleted(false);
		user.randomSalt();
		String name = user.getName();
		if(name==null){
			if(!StringUtils.isBlank(user.getPhone())){
				user.setName(user.getPhone());
			}else if(!StringUtils.isBlank(user.getEmail())){
				user.setName(user.getEmail());
			}else{
				return null;
			}
		}
		user.setPassword(CipherUtil.hash(user.getName()+user.getPassword()+user.getSalt()));//创建时生成MD5密码
		userMapper.create(user);
		return user;
	}

	@Override
	public User getById(Long id) {
		return userMapper.getById(id);
	}

	@Override
	/*
	 * password变更需特殊处理
	 * userType|channel等其他信息变更
	 * 
	 */
	public User update(User user) {//password|userType|channel
		/*String password = user.getPassword();
		if(!StringUtils.isBlank(password)){//更新密码时需额外生成salt
			user.randomSalt();
			user.setPassword(CipherUtil.hash(user.getName()+user.getPassword()+user.getSalt()));
		}*/
		user.setUpdateTime(new Date());
		userMapper.update(user);
        return user;
	}

	@Override
	public void deleteById(Long id) {
		userMapper.deleteById(id);
	}

	@Override
	public List<User> listByIds(List<Long> ids) {
		return userMapper.listByIds(ids);
	}

	@Override
	public User getByTypeAndScene(String type, SceneType sceneType,String account) {
		switch(sceneType){
		  case name: 
			  return userMapper.getByTypeAndName(type, account);
		  case email:
			  return userMapper.getByTypeAndEmail(type, account);
		  case phone:
			  return userMapper.getByTypeAndPhone(type, account);
	      default: 
	    	  return null;		  
		}
	}

	@Override
	public List<User> listByCompanyId(Long companyId) {
		return userMapper.listByCompanyId(companyId);
	}

	@Override
	public int updateCompanyNull(Long id) {
		return userMapper.updateCompanyNull(id);
	}

	@Override
	public int getAdminNum(Long companyId) {
		return 0;
	}

}
