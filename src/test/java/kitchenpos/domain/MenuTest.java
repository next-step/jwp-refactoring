package kitchenpos.domain;

import static java.util.Arrays.*;
import static kitchenpos.domain.MenuGroupTest.*;
import static kitchenpos.domain.MenuProductsTest.*;
import static kitchenpos.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

	@DisplayName("가격이 음수인 메뉴는 생성 될 수 없다.")
	@Test
	void negativePriceTest() {
		assertThatThrownBy(() -> Menu.create(
			Name.valueOf("치킨"),
			Price.wonOf(-1),
			치킨그룹,
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
			치킨그룹,
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
		Menu.Builder menuBuilder = new Menu.Builder();
		Menu menu = menuBuilder
			.name(Name.valueOf("치킨_피자_세트"))
			.price(Price.wonOf(1000))
			.menuGroup(치킨그룹)
			.menuProducts(MenuProducts.of(asList(후라이드치킨1개)))
			.build();

		// than
		assertThat(menu.getName()).isEqualTo(Name.valueOf("치킨_피자_세트"));
		assertThat(menu.getPrice()).isEqualTo(Price.wonOf(1000));
		assertThat(menu.getMenu()).isEqualTo(치킨그룹);
		assertThat(menu.getMenuProducts()).containsExactly(후라이드치킨1개);
		assertThat(후라이드치킨1개.getMenu()).isEqualTo(menu);
	}

	@DisplayName("메뉴의 이름, 가격, 메뉴그룹, 메뉴상품리스트 는 필수정보이다.")
	@Test
	void createWithNullTest() {
		// given
		Name name = Name.valueOf("치킨");
		Price price = Price.wonOf(1000);
		MenuGroup menuGroup = 치킨그룹;
		MenuProducts menuProducts = MenuProducts.of(asList(new MenuProduct(후라이드치킨, 1)));

		// when then
		assertAll(
			() -> assertThatThrownBy(() -> Menu.create(name, price, menuGroup, null), "메뉴상품리스트가 없는 경우")
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("메뉴의 이름, 가격, 메뉴그룹, 메뉴상품리스트 는 필수정보입니다."),
			() -> assertThatThrownBy(() -> Menu.create(name, price, null, menuProducts), "메뉴그룹이 없는 경우")
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("메뉴의 이름, 가격, 메뉴그룹, 메뉴상품리스트 는 필수정보입니다."),
			() -> assertThatThrownBy(() -> Menu.create(name, null, menuGroup, menuProducts), "가격이 없는 경우")
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("메뉴의 이름, 가격, 메뉴그룹, 메뉴상품리스트 는 필수정보입니다."),
			() -> assertThatThrownBy(() -> Menu.create(null, price, menuGroup, menuProducts), "이름이 없는 경우")
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("메뉴의 이름, 가격, 메뉴그룹, 메뉴상품리스트 는 필수정보입니다.")
		);
	}
}