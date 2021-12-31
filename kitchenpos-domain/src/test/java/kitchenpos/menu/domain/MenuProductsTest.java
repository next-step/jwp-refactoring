package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
	
}
