package kitchenpos.table.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.Product;

class MenuTest {

	@DisplayName("메뉴가격이 메뉴 상품의 총 가격보다 크면 오류발생")
	@Test
	void testValidateTotalPrice() {
		//given
		Product 파스타 = new Product("파스타", new Price(BigDecimal.valueOf(8000)));

		List<MenuProduct> menuProductList = new ArrayList<>();
		menuProductList.add(new MenuProduct(파스타, 2));

		Assertions.assertThatThrownBy(() -> {
			new Menu("menu", new Price(BigDecimal.valueOf(20000)), new MenuGroup("양식"), menuProductList);
		})
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴 가격은 메뉴 상품 가격의 합보다 작아야합니다.");
	}

	@DisplayName("menu 명 null인경우 오류발생")
	@Test
	void testMenuNameNull() {
		Assertions.assertThatThrownBy(() -> {
			new Menu(null, new Price(BigDecimal.valueOf(20000)), new MenuGroup("양식"), null);
		})
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("menu 명을 입력해주세요");
	}

	@DisplayName("가격이 null인경우 오류 발생")
	@Test
	void testMenuPriceNull() {
		Assertions.assertThatThrownBy(() -> {
			new Menu("menu name", null, new MenuGroup("양식"), null);
		})
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("가격을 입력해주세요");
	}
}