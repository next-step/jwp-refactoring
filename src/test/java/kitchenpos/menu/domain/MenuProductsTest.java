package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

@DisplayName("메뉴상품들 도메인 테스트")
public class MenuProductsTest {

	public static final MenuProducts 후라이드둘_메뉴_후라이드_상품들 = MenuProducts.of(
		Collections.singletonList(MenuProductTest.후라이드둘_메뉴_후라이드_상품));

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		// given
		List<MenuProduct> menuProductList = Collections.singletonList(MenuProductTest.후라이드둘_메뉴_후라이드_상품);

		// when, then
		assertThat(MenuProducts.of(menuProductList)).isEqualTo(MenuProducts.of(menuProductList));
	}

	@DisplayName("주어진 가격이 총 구성품 가격보다 높은지 확인할 수 있다")
	@Test
	void isOverPriceTest() {
		// given
		Price price = Price.valueOf(50_000);
		MenuGroup 추천메뉴 = MenuGroup.of(1L, "추천메뉴");
		Menu 후라이드둘_메뉴 = Menu.of(1L, "후라이드둘", BigDecimal.valueOf(30_000), 추천메뉴);
		Product 후라이드 = Product.of(1L, "후라이드 치킨", BigDecimal.valueOf(15_000));
		MenuProduct menuProduct = MenuProduct.of(1L, 후라이드둘_메뉴, 후라이드, 2L);
		MenuProducts menuProducts = MenuProducts.of(Collections.singletonList(menuProduct));

		// when
		boolean result = menuProducts.isOverPrice(price);

		// then
		assertThat(result).isTrue();

	}
}
