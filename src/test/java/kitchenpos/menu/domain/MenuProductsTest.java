package kitchenpos.menu.domain;

import static kitchenpos.TextFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

class MenuProductsTest {

	public static MenuProduct 후라이드치킨2개 = new MenuProduct(후라이드치킨, 2);
	public static MenuProduct 피자3개 = new MenuProduct(피자, 3);

	@DisplayName("메뉴 상품의 가격은 상품의 가격 * 개수로 계산된다")
	@Test
	void calculatePrice() {
		// given
		Product 치킨 = new Product(Name.valueOf("치킨"), Price.wonOf(1000));

		// when
		MenuProduct 치킨2개 = new MenuProduct(치킨, 2);

		// then
		assertThat(치킨2개.calculatePrice()).isEqualTo(Price.wonOf(2000));

		// when
		MenuProduct 치킨0개 = new MenuProduct(치킨, 0);

		// than
		assertThat(치킨0개.calculatePrice()).isEqualTo(Price.wonOf(0));
	}

	@DisplayName("메뉴상품 리스트의 없다면, 비어있는 리스트가 저장된다.")
	@Test
	void createWithNullTest() {
		MenuProducts menuProducts = MenuProducts.of(null);

		assertThat(menuProducts.getMenuProducts()).isNotNull();
		assertThat(menuProducts.getMenuProducts()).hasSize(0);
	}
}