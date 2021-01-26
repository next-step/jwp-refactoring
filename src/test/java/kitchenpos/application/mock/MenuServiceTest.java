package kitchenpos.application.mock;

import static kitchenpos.domain.DomainFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
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

import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
	@InjectMocks
	private MenuService menuService;
	@Mock
	private MenuDao menuDao;
	@Mock
	private MenuGroupDao menuGroupDao;
	@Mock
	private MenuProductDao menuProductDao;
	@Mock
	private ProductDao productDao;

	private MenuProduct menuProduct;
	private Product product;

	@BeforeEach
	void setUp() {
		menuProduct = createMenuProduct(1L, 2L);
		product = createProduct(1L, "후라이드", new BigDecimal(11000));
	}

	@DisplayName("가격이 0 보다 작으면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException1() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(createMenu("후라이드+후라이드", new BigDecimal(-1), 1L, menuProduct)));
	}

	@DisplayName("없는 메뉴 그룹이면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException2() {
		when(menuGroupDao.existsById(anyLong())).thenReturn(false);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(createMenu("후라이드+후라이드", new BigDecimal(19000), 10L, menuProduct)));
	}

	@DisplayName("등록되지 않은 상품이면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException3() {
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		when(productDao.findById(anyLong())).thenReturn(Optional.empty());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(createMenu("후라이드+후라이드", new BigDecimal(19000), 1L, menuProduct)));
	}

	@DisplayName("가격이 각 메뉴 상품의 합보다 비싸면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException4() {
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		when(productDao.findById(anyLong())).thenReturn(Optional.of(product));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(createMenu("후라이드+후라이드", new BigDecimal(34000), 1L, menuProduct)));
	}

	@DisplayName("메뉴 등록")
	@Test
	void create() {
		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
		when(menuDao.save(any(Menu.class))).thenAnswer(invocation -> {
			Menu menu = invocation.getArgument(0, Menu.class);
			menu.setId(1L);
			return menu;
		});
		when(menuProductDao.save(any(MenuProduct.class))).thenAnswer(invocation -> {
			MenuProduct menuProduct = invocation.getArgument(0, MenuProduct.class);
			menuProduct.setSeq(1L);
			return menuProduct;
		});

		Menu resultMenu = menuService.create(createMenu("후라이드+후라이드", new BigDecimal(19000), 1L, menuProduct));

		assertThat(resultMenu.getId()).isNotNull();
		List<MenuProduct> menuProducts = resultMenu.getMenuProducts();
		assertThat(menuProducts.get(0).getMenuId()).isEqualTo(resultMenu.getId());
	}

	@DisplayName("메뉴 목록 조회")
	@Test
	void list() {
		when(menuDao.findAll()).thenReturn(createMenus(1L, 2L, 3L));
		when(menuProductDao.findAllByMenuId(anyLong())).thenReturn(Collections.singletonList(menuProduct));

		List<Menu> resultMenus = menuService.list();

		assertThat(resultMenus).hasSize(3);
		assertThat(resultMenus.stream().map(Menu::getMenuProducts).collect(Collectors.toList())).hasSize(3);
	}
}
