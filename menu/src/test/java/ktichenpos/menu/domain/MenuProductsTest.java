package ktichenpos.menu.domain;

import static ktichenpos.menu.generator.ProductGenerator.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import ktichenpos.menu.menu.domain.MenuProduct;
import ktichenpos.menu.menu.domain.MenuProducts;

@DisplayName("메뉴 상품들 테스트")
class MenuProductsTest {

	@Test
	@DisplayName("메뉴 상품들 생성")
	void createMenuProductsTest() {
		assertThatNoException()
			.isThrownBy(() -> MenuProducts.from(
				Collections.singletonList(MenuProduct.of(후라이드_치킨(), Quantity.from(1L)))
			));
	}

	@Test
	@DisplayName("메뉴 상품 리스트가 없으면 예외 발생")
	void createMenuProductsWithNullTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> MenuProducts.from(null))
			.withMessage("메뉴 상품 리스트는 필수입니다.");
	}

	@Test
	@DisplayName("메뉴 상품 리스트에 null이 포함되면 예외 발생")
	void createMenuProductsWithNullInListTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> MenuProducts.from(Collections.singletonList(null)))
			.withMessageEndingWith("null이 포함될 수 없습니다.");
	}

	@Test
	@DisplayName("메뉴 상품들의 합산 금액")
	void totalPriceTest() {
		//given
		MenuProduct menuProduct1 = MenuProduct.of(후라이드_치킨(), Quantity.from(2L));
		MenuProduct menuProduct2 = MenuProduct.of(양념_치킨(), Quantity.from(3L));

		//when
		Price totalPrice = MenuProducts.from(Arrays.asList(menuProduct1, menuProduct2)).totalPrice();

		//then
		assertThat(totalPrice).isEqualTo(Price.from(BigDecimal.valueOf(50)));
	}
}
