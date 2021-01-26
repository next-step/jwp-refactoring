package kitchenpos.application;

import static kitchenpos.domain.DomainFactory.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MenuServiceTest {
	@Autowired
	private MenuService menuService;

	@DisplayName("가격이 0 보다 작으면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException1() {
		MenuProduct menuProduct = createMenuProduct(1L, 2L);
		Menu menu = createMenu("후라이드+후라이드", -1, 1L, menuProduct);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(menu));
	}

	@DisplayName("없는 메뉴 그룹이면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException2() {
		MenuProduct menuProduct = createMenuProduct(1L, 2L);
		Menu menu = createMenu("후라이드+후라이드", 19000, 10L, menuProduct);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(menu));
	}

	@DisplayName("가격이 각 메뉴 상품의 합보다 비싸면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException3() {
		MenuProduct menuProduct = createMenuProduct(1L, 2L);
		Menu menu = createMenu("후라이드+후라이드", 34000, 1L, menuProduct);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(menu));
	}

	@DisplayName("메뉴 등록")
	@Test
	void create() {
		MenuProduct menuProduct = createMenuProduct(1L, 2L);
		Menu menu = createMenu("후라이드+후라이드", 19000, 1L, menuProduct);

		Menu resultMenu = menuService.create(menu);

		assertThat(resultMenu.getId()).isNotNull();
		List<MenuProduct> menuProducts = resultMenu.getMenuProducts();
		assertThat(menuProducts.get(0).getMenuId()).isEqualTo(resultMenu.getId());
	}

	@DisplayName("메뉴 목록 조회")
	@Test
	void list() {
		List<Menu> resultMenus = menuService.list();

		assertThat(resultMenus).hasSize(6);
		assertThat(resultMenus.stream().map(Menu::getMenuProducts).collect(Collectors.toList())).hasSize(6);
	}
}
