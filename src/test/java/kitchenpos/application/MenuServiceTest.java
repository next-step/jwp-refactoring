package kitchenpos.application;

import static kitchenpos.generator.MenuGenerator.*;
import static kitchenpos.generator.ProductGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import kitchenpos.generator.MenuGenerator;
import kitchenpos.generator.MenuProductGenerator;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;

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

	@DisplayName("메뉴를 등록할 수 있다.")
	@Test
	void createMenuTest() {
		// given
		MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
		MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(16000), 1L,
			Collections.singletonList(menuProductRequest));

		Menu 후라이드_세트 = MenuGenerator.후라이드_세트();
		given(menuGroupDao.existsById(menuRequest.getMenuGroupId())).willReturn(true);
		given(productDao.findById(menuProductRequest.getProductId()))
			.willReturn(Optional.of(후라이드_치킨()));
		given(menuDao.save(any())).willReturn(후라이드_세트);
		MenuProduct 후라이드_한마리 = MenuProductGenerator.메뉴_상품(1L, 1L, 1L);
		given(menuProductDao.save(any())).willReturn(후라이드_한마리);

		// when
		MenuResponse menuResponse = menuService.create(menuRequest);

		// then
		verify(menuDao, times(1)).save(any());
		assertThat(menuResponse).satisfies(response -> {
			assertThat(response.getId()).isEqualTo(후라이드_세트.getId());
			assertThat(response.getName()).isEqualTo(후라이드_세트.getName());
			assertThat(response.getPrice()).isEqualTo(후라이드_세트.getPrice());
			assertThat(response.getMenuGroupId()).isEqualTo(후라이드_세트.getMenuGroupId());
			assertThat(response.getMenuProducts()).hasSize(1);
			assertThat(response.getMenuProducts().get(0).getProductId())
				.isEqualTo(후라이드_한마리.getProductId());
			assertThat(response.getMenuProducts().get(0).getQuantity())
				.isEqualTo(후라이드_한마리.getQuantity());
		});
	}

	@DisplayName("메뉴의 가격은 반드시 존재하여야 한다.")
	@Test
	void createMenuWithoutPriceTest() {
		// given
		MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
		MenuRequest menuRequest = new MenuRequest("후라이드 세트", null, 1L,
			Collections.singletonList(menuProductRequest));

		// when
		Throwable actual = catchThrowable(() -> menuService.create(menuRequest));

		// then
		assertThat(actual).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 가격은 0원 이상이어야 한다.")
	@Test
	void createMenuWithNegativePriceTest() {
		// given
		MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
		MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(-1), 1L,
			Collections.singletonList(menuProductRequest));

		// when
		Throwable actual = catchThrowable(() -> menuService.create(menuRequest));

		// then
		assertThat(actual).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 가격은 메뉴에 속한 상품들의 가격의 합보다 클 수 없다.")
	@Test
	void createMenuWithPriceGreaterThanSumOfMenuProductsTest() {
		// given
		MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
		MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(20000), 1L,
			Collections.singletonList(menuProductRequest));

		given(menuGroupDao.existsById(menuRequest.getMenuGroupId())).willReturn(true);
		given(productDao.findById(menuProductRequest.getProductId()))
			.willReturn(Optional.of(후라이드_치킨()));

		// when
		Throwable actual = catchThrowable(() -> menuService.create(menuRequest));

		// then
		assertThat(actual).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 메뉴 그룹은 이미 저장되어 있어야 한다.")
	@Test
	void createMenuWithoutMenuGroupTest() {
		// given
		MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
		MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(20000), 1L,
			Collections.singletonList(menuProductRequest));

		given(menuGroupDao.existsById(anyLong())).willReturn(false);

		// when
		Throwable actual = catchThrowable(() -> menuService.create(menuRequest));

		// then
		assertThat(actual).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 상품은 이미 저장되어 있어야 한다.")
	@Test
	void createMenuWithoutProductTest() {
		// given
		MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
		MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(20000), 1L,
			Collections.singletonList(menuProductRequest));
		given(menuGroupDao.existsById(menuRequest.getMenuGroupId())).willReturn(true);
		given(productDao.findById(anyLong())).willReturn(Optional.empty());

		// when
		Throwable actual = catchThrowable(() -> menuService.create(menuRequest));

		// then
		assertThat(actual).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 목록을 조회할 수 있다.")
	@Test
	void menuListTest() {
		// given
		Menu 후라이드_세트 = 후라이드_세트();
		given(menuDao.findAll()).willReturn(Collections.singletonList(후라이드_세트));

		// when
		List<Menu> actual = menuService.list();

		// then
		verify(menuDao, only()).findAll();
		verify(menuProductDao, only()).findAllByMenuId(후라이드_세트.getId());
		assertThat(actual).containsExactly(후라이드_세트);
	}

	private void 메뉴_상품_저장됨(MenuProductRequest expectedMenuProduct) {
		ArgumentCaptor<MenuProduct> captor = ArgumentCaptor.forClass(MenuProduct.class);
		verify(menuProductDao, only()).save(captor.capture());
		assertThat(captor.getValue())
			.extracting(MenuProduct::getQuantity)
			.isEqualTo(expectedMenuProduct.getQuantity());

	}

	private void 메뉴_저장됨(MenuRequest menuRequest) {
		ArgumentCaptor<Menu> menuCaptor = ArgumentCaptor.forClass(Menu.class);
		verify(menuDao, only()).save(menuCaptor.capture());
		assertThat(menuCaptor.getValue())
			.extracting(Menu::getName, Menu::getPrice)
			.containsExactly(menuRequest.getName(), menuRequest.getPrice());
		;
	}

	private Product 후라이드_치킨() {
		return 상품("후라이드", BigDecimal.valueOf(16000));
	}
}
