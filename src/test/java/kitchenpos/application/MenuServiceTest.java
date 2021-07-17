package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuRequest;
import kitchenpos.domain.MenuProductRequest;
import kitchenpos.domain.ProductRequest;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
	private MenuRequest chickenMenu;
	private MenuRequest ramenMenu;
	private MenuRequest sogogiMenu;
	private ProductRequest chicken;
	private MenuProductRequest menuProduct;

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

	@BeforeEach
	void setUp() {
	 	chickenMenu = new MenuRequest("치킨 메뉴", new BigDecimal(20000), 1L);
		ramenMenu = new MenuRequest("라면 메뉴", new BigDecimal(30000), 2L);
		sogogiMenu = new MenuRequest("소고기 메뉴", new BigDecimal(100000), 3L);
		chicken = new ProductRequest("치킨", new BigDecimal(10000));
		menuProduct = new MenuProductRequest(1L, 1L, 2L);
	}

	@DisplayName("메뉴를 등록한다.")
	@Test
	void createMenuInHappyCase() {
		// given
		lenient().when(menuGroupDao.existsById(1L)).thenReturn(true);
		lenient().when(productDao.findById(1L)).thenReturn(Optional.of(chicken));
		lenient().when(menuDao.save(any())).thenReturn(chickenMenu);
		lenient().when(menuProductDao.save(any())).thenReturn(menuProduct);
		// when
		List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct);
		MenuRequest menu = menuService.create(new MenuRequest("치킨 메뉴", new BigDecimal(20000), 1L, menuProducts));
		// then
		assertThat(menu.getName()).isEqualTo("치킨 메뉴");
		assertThat(menu.getPrice()).isEqualTo(new BigDecimal(20000));
	}

	@DisplayName("가격이 음수인 메뉴를 등록할 수 없다.")
	@Test
	void createMenuWithMinusPrice() {
		// given
		lenient().when(menuGroupDao.existsById(1L)).thenReturn(true);
		lenient().when(productDao.findById(1L)).thenReturn(Optional.of(chicken));
		lenient().when(menuDao.save(any())).thenReturn(chickenMenu);
		lenient().when(menuProductDao.save(any())).thenReturn(menuProduct);
		// when, then
		List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct);
		assertThatThrownBy(() -> menuService.create(new MenuRequest("치킨 메뉴", new BigDecimal(-10000), 1L, menuProducts)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("등록되어 있지 않은 메뉴 그룹을 요청할 수 없다.")
	@Test
	void createMenuWithNotExistMenuGroup() {
		// given
		lenient().when(menuGroupDao.existsById(1L)).thenReturn(false);
		lenient().when(productDao.findById(1L)).thenReturn(Optional.of(chicken));
		lenient().when(menuDao.save(any())).thenReturn(chickenMenu);
		lenient().when(menuProductDao.save(any())).thenReturn(menuProduct);
		// when, then
		List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct);
		assertThatThrownBy(() -> menuService.create(new MenuRequest("치킨 메뉴", new BigDecimal(20000), 1L, menuProducts)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("등록되어 있지 않은 상품을 요청할 수 없다.")
	@Test
	void createMenuWithNotExistProduct() {
		// given
		lenient().when(menuGroupDao.existsById(1L)).thenReturn(true);
		lenient().when(menuDao.save(any())).thenReturn(ramenMenu);
		lenient().when(menuProductDao.save(any())).thenReturn(menuProduct);
		// when, then
		List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct);
		assertThatThrownBy(() -> menuService.create(new MenuRequest("치킨 메뉴", new BigDecimal(20000), 1L, menuProducts)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품의 가격 합 보다 메뉴의 가격이 더 클 수 없다.")
	@Test
	void createMenuWhereMenuPriceIsBiggerThanProductSum() {
		// given
		ProductRequest cheapChicken = new ProductRequest("치킨", new BigDecimal(100));
		lenient().when(menuGroupDao.existsById(1L)).thenReturn(true);
		lenient().when(productDao.findById(1L)).thenReturn(Optional.of(cheapChicken));
		lenient().when(menuDao.save(any())).thenReturn(chickenMenu);
		lenient().when(menuProductDao.save(any())).thenReturn(menuProduct);
		// when, then
		List<MenuProductRequest> menuProducts = Arrays.asList(menuProduct);
		assertThatThrownBy(() -> menuService.create(new MenuRequest("치킨 메뉴", new BigDecimal(20000), 1L, menuProducts)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴를 조회한다.")
	@Test
	void findMenu() {
		// given
		when(menuDao.findAll()).thenReturn(Arrays.asList(chickenMenu, ramenMenu, sogogiMenu));
		when(menuProductDao.findAllByMenuId(any())).thenReturn(Arrays.asList(new MenuProductRequest(), new MenuProductRequest()));
		// when
		List<MenuRequest> menus = menuService.list();
		// then
		assertThat(menus.size()).isEqualTo(3);
		assertThat(menus.get(0).getName()).isEqualTo("치킨 메뉴");
		assertThat(menus.get(1).getName()).isEqualTo("라면 메뉴");
		assertThat(menus.get(2).getName()).isEqualTo("소고기 메뉴");
	}
}
