package kitchenpos.application;

import static kitchenpos.application.MenuGroupServiceTest.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.fixture.ProductFixture;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	MenuDao menuDao;
	@Mock
	MenuGroupDao menuGroupDao;
	@Mock
	MenuProductDao menuProductDao;
	@Mock
	ProductService productService;

	MenuService menuService;

	Menu menu;
	MenuGroup menuGroup;
	List<Product> products;

	@BeforeEach
	void setUp() {
		menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productService);

		products = ProductFixture.상품목록(3);
		menuGroup = createMenuGroup();
		menu = createMenu(products, menuGroup);
	}

	@Test
	@DisplayName("메뉴 생성")
	void testCreateMenu() {
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		products.forEach(
			product -> when(productService.findById(product.getId()))
				.thenReturn(product)
		);
		when(menuDao.save(menu)).thenReturn(menu);
		menu.getMenuProducts().forEach(
			menuProduct -> when(menuProductDao.save(menuProduct)).thenReturn(menuProduct));

		menuService.create(menu);

		verify(menuDao, times(1)).save(menu);
	}

	@Test
	@DisplayName("메뉴 가격이 0보다 작을 경우 등록실패")
	void testCreateMenuWhenPriceBelowThanZero() {
		menu.setPrice(BigDecimal.valueOf(-1));

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("메뉴 그룹이 존재하지 않을경우 등록실패")
	void testCreateMenuWhenMenuGroupNotExists() {
		when(menuGroupDao.existsById(anyLong())).thenReturn(false);

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("메뉴의 가격이 상품목록 가격 합보다 클 경우 등록 실패")
	void testCreateMenuWhenMenuPriceGreaterThanSumOfProductsPrice() {
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		products.forEach(
			product -> when(productService.findById(product.getId()))
				.thenReturn(product));
		menu.setPrice(menu.getPrice().add(BigDecimal.ONE));

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("상품이 존재하지 않을경우 등록실패")
	void testCreateMenuWhenProductNotExists() {
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		when(productService.findById(anyLong())).thenThrow(EntityNotFoundException.class);

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(EntityNotFoundException.class);
	}

	@Test
	@DisplayName("메뉴 목록 조회")
	void testGetMenuList() {
		when(menuDao.findAll()).thenReturn(Lists.newArrayList(menu));
		menu.getMenuProducts().forEach(
			menuProduct -> when(menuProductDao.findAllByMenuId(menu.getId()))
				.thenReturn(Lists.newArrayList(menuProduct)));

		List<Menu> menus = menuService.list();

		assertThat(menus).isNotEmpty();
		assertThat(menus).containsExactly(menu);
		verify(menuDao, times(1)).findAll();
	}

	public static Menu createMenu(List<Product> products, MenuGroup menuGroup) {
		Menu menu = new Menu();

		menu.setId(1L);
		menu.setName("식빵");
		Money price = products.stream()
			.map(Product::getPrice)
			.reduce(Money.ZERO, Money::add);
		menu.setPrice(price.toBigDecimal());
		menu.setMenuProducts(createMenuProducts(products));
		menu.setMenuGroupId(menuGroup.getId());

		return menu;
	}

	private static List<MenuProduct> createMenuProducts(List<Product> products) {
		return products.stream()
			.map(product -> {
				MenuProduct menuProduct = new MenuProduct();
				menuProduct.setProductId(product.getId());
				menuProduct.setQuantity(1);
				return menuProduct;
			}).collect(Collectors.toList());
	}
}
