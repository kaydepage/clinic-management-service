package au.edu.rmit.sept.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
public class WebappApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebappApplication.class, args);
    }
    @Bean
    public FilterRegistrationBean<HiddenHttpMethodFilter> registerHiddenHttpMethodFilter() {
        FilterRegistrationBean<HiddenHttpMethodFilter> filterRegistrationBean = new FilterRegistrationBean<>(new HiddenHttpMethodFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}