package kitchenpos.menu.domain;

import static kitchenpos.generator.ProductGenerator.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Quantity;

@DisplayName("메뉴 테스트")
class MenuTest {

	@Test
	@DisplayName("메뉴 생성")
	void createMenuTest() {
		assertThatNoException()
			.isThrownBy(() -> Menu.of(
				"후라이드",
				Price.from(BigDecimal.TEN),
				MenuGroup.from("치킨"),
				MenuProducts.from(
					Collections.singletonList(MenuProduct.of(후라이드_치킨(),
						Quantity.from(1L)))))
			);
	}

	@Test
	@DisplayName("메뉴 생성 - 메뉴 이름이 null이면 예외 발생")
	void createMenuWithNullNameTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Menu.of(
				null,
				Price.from(BigDecimal.TEN),
				MenuGroup.from("치킨"),
				MenuProducts.from(
					Collections.singletonList(MenuProduct.of(후라이드_치킨(),
						Quantity.from(1L)))))
			)
			.withMessage("메뉴 이름은 필수입니다.");
	}

	@Test
	@DisplayName("메뉴 생성 - 메뉴 가격이 null이면 예외 발생")
	void createMenuWithNullPriceTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Menu.of(
				"후라이드",
				null,
				MenuGroup.from("치킨"),
				MenuProducts.from(
					Collections.singletonList(MenuProduct.of(후라이드_치킨(),
						Quantity.from(1L)))))
			)
			.withMessage("메뉴 가격은 필수입니다.");
	}

	@Test
	@DisplayName("메뉴 생성 - 메뉴 그룹이 null이면 예외 발생")
	void createMenuWithNullMenuGroupTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Menu.of(
				"후라이드",
				Price.from(BigDecimal.TEN),
				null,
				MenuProducts.from(
					Collections.singletonList(MenuProduct.of(후라이드_치킨(),
						Quantity.from(1L)))))
			)
			.withMessage("메뉴 그룹은 필수입니다.");
	}

	@Test
	@DisplayName("메뉴 생성 - 메뉴 상품이 null이면 예외 발생")
	void createMenuWithNullMenuProductsTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Menu.of(
				"후라이드",
				Price.from(BigDecimal.TEN),
				MenuGroup.from("치킨"),
				null
			))
			.withMessage("메뉴 상품은 필수입니다.");
	}

	@Test
	@DisplayName("메뉴 생성 - 메뉴 가격이 메뉴 상품의 가격 합보다 크면 예외 발생")
	void createMenuWithPriceGreaterThanMenuProductsPriceTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Menu.of(
				"후라이드",
				Price.from(BigDecimal.valueOf(30)),
				MenuGroup.from("치킨"),
				MenuProducts.from(
					Collections.singletonList(MenuProduct.of(후라이드_치킨(),
						Quantity.from(2L)))))
			)
			.withMessage("메뉴 가격은 메뉴 상품의 총 가격보다 작거나 같아야 합니다.");
	}
}
