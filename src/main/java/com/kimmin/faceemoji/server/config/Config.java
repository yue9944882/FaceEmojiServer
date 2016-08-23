package com.kimmin.faceemoji.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.servlet.MultipartConfigElement;

import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.boot.context.embedded.MultipartConfigFactory;

/**
 * Created by kimmin on 7/19/16.
 */


@Configuration
public class Config {

    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory(EntityManagerFactory emf) {
        HibernateJpaSessionFactoryBean factory = new HibernateJpaSessionFactoryBean();
        factory.setEntityManagerFactory(emf);
        return factory;
    }
}
