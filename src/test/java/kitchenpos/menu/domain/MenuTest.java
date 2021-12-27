package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.product.domain.ProductTest;

@DisplayName("메뉴 도메인 테스트")
public class MenuTest {

	public static final Menu 후라이드둘 = Menu.of(1L, "후라이드둘", BigDecimal.valueOf(30_000), MenuGroupTest.추천메뉴);

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(Menu.of(1L, "후라이드", BigDecimal.ONE, MenuGroup.of(1L, "추천메뉴")))
			.isEqualTo(Menu.of(1L, "후라이드", BigDecimal.ONE, MenuGroup.of(1L, "추천메뉴")));
	}

	@DisplayName("등록 시, 메뉴 그룹이 필요하다")
	@Test
	void validateTest() {
		assertThatThrownBy(() -> Menu.of("반반", 1000, null))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("등록 시, 가격 정보가 필요하다")
	@Test
	void validateTest2() {
		assertThatThrownBy(() -> Menu.create("반반", null, MenuGroupTest.추천메뉴))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("등록 시, 이름이 필요하다")
	@Test
	void validateTest3() {
		assertThatThrownBy(() -> Menu.create(null, BigDecimal.ONE, MenuGroupTest.추천메뉴))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("가격이 구성품의 가격의 합보다 높으면 안된다")
	@Test
	void checkOverPriceTest() {
		// given
		Menu menu = Menu.of(1L, "후라이드들", BigDecimal.valueOf(50_000), MenuGroupTest.추천메뉴);
		MenuProduct.of(1L, menu, ProductTest.후라이드, 2L);

		// when, then
		assertThatThrownBy(menu::checkOverPrice)
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());

	}
}
