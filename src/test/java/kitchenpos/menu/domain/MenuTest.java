package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {
	@DisplayName("메뉴 그룹 정보가 없으면 IllegalArgumentException 발생")
	@Test
	void createWhenNoMenuGroup() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Menu.builder().name("닭발").price(10000).build());
	}

	@DisplayName("메뉴 가격이 원래 상품 가격의 합보다 크면 IllegalArgumentException 발생")
	@Test
	void createWhenExpensive() {
		Product product = Product.builder().name("후라이드").price(11000).build();
		MenuProduct menuProduct = MenuProduct.builder().product(product).quantity(2).build();
		MenuGroup menuGroup = MenuGroup.builder().name("추천메뉴").build();

		Menu menu = Menu.builder()
			.name("후라이드2")
			.menuGroup(menuGroup)
			.price(40000)
			.build();

		assertThatIllegalArgumentException()
			.isThrownBy(() -> menu.setMenuProducts(Arrays.asList(menuProduct)));
	}

	@DisplayName("메뉴 생성")
	@Test
	void create() {
		Product product = Product.builder().name("후라이드").price(11000).build();
		MenuProduct menuProduct = MenuProduct.builder().product(product).quantity(2).build();
		MenuGroup menuGroup = MenuGroup.builder().name("추천메뉴").build();

		Menu menu = Menu.builder()
			.name("후라이드2")
			.menuGroup(menuGroup)
			.price(20000)
			.build();
		menu.setMenuProducts(Arrays.asList(menuProduct));

		assertThat(menu).isNotNull();
	}
}
