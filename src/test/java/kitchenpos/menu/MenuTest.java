package kitchenpos.menu;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@DisplayName("메뉴 도메인 테스트")
public class MenuTest {
	
	@DisplayName("메뉴 생성")
	@Test
	void create() {
		MenuProduct menuProduct = new MenuProduct();
		Menu menu = new Menu(1L, "메뉴", new BigDecimal(1000), 1L, Arrays.asList(menuProduct));
	}
}
