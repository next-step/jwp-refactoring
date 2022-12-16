package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import kitchenpos.exception.InvalidMenuPriceException;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	MenuRepository menuRepository;

	@InjectMocks
	MenuService menuService;

	Menu menu;
	MenuGroup menuGroup;
	List<Product> products;

	@BeforeEach
	void setUp() {
		products = createProducts(3);
		menuGroup = new MenuGroup("menu-group");
		menu = new Menu("menu", 3_000L, menuGroup, products);
	}

	@Test
	@DisplayName("메뉴 생성 성공")
	void testCreateMenu() {
		when(menuRepository.save(menu)).thenReturn(menu);

		menuService.create(menu);

		verify(menuRepository, times(1)).save(menu);
	}

	@Test
	@DisplayName("메뉴의 가격이 상품목록 가격 합보다 클 경우 등록 실패")
	void testCreateMenuWhenMenuPriceGreaterThanSumOfProductsPrice() {
		Money invalidMenuPrice = sumProductsPrice(products).minus(1);
		Menu invalidMenu = new Menu(menu.getName(), invalidMenuPrice.longValue(), menuGroup, products);

		assertThatThrownBy(() -> menuService.create(invalidMenu))
			.isInstanceOf(InvalidMenuPriceException.class);
	}

	private Money sumProductsPrice(List<Product> products) {
		return products.stream()
			.map(Product::getPrice)
			.reduce(Money.ZERO, Money::add);
	}

	@Test
	@DisplayName("메뉴 목록 조회 성공")
	void testGetMenuList() {
		when(menuRepository.findAll()).thenReturn(Lists.newArrayList(menu));

		List<Menu> menus = menuService.findAll();

		assertThat(menus).isNotEmpty();
		assertThat(menus).containsExactly(menu);
		verify(menuRepository, times(1)).findAll();
	}

	private List<Product> createProducts(int count) {
		return LongStream.range(0, count)
			.mapToObj(i -> new Product(i, "product-"+i, 1_000))
			.collect(Collectors.toList());
	}
}
