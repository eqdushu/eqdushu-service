package com.eqdushu.server.system;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

public class AppContext implements ApplicationContextAware, ServletContextAware{

    private static AppContext _appContext ;

    private static ServletContext _servletContext;
    
    private static ApplicationContext _applicationContext;

    private AppContext() {
        _appContext = this;
    }


    public ServletContext getServletContext() {
        return _servletContext;
    }


    public ApplicationContext getApplicationContext() {
        return _applicationContext;
    }

    public static AppContext instance() {
        if( _applicationContext == null ||  _servletContext == null){
            throw new RuntimeException("AppContext initialize is incomplete!");
        }
        return _appContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        _applicationContext= applicationContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        _servletContext = servletContext;
    }

}
