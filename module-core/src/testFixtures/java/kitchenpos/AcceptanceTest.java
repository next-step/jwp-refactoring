package kitchenpos;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Sql("classpath:truncate.sql")
@ActiveProfiles("test")
public class AcceptanceTest extends RestAssuredTest {

}
