package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

@Transactional
@SpringBootTest
class MenuServiceTest {

	private static MenuProductRequest 후라이드_2마리 = new MenuProductRequest(1, 2);
	private static MenuProductRequest 양념_1마리 = new MenuProductRequest(2, 1);
	private static MenuRequest 후라이드2_양념1 = new MenuRequest("후라이드+후라이드+양념", new BigDecimal(40000), 1L,
		Lists.newArrayList(후라이드_2마리, 양념_1마리));

	@Autowired
	private MenuService menuService;

	@Test
	@DisplayName("메뉴 생성 테스트")
	public void createMenuSuccessTest() {
		//given
		//when
		Menu menu = menuService.create(후라이드2_양념1);

		//then
		assertThat(menu).isNotNull();
		assertThat(menu.getId()).isEqualTo(7L);
		assertThat(menu.getName()).isEqualTo("후라이드+후라이드+양념");
	}

	@Test
	@DisplayName("메뉴 가격이 0보다 작아서 실패 테스트")
	public void createMenuFailZeroLessThanFailTest() {
		//given
		MenuRequest 메뉴가격이_0보다_작은_메뉴 = new MenuRequest("후라이드+후라이드+양념", new BigDecimal(-1), 1L,
			Lists.newArrayList(후라이드_2마리, 양념_1마리));

		//when
		//then
		assertThatThrownBy(() -> menuService.create(메뉴가격이_0보다_작은_메뉴))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("가격은 0보다 작을 수 없습니다");
	}

	@Test
	@DisplayName("메뉴의 메뉴그룹이 존재하지 않아서 생성 실패")
	public void createMenuNotExistedMenuGroupFailTest() {
		//given
		MenuRequest 메뉴그룹이_없는_메뉴 = new MenuRequest("후라이드+후라이드+양념", new BigDecimal(43000), 99L,
			Lists.newArrayList(후라이드_2마리, 양념_1마리));

		//when
		//then
		assertThatThrownBy(() -> menuService.create(메뉴그룹이_없는_메뉴))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("메뉴그룹이 존재하지 않습니다.");
	}

	@Test
	@DisplayName("메뉴의 가격이 상품들의 가격합보다 커서 생성 실패")
	public void createMenuPriceGreaterThanProductsPriceFailTest() {
		//given
		MenuRequest 상품들가격_합보다_가격이_큰_메뉴 = new MenuRequest("후라이드+후라이드+양념", new BigDecimal(60000), 1L,
			Lists.newArrayList(후라이드_2마리, 양념_1마리));

		//when
		//then
		assertThatThrownBy(() -> menuService.create(상품들가격_합보다_가격이_큰_메뉴))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("메뉴의 가격은 상품들의 가격합보다 작거나 같아야 합니다");
	}

	@Test
	@DisplayName("메뉴 목록 조회 테스트")
	public void findMenuList() {
		//given
		//when

		List<Menu> menus = menuService.list();
		//then
		assertThat(menus).hasSize(6);
	}
}
