package kitchenpos.ui;


import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Product;

@Transactional
@SpringBootTest
class ProductRestControllerTest {

	@Autowired
	private ProductRestController productRestController;

	@Test
	@DisplayName("상품 생성 성공 테스트")
	public void createProductSuccessTest() {
		//given
		Product product = new Product(null, "로제치킨", new BigDecimal(25000));

		//when
		ResponseEntity<Product> productResponseEntity = productRestController.create(product);

		//then
		assertThat(productResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(productResponseEntity.getHeaders().getLocation().toString()).isEqualTo("/api/products/7");
	}

	@Test
	@DisplayName("상품가격이 0보다 작아서 생성 실패 테스트")
	public void createProductFailTest() {
		//given
		Product product = new Product(null, "로제치킨", new BigDecimal(-1));

		//when
		//then
		assertThatThrownBy(() -> productRestController.create(product))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("상품 목록 조회 테스트")
	public void findAllProductListSuccessTest() {
		//given
		//when
		ResponseEntity<List<Product>> responseEntity = productRestController.list();

		//then
		assertThat(responseEntity.getBody().size()).isEqualTo(6);
	}
}
