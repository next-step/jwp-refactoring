package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.ProductFixture;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	MenuRepository menuRepository;

	@InjectMocks
	MenuService menuService;

	Menu2 menu;
	MenuGroup menuGroup;
	List<Product> products;

	@BeforeEach
	void setUp() {
		products = ProductFixture.상품목록(3);
		menuGroup = 메뉴그룹();
		menu = MenuFixture.메뉴2(products, menuGroup);
	}

	@Test
	@DisplayName("메뉴 생성")
	void testCreateMenu() {
		when(menuRepository.save(menu)).thenReturn(menu);

		menuService.create2(menu);

		verify(menuRepository, times(1)).save(menu);
	}

	@Test
	@DisplayName("메뉴의 가격이 상품목록 가격 합보다 클 경우 등록 실패")
	void testCreateMenuWhenMenuPriceGreaterThanSumOfProductsPrice() {
		Menu2 invalidMenu = new Menu2(menu.getName(), Money.valueOf(100_000), menuGroup, products);

		assertThatThrownBy(() -> menuService.create2(invalidMenu))
			.isInstanceOf(InvalidMenuPriceException.class);
	}

	@Test
	@DisplayName("메뉴 목록 조회")
	void testGetMenuList() {
		when(menuRepository.findAll()).thenReturn(Lists.newArrayList(menu));

		List<Menu2> menus = menuService.list2();

		assertThat(menus).isNotEmpty();
		assertThat(menus).containsExactly(menu);
		verify(menuRepository, times(1)).findAll();
	}

}
