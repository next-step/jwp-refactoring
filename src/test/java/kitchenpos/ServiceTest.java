package kitchenpos;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql("classpath:truncate.sql")
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ServiceTest {
}
