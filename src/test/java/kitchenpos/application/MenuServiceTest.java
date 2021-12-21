package kitchenpos.application;

import static kitchenpos.common.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

	@Test
	void create() {
		final MenuGroup menuGroup = menuGroup(1L, "메뉴그룹");
		final Product product = product(1L, "콜라", 500);
		final MenuProduct menuProduct = menuProduct(1L, product.getId(), 2);

		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(any())).willReturn(Optional.of(product));
		given(menuDao.save(any())).willReturn(
			menu(1L, "menu", 1_000, menuGroup.getId(), Arrays.asList(menuProduct))
		);
		given(menuProductDao.save(any())).willReturn(menuProduct);

		final Menu createdMenu = menuService.create(
			menu(null, "menu", 1_000, menuGroup.getId(),
				Arrays.asList(menuProduct(null, product.getId(), 2)))
		);

		assertThat(createdMenu.getId()).isNotNull();
		assertThat(createdMenu.getMenuProducts().size()).isEqualTo(1);
		assertThat(createdMenu.getMenuProducts().get(0).getSeq()).isNotNull();
	}

	@Test
	void create_invalid_menu_price() {
		final MenuGroup menuGroup = menuGroup(1L, "메뉴그룹");
		final Product product = product(1L, "사이다", 300);
		final List<MenuProduct> menuProducts = Arrays.asList(
			menuProduct(null, product.getId(), 3)
		);

		final BigDecimal nullPrice = null;
		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(
				menu(null, "menu", nullPrice, menuGroup.getId(), menuProducts)
			));

		final long minusPrice = -1;
		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(
				menu(null, "menu", minusPrice, menuGroup.getId(), menuProducts)
			));
	}

	@Test
	void create_not_found_menu_group() {
		final Product product = product(1L, "콜라", 500);
		final List<MenuProduct> menuProducts = Arrays.asList(
			menuProduct(null, product.getId(), 2)
		);
		given(menuGroupDao.existsById(any())).willReturn(false);

		final Long notFoundMenuGroupId = 1L;
		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(
				menu(null, "menu", 1_000, notFoundMenuGroupId, menuProducts)
			));
	}

	@Test
	void create_not_found_menu_product() {
		final MenuGroup menuGroup = menuGroup(1L, "메뉴그룹");
		final Long notFoundProductId = 1L;
		final List<MenuProduct> menuProducts = Arrays.asList(
			menuProduct(1L, notFoundProductId, 3)
		);

		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(any())).willReturn(Optional.empty());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(
				menu(null, "menu", 300, menuGroup.getId(), menuProducts)
			));
	}

	@Test
	void create_menu_price_bigger_than_sum_products() {
		final MenuGroup menuGroup = menuGroup(1L, "메뉴그룹");
		final Product product = product(1L, "콜라", 500);
		final List<MenuProduct> menuProducts = Arrays.asList(
			menuProduct(1L, product.getId(), 2)
		);

		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(any())).willReturn(Optional.of(product));

		final long invalidMenuPrice = Long.MAX_VALUE;
		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(
				menu(null, "menu", invalidMenuPrice, menuGroup.getId(), menuProducts)
			));
	}

	@Test
	void list() {
		final MenuGroup menuGroup1 = menuGroup(1L, "메뉴그룹1");
		final Product product = product(1L, "사이다", 300);
		final List<MenuProduct> menuProducts = Arrays.asList(menuProduct(null, product.getId(), 3));
		final Menu menu1 = menu(1L, "menu1", 900, menuGroup1.getId(), menuProducts);
		final MenuGroup menuGroup2 = menuGroup(2L, "메뉴그룹2");
		final Menu menu2 = menu(2L, "menu2", 900, menuGroup2.getId(), menuProducts);

		given(menuDao.findAll()).willReturn(Arrays.asList(menu1, menu2));
		given(menuProductDao.findAllByMenuId(any())).willReturn(menuProducts);

		assertThat(menuService.list()).containsExactly(menu1, menu2);
	}
}
