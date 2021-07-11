package kitchenpos.config;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.StandardCharsets;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MockMvcTestConfig.Beans.class)
public @interface MockMvcTestConfig {
    class Beans {
        @Bean
        MockMvcBuilderCustomizer utf8Config() {
            return builder -> builder.addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true));
        }
    }
}
