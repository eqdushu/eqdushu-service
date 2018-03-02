package com.eqdushu.server.system;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;

public class AppInitializer implements ApplicationListener<ApplicationContextEvent> {
	
//	@Autowired
//	private QuestionTimeoutExecutor executor;

	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		if(event.getApplicationContext().getParent() == null){
//			if(event instanceof ContextRefreshedEvent){
//				executor.startAll(); //定期扫描locked队列与unclosed队列，更新超时的问题
//			}else if(event instanceof ContextClosedEvent){ 
//				executor.stopAll();
//			}
		}
	}

}
