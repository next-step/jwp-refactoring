package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

	@DisplayName("상품객체를 생성한다")
	@Test
	void create() {
		Product product = new Product("상품", new BigDecimal(0));

		//then
		assertThat(product).isNotNull();
	}

	@DisplayName("상품금액이 0미만 또는 Null인 경우 상품객체를 생성할 수 없다.")
	@Test
	void createWithUnderZeroPrice() {
		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> new Product("상품", null))
			  .withMessage("상품금액은 0원 이상이어야 합니다.");
	}
}
