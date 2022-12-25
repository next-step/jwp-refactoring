package ktichenpos.menu.domain;

import static ktichenpos.menu.generator.ProductGenerator.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ktichenpos.menu.menu.domain.MenuProduct;
import ktichenpos.menu.menu.domain.Price;
import ktichenpos.menu.menu.domain.Product;
import ktichenpos.menu.menu.domain.Quantity;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {

	@Test
	@DisplayName("메뉴 상품 생성")
	void createMenuProductTest() {
		// given
		Product 후라이드_치킨 = 후라이드_치킨();
		Quantity 수량 = Quantity.from(1L);

		// when, then
		assertThatNoException()
			.isThrownBy(() -> MenuProduct.of(후라이드_치킨, 수량));
	}

	@Test
	@DisplayName("메뉴 상품 생성 - 상품이 없으면 예외 발생")
	void createMenuProductWithNullProductTest() {
		// given
		Product 후라이드_치킨 = null;
		Quantity 수량 = Quantity.from(1L);

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> MenuProduct.of(후라이드_치킨, 수량))
			.withMessage("상품은 필수입니다.");
	}

	@Test
	@DisplayName("메뉴 상품 생성 - 수량이 없으면 예외 발생")
	void createMenuProductWithNullQuantityTest() {
		// given
		Product 후라이드_치킨 = 후라이드_치킨();
		Quantity 수량 = null;

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> MenuProduct.of(후라이드_치킨, 수량))
			.withMessage("수량은 필수입니다.");
	}

	@Test
	@DisplayName("수량 * 가격 = 금액")
	void calculatePriceTest() {
		// given
		Product 후라이드_치킨 = 후라이드_치킨();
		Quantity 수량 = Quantity.from(2L);
		MenuProduct menuProduct = MenuProduct.of(후라이드_치킨, 수량);

		// when
		Price price = menuProduct.price();

		// then
		Assertions.assertThat(price).isEqualTo(Price.from(BigDecimal.valueOf(20)));
	}
}
