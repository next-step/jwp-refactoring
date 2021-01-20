package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

	private MenuGroup food;
	private Product 치킨;
	private Product 피자;
	private List<MenuProduct> products;

	@BeforeEach
	void setUp() {
		food = MenuGroup.of(1L, "세트");
		치킨 = Product.of(1L, "치킨", 16000);
		피자 = Product.of(2L, "피자", 20000);
		products = Arrays.asList(MenuProduct.of(치킨, 2), MenuProduct.of(피자, 1));
	}

	@DisplayName("메뉴: 메뉴 생성 테스트")
	@Test
	void createTest() {
		// given
		Menu menu = Menu.of(1L, "치피세트", 50000, food.getId(), products);
		given(menuGroupDao.existsById(1L)).willReturn(true);
		given(productDao.findById(1L)).willReturn(Optional.of(치킨));
		given(productDao.findById(2L)).willReturn(Optional.of(피자));
		given(menuDao.save(any())).willReturn(menu);
		given(menuProductDao.save(products.get(0))).willReturn(MenuProduct.of(1L, 1L, 1L, 2));
		given(menuProductDao.save(products.get(1))).willReturn(MenuProduct.of(2L, 1L, 2L, 1));

		// when
		Menu actual = menuService.create(menu);

		// then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getId()).isEqualTo(menu.getId()),
			() -> assertThat(actual.getName()).isEqualTo(menu.getName())
		);
	}

	@DisplayName("메뉴[예외]: 메뉴 생성 테스트(1. 메뉴 가격은 null이 아니고, 0보다 커야한다.)")
	@ParameterizedTest
	@ValueSource(longs = {-500L, 0})
	void errorMenuPriceTest(Long price) {
		// given // when
		Menu menu = Menu.of(1L, "치피세트", price, food.getId(), products);

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> menuService.create(menu)
		);
	}

	@DisplayName("메뉴[예외]: 메뉴 생성 테스트(2. 메뉴 그룹이 존재해야 한다.)")
	@Test
	void errorNotFoundMenuGroupTest() {
		// given
		Menu menu = Menu.of(1L, "치피세트", 50000, food.getId(), products);

		// when
		when(menuGroupDao.existsById(1L)).thenReturn(false);

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> menuService.create(menu)
		);
	}

	@DisplayName("메뉴[예외]: 메뉴 생성 테스트(3. 상품이 존재해야 한다.)")
	@Test
	void errorNotFoundProductTest() {
		// given
		Menu menu = Menu.of(1L, "치피세트", 52000, food.getId(), products);

		// when
		when(menuGroupDao.existsById(1L)).thenReturn(true);
		when(productDao.findById(any())).thenThrow(IllegalArgumentException.class);

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> menuService.create(menu)
		);
	}

	@DisplayName("메뉴[예외]: 메뉴 생성 테스트(4. 메뉴 가격이 상품들의 총 합보다 작거나 같아야 한다.)")
	@Test
	void errorTotalPriceTest() {
		// given
		Menu menu = Menu.of(1L, "치피세트", 53000, food.getId(), products);

		// when
		when(menuGroupDao.existsById(1L)).thenReturn(true);
		when(productDao.findById(1L)).thenReturn(Optional.of(치킨));
		when(productDao.findById(2L)).thenReturn(Optional.of(피자));

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> menuService.create(menu)
		);
	}
}
