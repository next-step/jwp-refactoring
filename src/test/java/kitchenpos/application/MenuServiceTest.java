package kitchenpos.application;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.BaseTest;
import kitchenpos.common.TestDataUtil;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

@DisplayName("MenuService 테스트")
class MenuServiceTest extends BaseTest {

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuDao menuDao;

	@DisplayName("메뉴를 등록할 수 있다.")
	@Test
	void create() {

		Menu menu = menuService.create(TestDataUtil.createMenu(예제_메뉴명, 예제_메뉴_가격, 예제_메뉴_그룹_ID, Arrays.asList(메뉴_후라이드, 메뉴_양념)));

		Menu savedMenu = menuDao.findById(menu.getId()).orElse(null);
		assertAll(
			() -> assertThat(savedMenu.getId()).isNotNull(),
			() -> assertThat(savedMenu.getPrice().intValue()).isEqualTo(예제_메뉴_가격),
			() -> assertThat(savedMenu.getName()).isEqualTo(예제_메뉴명),
			() -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(예제_메뉴_그룹_ID)
		);
	}

	@DisplayName("메뉴 가격을 기록하지 않거나, 0보다 작으면 등록할 수 없다.")
	@ParameterizedTest
	@NullSource
	@ValueSource(ints = {-1, -100, -1000})
	void createThrow1(Integer price) {

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				menuService.create(TestDataUtil.createMenu(예제_메뉴명, price, 예제_메뉴_그룹_ID, Arrays.asList(메뉴_후라이드, 메뉴_양념)));
			});

	}

	@DisplayName("메뉴 그룹 정보가 없으면 등록할 수 없다.")
	@Test
	void createThrow2() {

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				menuService.create(TestDataUtil.createMenu(예제_메뉴명, 예제_메뉴_가격, 메뉴_그룹_ID_없음, Arrays.asList(메뉴_후라이드, 메뉴_양념)));
			});

	}

	@DisplayName("메뉴 가격은 메뉴에 포함된 상품가격 * 갯수 의 총합보다 크면 등록할 수 없다.")
	@Test
	void createThrow3() {

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				int 두_상품_합산_가격 = 후라이드_가격 + 양념치킨_가격 + 1;
				menuService.create(TestDataUtil.createMenu(예제_메뉴명, 두_상품_합산_가격, 예제_메뉴_그룹_ID, Arrays.asList(메뉴_후라이드, 메뉴_양념)));
			});

	}

	@DisplayName("메뉴을 조회할 수 있다.")
	@Test
	void list() {
		List<Menu> menus = menuService.list();

		assertThat(menus).hasSize(6);
	}
}