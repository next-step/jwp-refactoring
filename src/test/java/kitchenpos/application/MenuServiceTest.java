package kitchenpos.application;

import static kitchenpos.generator.MenuGenerator.*;
import static kitchenpos.generator.ProductGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.generator.MenuProductGenerator;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

@DisplayName("메뉴 서비스 테스트")
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

	private MenuProduct 후라이드_한마리;
	private Menu 후라이드_세트;

	@BeforeEach
	void setUp() {
		후라이드_한마리 = MenuProductGenerator.메뉴_상품(1L, 1L, 1L);
		후라이드_세트 = 메뉴("후라이드세트", BigDecimal.valueOf(16000), 1L,
			Collections.singletonList(후라이드_한마리));
	}

	@DisplayName("메뉴를 등록할 수 있다.")
	@Test
	void createMenuTest() {
		// given
		given(menuGroupDao.existsById(후라이드_세트.getMenuGroupId())).willReturn(true);
		given(productDao.findById(후라이드_한마리.getProductId()))
			.willReturn(Optional.of(후라이드_치킨()));
		given(menuDao.save(any())).willReturn(후라이드_세트);

		// when
		menuService.create(후라이드_세트);

		// then
		assertAll(
			() -> 메뉴_저장됨(후라이드_세트),
			() -> 메뉴_상품_저장됨(후라이드_한마리)
		);
	}

	@DisplayName("메뉴의 가격은 반드시 존재하여야 한다.")
	@Test
	void createMenuWithoutPriceTest() {
		// given
		후라이드_세트 = 메뉴("후라이드세트", null, 1L,
			Collections.singletonList(후라이드_한마리));

		// when
		Throwable actual = catchThrowable(() -> menuService.create(후라이드_세트));

		// then
		assertThat(actual).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 가격은 0원 이상이어야 한다.")
	@Test
	void createMenuWithNegativePriceTest() {
		// given
		후라이드_세트 = 메뉴("후라이드세트", BigDecimal.valueOf(-1), 1L,
			Collections.singletonList(후라이드_한마리));

		// when
		Throwable actual = catchThrowable(() -> menuService.create(후라이드_세트));

		// then
		assertThat(actual).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 가격은 메뉴에 속한 상품들의 가격의 합보다 클 수 없다.")
	@Test
	void createMenuWithPriceGreaterThanSumOfMenuProductsTest() {
		// given
		후라이드_세트 = 메뉴("후라이드세트", BigDecimal.valueOf(20000), 1L,
			Collections.singletonList(후라이드_한마리));

		given(menuGroupDao.existsById(후라이드_세트.getMenuGroupId())).willReturn(true);
		given(productDao.findById(후라이드_한마리.getProductId()))
			.willReturn(Optional.of(후라이드_치킨()));

		// when
		Throwable actual = catchThrowable(() -> menuService.create(후라이드_세트));

		// then
		assertThat(actual).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 메뉴 그룹은 이미 저장되어 있어야 한다.")
	@Test
	void createMenuWithoutMenuGroupTest() {
		// given
		given(menuGroupDao.existsById(anyLong())).willReturn(false);

		// when
		Throwable actual = catchThrowable(() -> menuService.create(후라이드_세트));

		// then
		assertThat(actual).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 상품은 이미 저장되어 있어야 한다.")
	@Test
	void createMenuWithoutProductTest() {
		// given
		given(menuGroupDao.existsById(후라이드_세트.getMenuGroupId())).willReturn(true);
		given(productDao.findById(anyLong())).willReturn(Optional.empty());

		// when
		Throwable actual = catchThrowable(() -> menuService.create(후라이드_세트));

		// then
		assertThat(actual).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 목록을 조회할 수 있다.")
	@Test
	void menuListTest() {
		// given
		given(menuDao.findAll()).willReturn(Collections.singletonList(후라이드_세트));

		// when
		List<Menu> actual = menuService.list();

		// then
		verify(menuDao, only()).findAll();
		verify(menuProductDao, only()).findAllByMenuId(후라이드_세트.getId());
		assertThat(actual).containsExactly(후라이드_세트);
	}

	private void 메뉴_상품_저장됨(MenuProduct expectedMenuProduct) {
		ArgumentCaptor<MenuProduct> captor = ArgumentCaptor.forClass(MenuProduct.class);
		verify(menuProductDao, only()).save(captor.capture());
		assertThat(captor.getValue())
			.extracting(MenuProduct::getQuantity)
			.isEqualTo(expectedMenuProduct.getQuantity());

	}

	private void 메뉴_저장됨(Menu expectedMenu) {
		ArgumentCaptor<Menu> menuCaptor = ArgumentCaptor.forClass(Menu.class);
		verify(menuDao, only()).save(menuCaptor.capture());
		assertThat(menuCaptor.getValue())
			.extracting(Menu::getName, Menu::getPrice)
			.containsExactly(expectedMenu.getName(), expectedMenu.getPrice());
	}

	private Product 후라이드_치킨() {
		return 상품("후라이드", BigDecimal.valueOf(16000));
	}
}
