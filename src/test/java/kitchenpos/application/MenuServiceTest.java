package kitchenpos.application;

import static kitchenpos.application.MenuGroupServiceTest.createMenuGroup;
import static kitchenpos.application.ProductServiceTest.createProducts;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock MenuDao menuDao;
	@Mock MenuGroupDao menuGroupDao;
	@Mock MenuProductDao menuProductDao;
	@Mock ProductDao productDao;

	MenuService menuService;

	private Menu menu;
	private MenuGroup menuGroup;
	private List<Product> products;

	@BeforeEach
	void setUp() {
		menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

		products = createProducts(1000, 2000, 3000);
		menuGroup = createMenuGroup();
		menu = createMenu(products, menuGroup);
	}

	@Test
	@DisplayName("메뉴 등록")
	void testCreateMenu() {
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		products.forEach(
			product -> when(productDao.findById(product.getId())).thenReturn(Optional.of(product))
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
	@DisplayName("메뉴의 가격이 상품 가격 합보다 클 경우 등록 실패")
	void testCreateMenuWhenMenuPriceGreaterThanSumOfProductsPrice() {
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		products.forEach(
			product -> when(productDao.findById(product.getId()))
				.thenReturn(Optional.of(product)));
		menu.setPrice(menu.getPrice().add(BigDecimal.ONE));

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("상품이 존재하지 않을경우 등록실패")
	void testCreateMenuWhenProductNotExists() {
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		when(productDao.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
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
		BigDecimal price = products.stream()
			.map(Product::getPrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		menu.setPrice(price);
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
