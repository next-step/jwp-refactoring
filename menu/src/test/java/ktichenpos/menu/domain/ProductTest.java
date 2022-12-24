package ktichenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Price;
import ktichenpos.menu.menu.domain.Product;

@DisplayName("상품 테스트")
class ProductTest {

	@Test
	@DisplayName("상품 생성")
	void createProductTest() {
		assertThatNoException()
			.isThrownBy(() -> Product.of("후라이드_치킨", Price.from(BigDecimal.valueOf(10000))));
	}

	@Test
	@DisplayName("상품 생성 - 이름이 없으면 예외")
	void createProductWithNllNameTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Product.of(null, Price.from(BigDecimal.TEN)))
			.withMessage("이름은 필수입니다.");
	}

	@Test
	@DisplayName("상품 생성 - 가격이 없으면 예외")
	void createProductWithNullPriceTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Product.of("후라이드_치킨", null))
			.withMessage("가격은 필수입니다.");
	}
}
