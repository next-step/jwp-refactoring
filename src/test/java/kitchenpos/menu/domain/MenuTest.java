package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

public class MenuTest {
	@DisplayName("메뉴는 메뉴에 딸린 상품의 가격의 합보다 작을 수 없다.")
	@Test
	void createMenuInHappyCase() {
		Menu menu = new Menu("메뉴", new Price(new BigDecimal(12000)), new MenuGroup());
		MenuProduct menuProduct = new MenuProduct(menu, new Product("상품", new Price(new BigDecimal(1000))), 10L);

		assertThatThrownBy(() -> menu.addMenuProducts(Arrays.asList(menuProduct))).isInstanceOf(IllegalArgumentException.class);
	}
}
