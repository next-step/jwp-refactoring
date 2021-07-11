package kitchenpos.table.domain;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.Product;

class MenuTest {

	@DisplayName("메뉴가격이 메뉴 상품의 총 가격보다 크면 오류발생")
	@Test
	void testValidateTotalPrice() {
		//given
		Product 파스타 = new Product("파스타", new Price(BigDecimal.valueOf(8000)));

		Menu menu = new Menu("menu", new Price(BigDecimal.valueOf(20000)), new MenuGroup("양식"));
		menu.addMenuProduct(new MenuProduct(파스타, 2));

		Assertions.assertThatThrownBy(menu::validateMenuPrice)
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴 가격은 메뉴 상품 가격의 합보다 작아야합니다.");
	}
}