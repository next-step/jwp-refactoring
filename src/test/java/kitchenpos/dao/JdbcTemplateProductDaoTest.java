package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import kitchenpos.domain.Product;

class JdbcTemplateProductDaoTest {
	private static final String NAME = "로제치킨";
	private static final BigDecimal PRICE = new BigDecimal(33000);

	private static DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
		.addScript("classpath:db/migration/V1__Initialize_project_tables.sql")
		.addScript("classpath:db/migration/V2__Insert_default_data.sql")
		.build();
	private static JdbcTemplateProductDao jdbcTemplateProductDao = new JdbcTemplateProductDao(dataSource);


	@Test
	@DisplayName("상품 저장 테스트")
	public void saveProductTest() {
		//given
		Product product = new Product(null, NAME, PRICE);
		//when
		Product save = jdbcTemplateProductDao.save(product);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(7L);
		assertThat(save.getName()).isEqualTo(NAME);
	}

	@Test
	@DisplayName("상품 목록 조회 테스트")
	public void findAllProductListTest() {
		//given

		//when
		List<Product> productList = jdbcTemplateProductDao.findAll();

		//then
		assertThat(productList).hasSizeGreaterThanOrEqualTo(6);
	}

}
