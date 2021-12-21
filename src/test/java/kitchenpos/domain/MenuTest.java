package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

	@Test
	@DisplayName("메뉴 생성 테스트")
	public void createMenuTest() {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setProductId(1L);
		menuProduct.setQuantity(2);
		
		//when
		Menu menu = new Menu(1L, "후라이드+후라이드", new BigDecimal(19000), 1L, Lists.newArrayList(menuProduct));

		//then
		assertThat(menu).isNotNull();
	}

}
