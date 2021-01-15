package kitchenpos.domain;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MenuTest {

	@Mock
	private MenuDao menuDao;

	@Mock
	private MenuGroupDao menuGroupDao;

	@Mock
	private MenuProductDao menuProductDao;

	@Mock
	private ProductDao productDao;

	private MenuService menuService;

	@BeforeEach
	void setUp() {
		menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
		assertThat(menuService).isNotNull();
	}

	@Test
	void 메뉴를_등록한다(){
		when(menuGroupDao.existsById(any())).thenReturn(true);

		Product product = new Product();
		product.setPrice(BigDecimal.valueOf(10000));
		when(productDao.findById(any())).thenReturn(java.util.Optional.of(product));

		Menu menu = new Menu();
		menu.setId(1L);
		menu.setPrice(BigDecimal.valueOf(10000));

		List<MenuProduct> menuProducts = new ArrayList<>();
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setQuantity(2);
		menuProducts.add(menuProduct);
		menu.setMenuProducts(menuProducts);

		when(menuProductDao.save(menuProduct)).thenReturn(menuProduct);
		when(menuDao.save(menu)).thenReturn(menu);
		assertThat(menuService.create(menu)).isEqualTo(menu);
	}

	@Test
	void 메뉴_등록_시_가격이_null_또는_0_미만이면_에러(){
		Menu menu1 = new Menu();
		menu1.setPrice(BigDecimal.valueOf(0));
		assertThrows(IllegalArgumentException.class, () -> menuService.create(menu1));

		Menu menu2 = new Menu();
		menu2.setPrice(null);
		assertThrows(IllegalArgumentException.class, () -> menuService.create(menu1));

	}
	@Test
	void 메뉴_목록을_조회한다(){
		List<Menu> menus = new ArrayList<>();
		menus.add(new Menu());
		menus.add(new Menu());
		when(menuDao.findAll()).thenReturn(menus);
		assertThat(menuService.list()).isNotNull();
		assertThat(menuService.list()).isNotEmpty();
		assertThat(menuService.list().size()).isEqualTo(2);
	}
}
