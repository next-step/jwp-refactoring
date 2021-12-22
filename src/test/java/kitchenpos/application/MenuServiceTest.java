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

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@InjectMocks
	private MenuService menuService;

	@Mock
	private MenuRepository menuRepository;
	@Mock
	private MenuGroupRepository menuGroupRepository;
	@Mock
	private MenuProductRepository menuProductRepository;
	@Mock
	private ProductRepository productRepository;

	@Test
	void create() {
		final MenuGroup menuGroup = menuGroup(1L, "메뉴그룹");
		final Product product = product(1L, "콜라", 500);
		final MenuProduct menuProduct = menuProduct(1L, null, product, 2);

		given(menuGroupRepository.existsById(any())).willReturn(true);
		given(productRepository.findById(any())).willReturn(Optional.of(product));
		given(menuRepository.save(any())).willReturn(
			menu(1L, "menu", 1_000, menuGroup, Arrays.asList(menuProduct))
		);
		given(menuProductRepository.save(any())).willReturn(menuProduct);

		final Menu createdMenu = menuService.create(
			menu(null, "menu", 1_000, menuGroup,
				Arrays.asList(menuProduct(null, null, product, 2)))
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
			menuProduct(null, null, product, 3)
		);

		final BigDecimal nullPrice = null;
		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(
				menu(null, "menu", nullPrice, menuGroup, menuProducts)
			));

		final long minusPrice = -1;
		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(
				menu(null, "menu", minusPrice, menuGroup, menuProducts)
			));
	}

	@Test
	void create_not_found_menu_group() {
		final Product product = product(1L, "콜라", 500);
		final List<MenuProduct> menuProducts = Arrays.asList(
			menuProduct(null, null, product, 2)
		);
		given(menuGroupRepository.existsById(any())).willReturn(false);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(
				menu(null, "menu", 1_000, null, menuProducts)
			));
	}

	@Test
	void create_not_found_menu_product() {
		final MenuGroup menuGroup = menuGroup(1L, "메뉴그룹");
		final List<MenuProduct> menuProducts = Arrays.asList(
			menuProduct(1L, null, null, 3)
		);

		given(menuGroupRepository.existsById(any())).willReturn(true);
		given(productRepository.findById(any())).willReturn(Optional.empty());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(
				menu(null, "menu", 300, menuGroup, menuProducts)
			));
	}

	@Test
	void create_menu_price_bigger_than_sum_products() {
		final MenuGroup menuGroup = menuGroup(1L, "메뉴그룹");
		final Product product = product(1L, "콜라", 500);
		final List<MenuProduct> menuProducts = Arrays.asList(
			menuProduct(1L, null, product, 2)
		);

		given(menuGroupRepository.existsById(any())).willReturn(true);
		given(productRepository.findById(any())).willReturn(Optional.of(product));

		final long invalidMenuPrice = Long.MAX_VALUE;
		assertThatIllegalArgumentException()
			.isThrownBy(() -> menuService.create(
				menu(null, "menu", invalidMenuPrice, menuGroup, menuProducts)
			));
	}

	@Test
	void list() {
		final MenuGroup menuGroup1 = menuGroup(1L, "메뉴그룹1");
		final Product product = product(1L, "사이다", 300);
		final List<MenuProduct> menuProducts = Arrays.asList(menuProduct(null, null, product, 3));
		final Menu menu1 = menu(1L, "menu1", 900, menuGroup1, menuProducts);
		final MenuGroup menuGroup2 = menuGroup(2L, "메뉴그룹2");
		final Menu menu2 = menu(2L, "menu2", 900, menuGroup2, menuProducts);

		given(menuRepository.findAll()).willReturn(Arrays.asList(menu1, menu2));
		given(menuProductRepository.findAllByMenuId(any())).willReturn(menuProducts);

		assertThat(menuService.list()).containsExactly(menu1, menu2);
	}
}
