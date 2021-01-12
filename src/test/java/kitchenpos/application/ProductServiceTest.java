package kitchenpos.application;

import static kitchenpos.domain.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;

@SpringBootTest
public class ProductServiceTest {
	@Autowired
	private ProductService productService;

	@Test
	@DisplayName("상품을 등록할 수 있다.")
	void create() {
		//given
		ProductRequest productRequest = new ProductRequest(상품_신규_NAME, 상품_신규_PRICE);

		//when
		ProductResponse result = productService.create(productRequest);

		//then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getName()).isEqualTo(상품_신규_NAME);
		assertThat(result.getPrice()).isEqualByComparingTo(상품_신규_PRICE);
	}

	@Test
	@DisplayName("상품 등록 시, 상품의 가격이 없으면 IllegalArgumentException을 throw 해야한다.")
	void createPriceNull() {
		//when-then
		assertThatThrownBy(() -> productService.create(new ProductRequest(상품_신규_NAME, null)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("상품 등록 시, 상품의 가격이 0 원 미만이면 IllegalArgumentException을 throw 해야한다.")
	void createPriceLessThanZero() {
		//when-then
		assertThatThrownBy(() -> productService.create(new ProductRequest(상품_신규_NAME, BigDecimal.valueOf(-200))))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
