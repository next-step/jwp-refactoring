package kitchenpos;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Sql("classpath:truncate.sql")
@ActiveProfiles("test")
@SpringBootTest
public class ServiceTest {
}
