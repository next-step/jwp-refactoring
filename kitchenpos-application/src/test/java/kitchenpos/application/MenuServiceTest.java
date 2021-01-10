package kitchenpos.application;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.BaseTest;
import kitchenpos.domain.Menu;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.exception.WrongPriceException;
import kitchenpos.repository.MenuRepository;

@DisplayName("MenuService 테스트")
class MenuServiceTest extends BaseTest {

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuRepository menuRepository;

	@DisplayName("메뉴를 등록할 수 있다.")
	@Test
	void create() {

		MenuResponse menu = menuService.create(MenuRequest.of(예제_메뉴명, 예제_메뉴_가격, 예제_메뉴_그룹_ID, Arrays.asList(메뉴_후라이드_갯수, 메뉴_양념_갯수)));

		Menu savedMenu = menuRepository.findById(menu.getId()).orElse(null);
		assertAll(
			() -> assertThat(savedMenu.getId()).isNotNull(),
			() -> assertThat(savedMenu.getRealPrice()).isEqualTo(예제_메뉴_가격),
			() -> assertThat(savedMenu.getName()).isEqualTo(예제_메뉴명),
			() -> assertThat(savedMenu.getMenuGroup().getId()).isEqualTo(예제_메뉴_그룹_ID)
		);
	}

	@DisplayName("메뉴 가격을 기록하지 않거나, 0보다 작으면 등록할 수 없다.")
	@ParameterizedTest
	@NullSource
	@MethodSource("paramCreateThrow")
	void createThrow1(BigDecimal price) {

		assertThatExceptionOfType(WrongPriceException.class)
			.isThrownBy(() -> {
				menuService.create(MenuRequest.of(예제_메뉴명, price, 예제_메뉴_그룹_ID, Arrays.asList(메뉴_후라이드_갯수, 메뉴_양념_갯수)));
			});

	}

	public static Stream<Arguments> paramCreateThrow() {
		return Stream.of(
			Arguments.of(BigDecimal.valueOf(-1)),
			Arguments.of(BigDecimal.valueOf(-10)),
			Arguments.of(BigDecimal.valueOf(-100)),
			Arguments.of(BigDecimal.valueOf(-1000)),
			Arguments.of(BigDecimal.valueOf(-10000))
		);
	}

	@DisplayName("메뉴 가격은 메뉴에 포함된 상품가격 * 갯수 의 총합보다 크면 등록할 수 없다.")
	@Test
	void createThrow3() {

		assertThatExceptionOfType(WrongPriceException.class)
			.isThrownBy(() -> {
				BigDecimal 두_상품_합산_가격 = 후라이드_가격.add(양념치킨_가격).add(BigDecimal.valueOf(1));
				menuService.create(MenuRequest.of(예제_메뉴명, 두_상품_합산_가격, 예제_메뉴_그룹_ID, Arrays.asList(메뉴_후라이드_갯수, 메뉴_양념_갯수)));
			});

	}

	@DisplayName("메뉴을 조회할 수 있다.")
	@Test
	void list() {
		List<MenuResponse> menus = menuService.findAll();

		assertThat(menus).hasSize(6);
	}
}