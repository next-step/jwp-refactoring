package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.ErrorCode;
import kitchenpos.common.PriceException;
import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductException;

@DisplayName("메뉴 : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@Mock
	MenuRepository menuRepository;

	@Mock
	MenuGroupRepository menuGroupRepository;

	@Mock
	ProductRepository productRepository;

	@InjectMocks
	private MenuService menuService;

	private MenuProduct menuProduct;

	private MenuGroup menuGroup;

	private Product product;

	private Menu menu;

	private MenuRequest menuRequest;

	@BeforeEach
	void setup() {
		menuGroup = MenuGroup.from("두마리");
		product = Product.of("불닭", 16000);
	}

	@DisplayName("메뉴 생성 테스트")
	@Test
	void createMenu() {
		// given
		menuRequest = MenuRequest.of("불닭메", 21000, 1L,
			Collections.singletonList(MenuProductRequest.of(1L, 2L)));

		given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));
		given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

		// when
		Menu menu = menuService.create(menuRequest);

		// then
		assertThat(menuService.create(menuRequest)).isEqualTo(menu);
	}

	@DisplayName("메뉴의 가격이 존재하지 않을 경우 생성시 예외처리 테스트")
	@Test
	void createMenuNullPrice() {
		// given
		menuRequest = MenuRequest.of("불닭메", (Integer)null, 1L,
			Collections.singletonList(MenuProductRequest.of(1L, 2L)));

		given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));

		// when // then
		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(PriceException.class)
			.hasMessageContaining(ErrorCode.PRICE_IS_NOT_NULL.getMessage());
	}

	@DisplayName("메뉴의 가격이 0이하일 경우 생성시 예외처리 테스트")
	@Test
	void createUnderZeroPrice() {
		// given
		menuRequest = MenuRequest.of("불닭메", -100, 1L,
			Collections.singletonList(MenuProductRequest.of(1L, 2L)));

		given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));
		// when // then
		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(PriceException.class)
			.hasMessageContaining(ErrorCode.PRICE_NOT_NEGATIVE_NUMBER.getMessage());
	}

	@DisplayName("메뉴에 등록할 메뉴 그룹이 존재하지 않을 경우 생성시 예외처리 테스트")
	@Test
	void createMenuUnknownMenuGroup() {
		// given
		menuRequest = MenuRequest.of("불닭메", 160000, 1L,
			Collections.singletonList(MenuProductRequest.of(1L, 2L)));

		// when // then
		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(MenuException.class)
			.hasMessageContaining(ErrorCode.MENU_GROUP_IS_NOT_NULL.getMessage());
	}

	@DisplayName("메뉴에 등록할 상품이 존재하지 않을 경우 생성시 예외처리 테스트")
	@Test
	void createMenuUnknownProduct() {
		// given
		menuRequest = MenuRequest.of("불닭메", 160000, 1L,
			Collections.singletonList(MenuProductRequest.of(1L, 2L)));

		given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));

		// when // then
		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(ProductException.class)
			.hasMessageContaining(ErrorCode.PRODUCT_IS_NULL.getMessage());
	}

	@DisplayName("메뉴의 가격이 메뉴 상품들의 가격을 모두 더한 값보다 클 경우 생성시 예외처리 테스트")
	@Test
	void createMenuPriceUnderSumPrice() {
		// given
		menuRequest = MenuRequest.of("불닭메", 80000, 1L,
			Collections.singletonList(MenuProductRequest.of(1L, 2L)));
		given(menuGroupRepository.findById(anyLong())).willReturn(Optional.of(menuGroup));
		given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

		// when // then
		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(PriceException.class)
			.hasMessageContaining(ErrorCode.PRODUCT_PRICE_IS_UNDER_SUM_PRICE.getMessage());
	}

	@DisplayName("메뉴 목록 조회 테스트")
	@Test
	void getList() {
		// given
		menu = Menu.of("불닭 메뉴", Price.from(16000), menuGroup, Collections.singletonList(menuProduct));

		// when
		when(menuRepository.findAll()).thenReturn(Collections.singletonList(menu));

		// then
		assertThat(menuService.list()).containsExactly(menu);
	}
}
