package kitchenpos.domain;

import static kitchenpos.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

	public static MenuProduct 후라이드치킨2개 = new MenuProduct(후라이드치킨, 2);
	public static MenuProduct 피자3개 = new MenuProduct(피자, 3);

	@DisplayName("메뉴상품의 가격은 상품의 가격 * 개수로 계산된다")
	@Test
	void calculatePrice() {
		Product 치킨 = new Product("치킨", Price.wonOf(1000));

		MenuProduct 치킨2개 = new MenuProduct(치킨, 2);

		assertThat(치킨2개.calculatePrice()).isEqualTo(Price.wonOf(2000));

		MenuProduct 치킨0개 = new MenuProduct(치킨, 0);

		assertThat(치킨0개.calculatePrice()).isEqualTo(Price.wonOf(0));
	}
}