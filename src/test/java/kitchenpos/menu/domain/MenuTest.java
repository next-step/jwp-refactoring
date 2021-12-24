package kitchenpos.menu.domain;

import static kitchenpos.menugroup.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

@DisplayName("메뉴")
class MenuTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		Name name = Name.from("후라이드+후라이드");
		Price price = Price.from(BigDecimal.valueOf(25000));
		MenuGroup menuGroup = 추천_메뉴_그룹();
		MenuProducts menuProducts = MenuProducts.from(Collections.singletonList(
			MenuProduct.of(
				Product.of(
					Name.from("후라이드치킨"),
					Price.from(BigDecimal.valueOf(17000))),
				Quantity.from(2L))));

		// when
		Menu menu = Menu.of(name, price, menuGroup.getId(), menuProducts);

		// then
		assertAll(
			() -> assertThat(menu).isNotNull(),
			() -> assertThat(menu.getName()).isEqualTo(name),
			() -> assertThat(menu.getPrice()).isEqualTo(price),
			() -> assertThat(menu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
			() -> assertThat(menu.getMenuProducts()).isEqualTo(menuProducts)
		);
	}

	@DisplayName("생성 실패 - 메뉴의 가격이 메뉴 상품들의 전체 가격보다 큰 경우")
	@Test
	void ofFailOnPriceInvalid() {
		// given
		Name name = Name.from("후라이드+후라이드");
		Price price = Price.from(BigDecimal.valueOf(100000));
		MenuGroup menuGroup = 추천_메뉴_그룹();
		MenuProducts menuProducts = MenuProducts.from(Collections.singletonList(
			MenuProduct.of(
				Product.of(
					Name.from("후라이드치킨"),
					Price.from(BigDecimal.valueOf(17000))),
				Quantity.from(2L))));

		// when
		ThrowingCallable throwingCallable = () -> Menu.of(name, price, menuGroup.getId(), menuProducts);

		// when & then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}
}
