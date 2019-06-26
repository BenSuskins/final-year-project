package uk.co.suskins.hrvsm.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import uk.co.suskins.hrvsm.api.Config.HSSMConfig;
import uk.co.suskins.hrvsm.api.Config.SwaggerConfig;

@SpringBootApplication
@Import({SwaggerConfig.class, HSSMConfig.class})
public class HRVSMApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HRVSMApiApplication.class);
    }
}
