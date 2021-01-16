package kitchenpos.mockito;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Disabled
public class MenuServiceTest {

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private MenuGroupRepository menuGroupRepository;

	@Mock
	private MenuProductRepository menuProductRepository;

	@Mock
	private ProductRepository productRepository;

	private MenuService menuService;

	@Mock
	private Menu menu;

	@BeforeEach
	void setUp() {
		menuService = new MenuService(menuRepository, menuGroupRepository, menuProductRepository, productRepository);
		assertThat(menuService).isNotNull();
		menu = mock(Menu.class);
	}

	@Test
	@DisplayName("메뉴를 등록한다")
	void create(){
		given(menu.getId()).willReturn(1L);
		given(menu.getPrice()).willReturn(BigDecimal.valueOf(10000));
		given(menuGroupRepository.existsById(any())).willReturn(true);

		Product product = mock(Product.class);
		given(product.getPrice()).willReturn(BigDecimal.valueOf(10000));
		given(productRepository.findById(any())).willReturn(java.util.Optional.of(product));
//		given(productDao.sumPriceByIdIn(any())).willReturn(BigDecimal.valueOf(10000));

		List<MenuProduct> menuProducts = new ArrayList<>();
		MenuProduct menuProduct = mock(MenuProduct.class);
		given(menuProduct.getQuantity()).willReturn(2L);
		menuProducts.add(menuProduct);
		given(menu.getMenuProducts()).willReturn(menuProducts);

		given(menuProductRepository.save(menuProduct)).willReturn(menuProduct);
		given(menuRepository.save(menu)).willReturn(menu);
//		assertThat(menuService.create(menu)).isEqualTo(menu);
	}

	@Test
	@DisplayName("메뉴 등록 시 가격이 null 또는 0 미만이면 에러")
	void givenPriceUnderZeroOrNullWhenCreateMenuThenError(){
		given(menu.getPrice()).willReturn(BigDecimal.valueOf(0));
//		assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));

		Menu menu2 = mock(Menu.class);
		given(menu2.getPrice()).willReturn(null);
//		assertThrows(IllegalArgumentException.class, () -> menuService.create(menu2));

	}
	@Test
	@DisplayName("메뉴 목록을 조회한다")
	void list(){
		List<Menu> menus = new ArrayList<>(Arrays.asList(mock(Menu.class), mock(Menu.class)));
		given(menuRepository.findAll()).willReturn(menus);

		assertThat(menuService.list()).isNotNull();
		assertThat(menuService.list()).isNotEmpty();
		assertThat(menuService.list().size()).isEqualTo(2);
	}
}
