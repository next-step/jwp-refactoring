package kitchenpos.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.domain.Product;

@ExtendWith(MockitoExtension.class)
public
class MenuServiceTest {

	@Mock
	MenuRepository menuRepository;
	@Mock
	MenuValidator menuValidator;

	@InjectMocks
	MenuService menuService;

	long MENU_GROUP_ID = 1L;
	Menu menu;
	List<MenuProduct> products;

	public static List<MenuProduct> createProducts(int count) {
		return LongStream.range(0, count)
						 .mapToObj(id ->
									   new MenuProduct(
										   id,
										   new Product(id, "product-" + id, 1_000),
										   1L))
						 .collect(Collectors.toList());
	}

	@BeforeEach
	void setUp() {
		products = createProducts(3);
		menu = new Menu("menu", 3_000L, MENU_GROUP_ID, products);
	}

	@Test
	@DisplayName("메뉴 생성 성공")
	void testCreateMenu() {
		when(menuRepository.save(menu)).thenReturn(menu);
		menuService.create(menu);

		verify(menuRepository, times(1)).save(menu);
	}

	@Test
	@DisplayName("메뉴 목록 조회 성공")
	void testGetMenuList() {
		when(menuRepository.findAll()).thenReturn(Lists.newArrayList(menu));

		List<Menu> menus = menuService.findAll();

		assertThat(menus).isNotEmpty();
		assertThat(menus).containsExactly(menu);
		verify(menuRepository, times(1)).findAll();
	}
}
