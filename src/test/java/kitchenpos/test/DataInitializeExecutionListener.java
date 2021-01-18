package kitchenpos.test;

import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-08
 */
@Service
@ActiveProfiles("test")
public class DataInitializeExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        removeAllData(testContext);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        removeAllData(testContext);
    }


    private void removeAllData(TestContext testContext) throws Exception{
        ApplicationContext applicationContext = testContext.getApplicationContext();
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.execute("delete from menu_product");
        jdbcTemplate.execute("delete from menu");
        jdbcTemplate.execute("delete from menu_group");
        jdbcTemplate.execute("delete from order_table");
        jdbcTemplate.execute("delete from order_line_item");
        jdbcTemplate.execute("delete from orders");
        jdbcTemplate.execute("delete from table_group");
        jdbcTemplate.execute("delete from product");
    }
}
