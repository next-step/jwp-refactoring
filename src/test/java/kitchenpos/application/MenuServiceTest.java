package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
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

	@Test
	void menuCreateTest() {
		Product product = new Product(1L);
		product.setPrice(BigDecimal.valueOf(10000));

		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setQuantity(10);
		menuProduct.setProductId(1L);

		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		when(productDao.findById(anyLong())).thenReturn(Optional.of(product));
		when(menuDao.save(any(Menu.class))).thenReturn(new Menu(1L));
		when(menuProductDao.save(any(MenuProduct.class))).thenReturn(new MenuProduct());

		assertThat(menuService.create(new Menu(1L, "치킨", BigDecimal.valueOf(20000), 1L, Arrays.asList(menuProduct)))).isNotNull();
	}

	@Test
	@DisplayName("메뉴 생성 시 금액이 없거나 0원 이하면 익셉션 발생.")
	void menuCreateFailTest() {
		Menu menu = new Menu(1L, "치킨", null, 1L, Arrays.asList(new MenuProduct()));
		assertThatThrownBy(() -> menuService.create(menu));

		menu.setPrice(BigDecimal.ZERO);
		assertThatThrownBy(() -> menuService.create(menu));
	}

	@Test
	@DisplayName("메뉴 생성 시 메뉴 그룹이 존재하지 않으면 익셉션 발생.")
	void menuCreateFailTest2() {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setQuantity(10);
		menuProduct.setProductId(1L);

		when(menuGroupDao.existsById(anyLong())).thenReturn(false);

		assertThatThrownBy(() -> menuService.create(new Menu(1L, "치킨", BigDecimal.valueOf(20000), 1L, Arrays.asList(menuProduct))))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("포함된 메뉴 상품의 총 가격이 메뉴상품의 총 가격보다 크면 익셉션 발생")
	void menuCreateFailTest3() {
		Product product = new Product();
		product.setPrice(BigDecimal.valueOf(10000));

		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setQuantity(1);
		menuProduct.setProductId(1L);

		when(menuGroupDao.existsById(anyLong())).thenReturn(true);
		when(productDao.findById(anyLong())).thenReturn(Optional.of(product));

		assertThatThrownBy(() -> menuService.create(new Menu(1L, "치킨", BigDecimal.valueOf(20000), 1L, Arrays.asList(menuProduct))))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void getMenuListTest() {
		when(menuDao.findAll()).thenReturn(Lists.list(new Menu(), new Menu()));
		assertThat(menuService.list()).hasSize(2);
	}
}
