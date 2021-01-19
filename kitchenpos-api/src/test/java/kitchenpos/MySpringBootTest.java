package kitchenpos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("local")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Transactional
@SpringBootTest
public @interface MySpringBootTest {
}
