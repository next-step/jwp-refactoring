package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.ui.ProductRestController;

@SpringBootTest
@Sql({"/cleanup.sql", "/db/migration/V1__Initialize_project_tables.sql", "/db/migration/V2__Insert_default_data.sql",
	"/db/migration/V3__remove_two_way.sql"})
class ProductRestControllerTest {

	@Autowired
	ProductRestController productRestController;

	@Test
	void create() {
		// given
		ProductRequest 새_상품_요청 = new ProductRequest("새_상품", BigDecimal.valueOf(4000L));

		// when
		ProductResponse response = productRestController.create(새_상품_요청).getBody();

		// then
		assertAll(
			() -> assertThat(response.getId()).isNotZero(),
			() -> assertThat(response.getName()).isEqualTo("새_상품"),
			() -> assertThat(response.getPrice()).isEqualTo(4000L)
		);
	}

	@Test
	void list() {
		// given
		// @see V2__Insert_default_data.sql

		// when
		List<ProductResponse> listResponse = productRestController.list().getBody();

		// then
		assertThat(listResponse)
			.hasSize(6)
			.map(ProductResponse::getName)
			.contains("후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
	}
}
