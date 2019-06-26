package uk.co.suskins.hrvsm.api.Config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {"uk.co.suskins.hrvsm.*"})
@EntityScan(basePackages = {"uk.co.suskins.hrvsm.*"})
@EnableJpaRepositories(basePackages = {"uk.co.suskins.hrvsm.repository"})
@EnableAutoConfiguration
@PropertySource(value = {"classpath:application.yml"})
public class HSSMConfig {
    public HSSMConfig() {
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
