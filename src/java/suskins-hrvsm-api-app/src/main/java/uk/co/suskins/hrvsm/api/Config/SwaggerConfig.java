package uk.co.suskins.hrvsm.api.Config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public SwaggerConfig() {
    }

    @Bean
    public Docket api() {
        return (new Docket(DocumentationType.SWAGGER_2))
                .select()
                .apis(this.apis())
                .paths(this.paths())
                .build()
                .apiInfo(this.apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                this.title(), this.description(), "",
                "", new Contact("", "", ""), "", "", Collections.emptyList());
    }

    private Predicate<String> paths() {
        return PathSelectors.any();
    }

    private Predicate<RequestHandler> apis() {
        return RequestHandlerSelectors.basePackage("uk.co.suskins");
    }

    private String description() {
        return "Ben Suskins - BSc Computer Science Final Year Project 2018/19.";
    }

    private String title() {
        return "Extracting Knowledge about Hate Speech from Social Media";
    }

}
