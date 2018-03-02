package com.eqdushu.server.event;

import org.springframework.context.ApplicationEvent;

import com.eqdushu.server.domain.user.User;

public class RewardCreateEvent extends ApplicationEvent {

	private static final long serialVersionUID = 9233899281L;
	
	private User user; 

	public RewardCreateEvent(Object source,User user) {
		super(source);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
