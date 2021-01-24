package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.product.domain.Product;
import kitchenpos.product.ui.ProductRestController;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql"})
class ProductRestControllerTest {

	@Autowired
	ProductRestController productRestController;

	@Test
	void create() {
		// given
		Product 새_상품 = new Product();
		새_상품.setPrice(BigDecimal.valueOf(4000L));
		새_상품.setName("새_상품");

		// when
		Product createdProduct = productRestController.create(새_상품).getBody();

		// then
		assertAll(
			() -> assertThat(createdProduct.getId()).isNotZero(),
			() -> assertThat(createdProduct.getName()).isEqualTo("새_상품"),
			() -> assertThat(createdProduct.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(4000L))
		);
	}

	@Test
	void list() {
		// given
		// @see V2__Insert_default_data.sql

		// when
		List<Product> productList = productRestController.list().getBody();

		// then
		assertThat(productList)
			.hasSize(6)
			.map(Product::getName)
			.contains("후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
	}
}
