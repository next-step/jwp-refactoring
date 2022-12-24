package ktichenpos.menu.application;

import static kitchenpos.generator.MenuGenerator.*;
import static kitchenpos.generator.MenuGroupGenerator.*;
import static kitchenpos.generator.ProductGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock 
	private MenuRepository menuRepository;
	@Mock 
	private MenuGroupRepository menuGroupRepository;
	@Mock 
	private ProductRepository productRepository;

	@InjectMocks
	private MenuService menuService;

	@DisplayName("메뉴를 등록할 수 있다.")
	@Test
	void createMenuTest() {
		// given
		MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
		MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.ONE, 1L,
			Collections.singletonList(menuProductRequest));

		MenuGroup 한마리_메뉴 = 한마리_메뉴();
		given(menuGroupRepository.menuGroup(anyLong())).willReturn(한마리_메뉴);

		Product 후라이드_치킨 = 후라이드_치킨();
		given(productRepository.product(menuProductRequest.getProductId())).willReturn(후라이드_치킨);

		Menu 후라이드_세트 = 후라이드_세트();
		given(menuRepository.save(any())).willReturn(후라이드_세트);

		// when
		MenuResponse menuResponse = menuService.create(menuRequest);

		// then
		verify(menuRepository, times(1)).save(any());
		assertThat(menuResponse).satisfies(response -> {
			assertThat(response.getId()).isEqualTo(후라이드_세트.id());
			assertThat(response.getName()).isEqualTo(후라이드_세트.name());
			assertThat(response.getPrice()).isEqualTo(후라이드_세트.price());
			assertThat(response.getMenuGroupId()).isEqualTo(후라이드_세트.menuGroup().id() );
			assertThat(response.getMenuProducts()).hasSize(1);
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
		given(menuGroupRepository.menuGroup(anyLong())).willThrow(NotFoundException.class);

		// when
		Throwable actual = catchThrowable(() -> menuService.create(menuRequest));

		// then
		assertThat(actual).isInstanceOf(NotFoundException.class);
	}

	@DisplayName("메뉴의 상품은 이미 저장되어 있어야 한다.")
	@Test
	void createMenuWithoutProductTest() {
		// given
		MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1L);
		MenuRequest menuRequest = new MenuRequest("후라이드 세트", BigDecimal.valueOf(20000), 1L,
			Collections.singletonList(menuProductRequest));

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
		given(menuRepository.findAll()).willReturn(Collections.singletonList(후라이드_세트));

		// when
		menuService.list();

		// then
		verify(menuRepository, only()).findAll();
	}
}
