package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
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
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

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

	private Menu menu;
	private MenuProduct menuProduct1;
	private MenuProduct menuProduct2;
	private Product product1;
	private Product product2;
	private MenuGroup menuGroup;
	private List<MenuProduct> menuProducts;

	@BeforeEach
	void setUp() {
		menuGroup = new MenuGroup(1L, "중식");
		product1 = new Product(1L, "탕수육", BigDecimal.valueOf(10000));
		product2 = new Product(2L, "깐풍기", BigDecimal.valueOf(12000));

		menuProduct1 = new MenuProduct(1L, 1L, product1.getId(), 1);
		menuProduct2 = new MenuProduct(2L, 1L, product2.getId(), 1);

		menuProducts = new ArrayList<>();
		menuProducts.add(menuProduct1);
		menuProducts.add(menuProduct2);

		menu = new Menu(1L, "A세트", BigDecimal.valueOf(20000), menuGroup.getId(), menuProducts);
	}

	@DisplayName("Menu 생성을 테스트 - happy path")
	@Test
	void testCreateMenu() {
		when(menuGroupDao.existsById(eq(menu.getMenuGroupId()))).thenReturn(true);
		when(productDao.findById(anyLong()))
			.thenReturn(Optional.of(product1))
			.thenReturn(Optional.of(product2));
		when(menuDao.save(eq(menu))).thenReturn(menu);
		when(menuProductDao.save(any())).thenReturn(menuProduct1).thenReturn(menuProduct2);
		Menu actual = menuService.create(menu);

		verify(menuGroupDao, times(1)).existsById(eq(menu.getMenuGroupId()));
		verify(productDao, times(2)).findById(anyLong());
		verify(menuDao, times(1)).save(any());
		verify(menuProductDao, times(2)).save(any());
		assertThat(actual.getName()).isEqualTo(menu.getName());
		assertThat(
			actual.getMenuProducts().stream().map(MenuProduct::getProductId).collect(Collectors.toList()))
			.containsExactlyElementsOf(
				menu.getMenuProducts().stream().map(MenuProduct::getProductId).collect(Collectors.toList()));
	}

	@DisplayName("메뉴 가격이 0보다 작은경우 오류발생")
	@Test
	void testCreateErrorPriceZero() {
		Menu menu = mock(Menu.class);
		when(menu.getPrice()).thenReturn(BigDecimal.valueOf(-1));

		assertThatThrownBy(() -> {
			menuService.create(menu);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴 가격은 0원 미만이 될 수 없습니다.");
	}

	@DisplayName("메뉴가 메뉴 그룹에 포함되어있지 않은경우 오류 발생")
	@Test
	void testMenuNotContainsInMenuGroup() {
		Menu menu = mock(Menu.class);
		when(menu.getPrice()).thenReturn(BigDecimal.valueOf(20000));
		when(menuGroupDao.existsById(anyLong())).thenReturn(false);

		assertThatThrownBy(() -> {
			menuService.create(menu);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴가 메뉴그룹에 등록되지 않았습니다.");
	}

	@DisplayName("메뉴의 메뉴상품이 상품에 등록되어 있지 않은경우 오류 발생")
	@Test
	void testMenuProductNotSavedProduct() {
		Menu menu = mock(Menu.class);
		when(menu.getPrice()).thenReturn(BigDecimal.valueOf(20000));
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);

		List<MenuProduct> menuProducts = new ArrayList<>();
		menuProducts.add(new MenuProduct(1L, 1L, 1L, 3));

		when(menu.getMenuProducts()).thenReturn(menuProducts);
		when(productDao.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> {
			menuService.create(menu);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("상품에 없는 메뉴상품입니다.");
	}

	@DisplayName("메뉴 가격이 메뉴 상품의 가격의 합보다 크면 오류 발생")
	@Test
	void testMenuPriceBiggerThanTotalMenuProductPrice() {
		Menu menu = mock(Menu.class);
		when(menu.getPrice()).thenReturn(BigDecimal.valueOf(20000));
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);

		List<MenuProduct> menuProducts = new ArrayList<>();
		menuProducts.add(new MenuProduct(1L, 1L, 1L, 2));

		Product product = new Product(1L, "product", BigDecimal.valueOf(9000));

		when(menu.getMenuProducts()).thenReturn(menuProducts);
		when(productDao.findById(anyLong())).thenReturn(Optional.of(product));

		assertThatThrownBy(() -> {
			menuService.create(menu);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴 가격은 메뉴 상품 가격의 합보다 작아야합니다.");
	}
}