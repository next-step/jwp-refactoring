package kitchenpos.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup {
	private static final List<String> TABLE_NAMES = Arrays.asList(
		"orders",
		"order_line_item",
		"menu",
		"menu_group",
		"menu_product",
		"order_table",
		"table_group",
		"product"
	);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public void execute() {
		jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
		TABLE_NAMES.forEach(tableName -> jdbcTemplate.execute("TRUNCATE TABLE " + tableName));
		jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
	}
}
