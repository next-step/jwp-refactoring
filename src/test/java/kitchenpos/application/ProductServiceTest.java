package kitchenpos.application;

import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@DisplayName("상품을 생성한다.")
	@ParameterizedTest
	@ValueSource(strings = {"요플레", "우유"})
	void create(String name) {
		// given
		final BigDecimal price = new BigDecimal(1000);
		ProductRequest request = new ProductRequest(name, price);

		// when
		ProductResponse response = productService.create(request);

		// then
		assertThat(response.getId()).isNotNull();
		assertThat(response.getName()).isEqualTo(name);
		assertThat(response.getPrice().longValue()).isEqualTo(price.longValue());
	}

	@DisplayName("잘못된 가격으로 상품 생성시 실패.")
	@ParameterizedTest
	@ValueSource(longs = {-1, -999999999})
	void create_PriceException(long price) {
		// given
		ProductRequest productRequest = new ProductRequest("제품", new BigDecimal(price));

		// when then
		assertThatThrownBy(() -> productService.create(productRequest)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품 리스트를 반환한다.")
	@Test
	void list() {
		// given
		create("요플레");
		create("우유");

		// when then
		assertThat(productService.list())
				.map(ProductResponse::getName)
				.contains("요플레", "우유");
	}
}
