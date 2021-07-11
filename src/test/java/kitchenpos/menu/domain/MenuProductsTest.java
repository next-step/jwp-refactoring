package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

	@DisplayName("메뉴상품 리스트의 없다면, 비어있는 리스트가 저장된다.")
	@Test
	void createWithNullTest() {
		MenuProducts menuProducts = MenuProducts.of(null);

		assertThat(menuProducts.getMenuProducts()).isNotNull();
		assertThat(menuProducts.getMenuProducts()).hasSize(0);
	}
}