package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Quantity;

class MenuProductsTest {

	@Test
	@DisplayName("메뉴상품들 생성")
	public void MenuProductsTest() {
		//given
		MenuProduct menuProduct = new MenuProduct(1L, Quantity.valueOf(2L));

		//when
		MenuProducts menuProducts = new MenuProducts(Lists.newArrayList(menuProduct));
		//then
		assertThat(menuProducts).isNotNull();
		assertThat(menuProducts.value()).hasSize(1);
	}

	@Test
	@DisplayName("메뉴상품들에 메뉴 등록하기")
	public void setMenu() {
		//given
		MenuProduct menuProduct = new MenuProduct(1L, Quantity.valueOf(2L));
		Menu menu = new Menu(1L);

		//when
		MenuProducts menuProducts = new MenuProducts(Lists.newArrayList(menuProduct)).setMenu(menu);
		//then
		assertThat(menuProducts.value().get(0).getMenuId()).isEqualTo(1L);
	}

}
