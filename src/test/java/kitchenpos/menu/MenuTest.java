package kitchenpos.menu;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.manugroup.domain.MenuGroup;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.Product;

@DisplayName("메뉴 도메인 테스트")
public class MenuTest {
	
	@DisplayName("메뉴 생성")
	@Test
	void 메뉴_생성() {
		MenuGroup menuGroup = new MenuGroup(1L, "메뉴그룹");
		Product product = new Product(1L, "상품이름", new Price(new BigDecimal(1000)));
		MenuProduct menuProduct = new MenuProduct(1L, product, 1);
		Menu menu = new Menu(1L, "메뉴", new Price(new BigDecimal(1000)), menuGroup, new MenuProducts(Arrays.asList(menuProduct)));

	}
}
