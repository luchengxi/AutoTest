package com.course.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidConfig {
    @ConfigurationProperties(prefix ="spring.datasource")
    @Bean
    public DataSource druid(){
        return new DruidDataSource();
    }

    //配置druid的监控
    //配置一个监控的servlet
    @Bean
    public ServletRegistrationBean staViewServlet(){
        ServletRegistrationBean bean= new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        Map<String,String> initParm = new HashMap<>();
        initParm.put("loginUsername","admin");
        initParm.put("loginPassword","123");
        //默认允许所有访问
        initParm.put("allow","");
        bean.setInitParameters(initParm);
        return bean;
    }
    //配置一个web监控的filter
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        Map<String,String> initParm = new HashMap<>();
        initParm.put("exclusions","*.js,*.css,/druid/*");
        bean.setInitParameters(initParm);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }
}
