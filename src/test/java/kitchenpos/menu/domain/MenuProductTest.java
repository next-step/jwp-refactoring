package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.product.domain.ProductTest;

@DisplayName("메뉴-제품 도메인 테스트")
public class MenuProductTest {

	public static final MenuProduct 후라이드둘_메뉴_후라이드_상품 = MenuProduct.of(1L, MenuTest.후라이드둘, ProductTest.후라이드, 2L);

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(MenuProduct.of(1L, null, null, 1L))
			.isEqualTo(MenuProduct.of(1L, null, null, 1L));
	}

	@DisplayName("생성 시, 상품 정보가 필요합니다")
	@Test
	void validateTest2() {
		assertThatThrownBy(() -> MenuProduct.create(null, 2L))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("생성 시, 수량 정보가 0 이상이어야 합니다")
	@Test
	void validateTest3() {
		assertThatThrownBy(() -> MenuProduct.create(ProductTest.후라이드, -1L))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

}
