package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

class ProductTest {

	@DisplayName("가격이 음수인 상품은 생성 될 수 없다.")
	@Test
	void negativePriceTest() {
		// given
		// when
		// than
		assertThatThrownBy(() -> new Product(Name.valueOf("상품"), Price.wonOf(BigDecimal.valueOf(-1))))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("가격은 음수가 될 수 없습니다.");
	}

	@DisplayName("상품의 이름과 가격은 필수 정보이다.")
	@Test
	void createProductWithNullTest() {
		assertAll(
			() -> assertThatThrownBy(() -> new Product(Name.valueOf("상품"), null), "가격이 null 일 경우 상품이 생성될 수 없다.")
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("상품의 이름과 가격은 필수 정보입니다."),

			() -> assertThatThrownBy(() -> new Product(null,  Price.wonOf(1000)), "이름이 null 일 경우 상품이 생성될 수 없다.")
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("상품의 이름과 가격은 필수 정보입니다.")
		);
	}
}