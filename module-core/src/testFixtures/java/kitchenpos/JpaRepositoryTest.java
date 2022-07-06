package kitchenpos;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql("classpath:truncate.sql")
@ActiveProfiles("test")
@DataJpaTest
@Transactional
public class JpaRepositoryTest {
}
