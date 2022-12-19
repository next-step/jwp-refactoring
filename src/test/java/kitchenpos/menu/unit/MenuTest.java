package kitchenpos.menu.unit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;

@DisplayName("메뉴 관련 단위 테스트")
public class MenuTest {
	@DisplayName("메뉴를 생성할 수 있다.")
	@Test
	void createMenu() {
		// given
		Long 중식_ID = 1L;
		Long 짜장면_ID = 1L;
		Long 군만두_ID = 2L;
		MenuProduct 짜장면_1개 = MenuProduct.of(짜장면_ID, 1);
		MenuProduct 군만두_2개 = MenuProduct.of(군만두_ID, 2);
		// when
		Menu 짜장세트 = Menu.of("짜장세트", BigDecimal.valueOf(6000), 중식_ID,
			MenuProducts.of(Arrays.asList(짜장면_1개, 군만두_2개)));
		// then
		assertAll(
			() -> assertThat(짜장세트.getMenuGroupId()).isEqualTo(중식_ID),
			() -> assertThat(짜장세트.getMenuProducts().value().size()).isEqualTo(2)
		);
	}

	@DisplayName("메뉴의 가격을 메뉴상품 가격과 비교할 수 있다.")
	@Test
	void moreExpensive() {
		// given
		Long 중식_ID = 1L;
		Long 짜장면_ID = 1L;
		Long 군만두_ID = 2L;
		MenuProduct 짜장면_1개 = MenuProduct.of(짜장면_ID, 1);
		MenuProduct 군만두_2개 = MenuProduct.of(군만두_ID, 2);
		// when
		Menu 짜장세트_6000원 = Menu.of("짜장세트", BigDecimal.valueOf(6000), 중식_ID,
			MenuProducts.of(Arrays.asList(짜장면_1개, 군만두_2개)));
		// then
		assertTrue(짜장세트_6000원.moreExpensive(Price.of(5000)));
	}

}
