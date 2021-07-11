package kitchenpos.menu.domain;

import static java.util.Arrays.*;
import static kitchenpos.menu.domain.TextFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;

class MenuTest {

	@DisplayName("가격이 음수인 메뉴는 생성 될 수 없다.")
	@Test
	void negativePriceTest() {
		assertThatThrownBy(() -> Menu.create(
			Name.valueOf("치킨"),
			Price.wonOf(-1),
			new MenuGroupId(1L),
			MenuProducts.of(asList(후라이드치킨2개, 피자3개))))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("가격은 음수가 될 수 없습니다.");
	}

	@DisplayName("메뉴의 가격이 메뉴와 연결된 상품의 수량 * 가격 보다 비쌀 수 없습니다.")
	@Test
	void moreExpensiveThanMenuProductsTest() {
		assertThatThrownBy(() -> Menu.create(
			Name.valueOf("치킨_피자_세트"),
			Price.wonOf(8001),
			new MenuGroupId(1L),
			MenuProducts.of(asList(후라이드치킨2개, 피자3개))))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴의 가격이 메뉴와 연결된 상품의 수량 * 가격 보다 비쌀 수 없습니다.");
	}

	@DisplayName("메뉴 이름, 가격, 메뉴 그룹, 메뉴상품 리스트 들로 메뉴를 만들 수 있다")
	@Test
	void createBuilderTest() {
		// given
		MenuProduct 후라이드치킨1개 = new MenuProduct(후라이드치킨, 1);

		// when
		Menu menu = new Menu.Builder()
			.name(Name.valueOf("치킨_피자_세트"))
			.price(Price.wonOf(1000))
			.menuGroupId(new MenuGroupId(1L))
			.menuProducts(MenuProducts.of(asList(후라이드치킨1개)))
			.build();

		// than
		assertThat(menu.getName()).isEqualTo(Name.valueOf("치킨_피자_세트"));
		assertThat(menu.getPrice()).isEqualTo(Price.wonOf(1000));
		assertThat(menu.getMenuGroupId()).isEqualTo(new MenuGroupId(1L));
		assertThat(menu.getMenuProducts()).containsExactly(후라이드치킨1개);
		assertThat(후라이드치킨1개.getMenu()).isEqualTo(menu);
	}

	@DisplayName("메뉴의 이름, 가격, 메뉴그룹, 메뉴상품리스트 는 필수정보이다.")
	@Test
	void createWithNullTest() {
		// given
		Name name = Name.valueOf("치킨");
		Price price = Price.wonOf(1000);
		MenuGroupId menuGroupId = new MenuGroupId(1L);
		MenuProducts menuProducts = MenuProducts.of(asList(new MenuProduct(후라이드치킨, 1)));

		// when then
		assertAll(
			() -> assertThatThrownBy(() -> Menu.create(name, price, menuGroupId, null), "메뉴상품리스트가 없는 경우")
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("메뉴의 이름, 가격, 메뉴그룹, 메뉴상품리스트 는 필수정보입니다."),
			() -> assertThatThrownBy(() -> Menu.create(name, price, null, menuProducts), "메뉴그룹이 없는 경우")
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("메뉴의 이름, 가격, 메뉴그룹, 메뉴상품리스트 는 필수정보입니다."),
			() -> assertThatThrownBy(() -> Menu.create(name, null, menuGroupId, menuProducts), "가격이 없는 경우")
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("메뉴의 이름, 가격, 메뉴그룹, 메뉴상품리스트 는 필수정보입니다."),
			() -> assertThatThrownBy(() -> Menu.create(null, price, menuGroupId, menuProducts), "이름이 없는 경우")
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("메뉴의 이름, 가격, 메뉴그룹, 메뉴상품리스트 는 필수정보입니다.")
		);
	}
}