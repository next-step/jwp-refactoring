package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.domain.ProductTest;

@DisplayName("메뉴-제품 도메인 테스트")
public class MenuProductTest {

	public static final MenuProduct 후라이드둘_메뉴_후라이드_상품 = MenuProduct.of(1L, MenuTest.후라이드둘, ProductTest.후라이드, 2L);

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(MenuProduct.of(1L, null, null, 0L))
			.isEqualTo(MenuProduct.of(1L, null, null, 0L));
	}

}
