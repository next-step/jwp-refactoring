package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Quantity;

class MenuProductTest {

	@Test
	@DisplayName("메뉴상품 생성 테스트")
	public void createMenuTest() {
		//when
		MenuProduct menuProduct = new MenuProduct(1L, null, null, Quantity.valueOf(2L));
		//then
		assertThat(menuProduct).isNotNull();
	}

}
