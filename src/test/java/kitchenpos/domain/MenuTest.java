package kitchenpos.domain;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import kitchenpos.exception.WrongPriceException;

@DisplayName("Menu 도메인 테스트")
class MenuTest {

	@DisplayName("create 메소드는 메뉴명, 가격, 메뉴 그룹, 상품정보, 수량 정보를 받아 Menu객체를 생성한다.")
	@Test
	void create() {
		Menu menu = Menu.create("메뉴명", BigDecimal.valueOf(10000), MenuGroup.create("메뉴그룹"), Arrays.asList(예제_상품1(), 예제_상품2()), Arrays.asList(1L, 1L));

		assertThat(menu).isInstanceOf(Menu.class);
	}

	@DisplayName("create 메소드는 메뉴 가격이 없거나 0보다 작으면 WrongPriceException이 발생한다.")
	@ParameterizedTest
	@NullSource
	@MethodSource("paramCreateThrow1")
	void createThrow1(BigDecimal price) {
		assertThatExceptionOfType(WrongPriceException.class)
			.isThrownBy(() -> {
				Menu.create("메뉴명", price, MenuGroup.create("메뉴그룹"), Arrays.asList(예제_상품1(), 예제_상품2()), Arrays.asList(1L, 1L));
			});

	}

	public static Stream<Arguments> paramCreateThrow1() {
		return Stream.of(
			Arguments.of(BigDecimal.valueOf(-1)),
			Arguments.of(BigDecimal.valueOf(-10)),
			Arguments.of(BigDecimal.valueOf(-100)),
			Arguments.of(BigDecimal.valueOf(-1000)),
			Arguments.of(BigDecimal.valueOf(-10000))
		);
	}

	@DisplayName("create 메소드는 메뉴 가격이 상품 총합가격보다 크면 WrongPriceException이 발생한다.")
	@ParameterizedTest
	@MethodSource("paramCreateThrow2")
	void createThrow2(BigDecimal price) {
		assertThatExceptionOfType(WrongPriceException.class)
			.isThrownBy(() -> {
				Menu.create("메뉴명", price, MenuGroup.create("메뉴그룹"), Arrays.asList(오천원_상품(), 육천원_상품()), Arrays.asList(1L, 1L));
			});

	}

	public static Stream<Arguments> paramCreateThrow2() {
		return Stream.of(
			Arguments.of(BigDecimal.valueOf(11001)),
			Arguments.of(BigDecimal.valueOf(12000)),
			Arguments.of(BigDecimal.valueOf(1200000))
		);
	}

}