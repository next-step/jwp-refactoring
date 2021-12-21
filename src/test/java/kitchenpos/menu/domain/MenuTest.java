package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupName;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;

@DisplayName("메뉴")
class MenuTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		MenuName name = MenuName.of("후라이드+후라이드");
		Price price = Price.of(BigDecimal.valueOf(25000));
		MenuGroup menuGroup = MenuGroup.of(MenuGroupName.of("추천메뉴"));
		MenuProducts menuProducts = MenuProducts.of(Collections.singletonList(
			MenuProduct.of(
				Product.of(
					ProductName.of("후라이드치킨"),
					Price.of(BigDecimal.valueOf(17000))),
				Quantity.of(2L))));

		// when
		Menu menu = Menu.of(name, price, menuGroup, menuProducts);

		// then
		assertAll(
			() -> assertThat(menu).isNotNull(),
			() -> assertThat(menu.getName()).isEqualTo(name),
			() -> assertThat(menu.getPrice()).isEqualTo(price),
			() -> assertThat(menu.getMenuGroup()).isEqualTo(menuGroup),
			() -> assertThat(menu.getMenuProducts()).isEqualTo(menuProducts)
		);
	}

	@DisplayName("생성 실패 - 메뉴의 가격이 메뉴 상품들의 전체 가격보다 큰 경우")
	@Test
	void ofFailOnPriceInvalid() {
		// given
		MenuName name = MenuName.of("후라이드+후라이드");
		Price price = Price.of(BigDecimal.valueOf(100000));
		MenuGroup menuGroup = MenuGroup.of(MenuGroupName.of("추천메뉴"));
		MenuProducts menuProducts = MenuProducts.of(Collections.singletonList(
			MenuProduct.of(
				Product.of(
					ProductName.of("후라이드치킨"),
					Price.of(BigDecimal.valueOf(17000))),
				Quantity.of(2L))));

		// when
		ThrowingCallable throwingCallable = () -> Menu.of(name, price, menuGroup, menuProducts);

		// when & then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}
}
