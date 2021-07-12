package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
public class MenuServiceTest {
	private MenuService menuService;

	@Mock
	private MenuDao menuDao;
	@Mock
	private MenuGroupDao menuGroupDao;
	@Mock
	private MenuProductDao menuProductDao;
	@Mock
	private ProductDao productDao;

	@BeforeEach
	void setUp() {
		menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
	}

	@Test
	void createMenuInHappyCase() {
		// given
		when(menuGroupDao.existsById(any())).thenReturn(true);
		when(productDao.findById(any())).thenReturn(Optional.of(new Product("치킨", new BigDecimal(10000))));
		when(menuDao.save(any())).thenReturn(new Menu("치킨메뉴", new BigDecimal(20000), 1L));
		when(menuProductDao.save(any())).thenReturn(new MenuProduct(1L, 1L, 2L));
		// when
		List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(1L, 1L, 2L));
		Menu menu = menuService.create(new Menu("치킨메뉴", new BigDecimal(20000), 1L, menuProducts));
		// then
		assertThat(menu.getName()).isEqualTo("치킨메뉴");
		assertThat(menu.getPrice()).isEqualTo(new BigDecimal(20000));
	}

	@Test
	void findMenu() {
		// given
		when(menuDao.findAll()).thenReturn(Arrays.asList(new Menu(), new Menu(), new Menu()));
		when(menuProductDao.findAllByMenuId(any())).thenReturn(Arrays.asList(new MenuProduct(), new MenuProduct()));
		// when
		List<Menu> menus = menuService.list();
		// then
		assertThat(menus.size()).isEqualTo(3);
	}
}
