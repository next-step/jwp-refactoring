package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

	@DisplayName("상품객체를 생성한다")
	@Test
	void create() {
		// given // when
		Product product = Product.of("서비스", 0);

		// then
		assertThat(product).isNotNull();
	}

	@DisplayName("상품금액이 0미만은 상품객체를 생성할 수 없다.")
	@Test
	void errorCreateMinusPrice() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Product.of("치킨", -20000))
			.withMessage("가격은 0원 이상이어야 합니다.");
	}
}
