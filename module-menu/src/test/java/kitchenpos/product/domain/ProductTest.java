package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Price;

class ProductTest {

	@Test
	@DisplayName("상품 생성 테스트")
	public void createProductTest() {
		//when
		Product product = new Product("로제치킨", Price.valueOf(new BigDecimal(15000)));
		//then
		assertThat(product).isNotNull();
		assertThat(product).isEqualTo( new Product("로제치킨", Price.valueOf(new BigDecimal(15000))));
	}

	@Test
	@DisplayName("상품 가격정보가 없어서 생성 실패")
	public void createProductFailPriceIsNullTest() {
		//when
		assertThatThrownBy(() -> new Product("로제치킨", Price.valueOf(null)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("가격은 0보다 작을 수 없습니다");
	}

	@Test
	@DisplayName("상품 가격이 0보다 작아서 생성 살패")
	public void createProductFailPriceLessThanZeroTest() {
		//when
		assertThatThrownBy(() -> new Product("로제치킨", Price.valueOf(new BigDecimal(-1))))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("가격은 0보다 작을 수 없습니다");
	}

}
