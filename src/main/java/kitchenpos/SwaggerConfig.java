package kitchenpos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    private static final String TITLE = "KITCHENPOS_API_SPEC";
    private static final String DESCRIPTION = "next-step woowacourse mission";
    private static final String VERSION = "1.0";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title(TITLE)
            .description(DESCRIPTION)
            .version(VERSION)
            .build();
    }
}
