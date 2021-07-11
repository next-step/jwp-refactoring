package kitchenpos.menu.domain;

import static java.util.Arrays.*;
import static kitchenpos.product.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductTest;

class MenuTest {

	@DisplayName("가격이 음수인 메뉴는 생성 될 수 없다.")
	@Test
	void negativePriceTest() {
		MenuProduct 후라이드치킨2개 = new MenuProduct(1L, 2);
		Product 후라이드치킨 = ProductTest.createProduct(1L, "후라이드치킨", 1000);

		assertThatThrownBy(() -> new Menu.Builder()
			.name(Name.valueOf("치킨"))
			.price(Price.wonOf(-1))
			.menuGroupId(new MenuGroupId(1L))
			.menuProducts(MenuProducts.of(asList(후라이드치킨2개)))
			.products(asList(후라이드치킨))
			.build()
		).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("가격은 음수가 될 수 없습니다.");
	}

	@DisplayName("메뉴의 가격이 메뉴와 연결된 상품의 수량 * 가격 보다 비쌀 수 없습니다.")
	@Test
	void moreExpensiveThanMenuProductsTest() {
		MenuProduct 후라이드치킨2개 = new MenuProduct(1L, 2);
		MenuProduct 피자3개 = new MenuProduct(2L, 3);
		Product 후라이드치킨 = ProductTest.createProduct(1L, "후라이드치킨", 1000);
		Product 피자 = ProductTest.createProduct(2L, "피자", 2000);

		assertThatThrownBy(() ->
			new Menu.Builder()
				.name(Name.valueOf("치킨"))
				.price(Price.wonOf(8001))
				.menuGroupId(new MenuGroupId(1L))
				.menuProducts(MenuProducts.of(asList(후라이드치킨2개, 피자3개)))
				.products(asList(후라이드치킨, 피자))
				.build()
		)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴의 가격이 메뉴와 연결된 상품의 수량 * 가격 보다 비쌀 수 없습니다.");
	}

	@DisplayName("메뉴 이름, 가격, 메뉴 그룹, 메뉴상품 리스트, 상품 리스르들로 메뉴를 만들 수 있다")
	@Test
	void createBuilderTest() {
		// given
		MenuProduct 후라이드치킨1개 = new MenuProduct(1L, 1);
		Product 후라이드치킨 = createProduct(1L, "후라이드치킨", 1000);

		// when
		Menu menu = new Menu.Builder()
			.name(Name.valueOf("치킨_피자_세트"))
			.price(Price.wonOf(1000))
			.menuGroupId(new MenuGroupId(1L))
			.menuProducts(MenuProducts.of(asList(후라이드치킨1개)))
			.products(asList(후라이드치킨))
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
		MenuProducts menuProducts = MenuProducts.of(asList(new MenuProduct(1L, 1)));

		// when then
		assertAll(
			() -> verifyThrowException(null, price, menuGroupId, menuProducts, new ArrayList<>()),
			() -> verifyThrowException(name, null, menuGroupId, menuProducts, new ArrayList<>()),
			() -> verifyThrowException(name, price, null, menuProducts, new ArrayList<>()),
			() -> verifyThrowException(name, price, menuGroupId, null, new ArrayList<>()),
			() -> verifyThrowException(name, price, menuGroupId, menuProducts, null)
		);
	}

	void verifyThrowException(Name name, Price price, MenuGroupId menuGroupId, MenuProducts menuProducts, List<Product> products) {
		assertThatThrownBy(() ->
				new Menu.Builder()
					.name(name)
					.price(price)
					.menuGroupId(menuGroupId)
					.menuProducts(menuProducts)
					.build()
			, "메뉴상품리스트가 없는 경우")
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴의 필수정보가 부족합니다.");
	}
}