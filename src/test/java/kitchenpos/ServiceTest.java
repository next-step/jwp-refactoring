package kitchenpos;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql("/db/sql/truncate.sql")
@SpringBootTest
@Transactional
public class ServiceTest {

}
