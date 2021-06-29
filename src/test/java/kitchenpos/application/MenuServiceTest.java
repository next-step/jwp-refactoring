package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@DisplayName("메뉴 요구사항 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuDao menuDao;

	@Mock
	private MenuGroupDao menuGroupDao;

	@Mock
	private MenuProductDao menuProductDao;

	@Mock
	private ProductDao productDao;

	@InjectMocks
	private MenuService menuService;

	@DisplayName("가격이 음수인 메뉴는 등록할 수 없다.")
	@Test
	void createMenuNegativePriceTest() {
		// given
		Menu negativePriceMenu = mock(Menu.class);
		when(negativePriceMenu.getPrice()).thenReturn(BigDecimal.valueOf(-1));

		// when
		// than
		assertThatThrownBy(() -> menuService.create(negativePriceMenu))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("가격이 음수인 메뉴는 등록할 수 없습니다");
	}

	@DisplayName("메뉴 그룹이 존재하지 않는 메뉴는 등록할 수 없다.")
	@Test
	void createMenuNoGroupMenuTest() {
		// given
		Menu menu = mock(Menu.class);
		when(menu.getPrice()).thenReturn(BigDecimal.ZERO);
		when(menuGroupDao.existsById(anyLong())).thenReturn(false);

		// when
		// than
		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴 그룹이 존재하지 않는 메뉴는 등록할 수 없습니다.");
	}

	@DisplayName("상품으로 등록된 메뉴만 등록할 수 있다.")
	@Test
	void createMenuNotMatchedProductMenuTest() {
		// given
		Menu menu = mock(Menu.class);
		when(menu.getPrice()).thenReturn(BigDecimal.ZERO);
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);

		MenuProduct menuProduct = mock(MenuProduct.class);
		when(menu.getMenuProducts()).thenReturn(asList(menuProduct));

		when(productDao.findById(anyLong())).thenReturn(Optional.empty());

		// when
		// than
		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("상품으로 등록되지 않은 메뉴는 등록할 수 없습니다.");
	}

	@DisplayName("메뉴의 가격이 메뉴와 연결된 상품의 수량 * 가격 보다 비쌀 수 없다.")
	@Test
	void createMenuInvalidPriceMenuTest() {
		// given
		Menu menu = mock(Menu.class);
		when(menu.getPrice()).thenReturn(BigDecimal.valueOf(1001));
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);

		MenuProduct menuProduct = mock(MenuProduct.class);
		when(menu.getMenuProducts()).thenReturn(asList(menuProduct));
		when(menuProduct.getQuantity()).thenReturn(1L);

		Product product = mock(Product.class);
		when(product.getPrice()).thenReturn(BigDecimal.valueOf(1000));
		when(productDao.findById(anyLong())).thenReturn(Optional.of(product));

		// when
		// than
		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴의 가격이 메뉴와 연결된 상품의 수량 * 가격 보다 비쌀 수 없습니다.");
	}

	@DisplayName("메뉴를 등록할 수 있다.")
	@Test
	void createMenuTest() {
		// given
		Menu menu = mock(Menu.class);
		when(menu.getPrice()).thenReturn(BigDecimal.valueOf(1000));
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);

		MenuProduct menuProduct = mock(MenuProduct.class);
		when(menu.getMenuProducts()).thenReturn(asList(menuProduct));
		when(menuProduct.getQuantity()).thenReturn(1L);

		Product product = mock(Product.class);
		when(product.getPrice()).thenReturn(BigDecimal.valueOf(1000));
		when(productDao.findById(anyLong())).thenReturn(Optional.of(product));

		Menu savedMenu = mock(Menu.class);
		when(menuDao.save(menu)).thenReturn(savedMenu);

		// when
		menuService.create(menu);

		// than
		verify(menuDao).save(menu);
		verify(menuProduct).setMenuId(savedMenu.getId());
		verify(menuProductDao).save(menuProduct);
		verify(savedMenu).setMenuProducts(anyList());
	}

	@DisplayName("메뉴 목록 조회를 할 수 있다.")
	@Test
	void listTest() {
		// given
		Menu menu = mock(Menu.class);
		when(menuDao.findAll()).thenReturn(asList(menu));

		// when
		List<Menu> menus = menuService.list();

		// then
		assertThat(menus).containsExactly(menu);
		verify(menuProductDao).findAllByMenuId(anyLong());
		verify(menu).setMenuProducts(anyList());
	}
}
