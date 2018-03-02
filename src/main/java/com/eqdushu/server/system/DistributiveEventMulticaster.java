package com.eqdushu.server.system;


import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class DistributiveEventMulticaster implements ApplicationEventMulticaster{

    private ApplicationEventMulticaster asyncEventMulticaster;

    private ApplicationEventMulticaster syncEventMulticaster;

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        if(listener.getClass().getAnnotation(AsyncListener.class) != null){
            asyncEventMulticaster.addApplicationListener(listener);
        }else{
            syncEventMulticaster.addApplicationListener(listener);
        }
    }

    @Override
    public void addApplicationListenerBean(String listenerBeanName) {

    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        if(listener.getClass().getAnnotation(AsyncListener.class) != null){
            asyncEventMulticaster.removeApplicationListener(listener);
        }else{
            syncEventMulticaster.removeApplicationListener(listener);
        }
    }

    @Override
    public void removeApplicationListenerBean(String listenerBeanName) {

    }

    @Override
    public void removeAllListeners() {
        asyncEventMulticaster.removeAllListeners();
        syncEventMulticaster.removeAllListeners();

    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        syncEventMulticaster.multicastEvent(event);
        asyncEventMulticaster.multicastEvent(event);
    }

    public void setAsyncEventMulticaster(ApplicationEventMulticaster asyncEventMulticaster) {
        this.asyncEventMulticaster = asyncEventMulticaster;
    }

    public void setSyncEventMulticaster(ApplicationEventMulticaster syncEventMulticaster) {
        this.syncEventMulticaster = syncEventMulticaster;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static  @interface AsyncListener {
    }
}
