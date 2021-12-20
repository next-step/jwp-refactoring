package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

	@Test
	@DisplayName("메뉴상품 생성 테스트")
	public void createMenuTest() {
		//when
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(1L);
		menuProduct.setQuantity(2);

		//then
		assertThat(menuProduct).isNotNull();
	}

}
