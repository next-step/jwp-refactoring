package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.PriceException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.MenuException;

@DisplayName("메뉴 : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@Mock
	MenuRepository menuRepository;

	@Mock
	MenuValidator menuValidator;

	@Mock
	Menu menu;

	@InjectMocks
	private MenuService menuService;

	private MenuRequest menuRequest;

	@DisplayName("메뉴 생성 테스트")
	@Test
	void createMenu() {
		// given
		menuRequest = MenuRequest.of("불닭메뉴", 21000, 1L,
			Collections.singletonList(MenuProductRequest.of(1L, 2L)));

		// when
		doNothing()
			.when(menuValidator)
			.validate(menuRequest);
		doReturn(menu)
			.when(menuRepository)
			.save(any(Menu.class));

		// then
		assertThat(menuService.create(menuRequest)).isEqualTo(menu);
	}

	@DisplayName("메뉴의 가격이 존재하지 않을 경우 생성시 예외처리 테스트")
	@Test
	void createMenuNullPrice() {
		// given
		menuRequest = MenuRequest.of("불닭메", (Integer)null, 1L,
			Collections.singletonList(MenuProductRequest.of(1L, 2L)));

		// when
		doThrow(new MenuException(ErrorCode.PRICE_IS_NOT_NULL))
			.when(menuValidator)
			.validate(menuRequest);

		// then
		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(MenuException.class)
			.hasMessageContaining(ErrorCode.PRICE_IS_NOT_NULL.getMessage());
	}

	@DisplayName("메뉴의 가격이 0이하일 경우 생성시 예외처리 테스트")
	@Test
	void createUnderZeroPrice() {
		// given
		menuRequest = MenuRequest.of("불닭메", -100, 1L,
			Collections.singletonList(MenuProductRequest.of(1L, 2L)));

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

		// when
		doThrow(new MenuException(ErrorCode.MENU_GROUP_IS_NOT_NULL))
			.when(menuValidator)
			.validate(menuRequest);

		// then
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

		// when
		doThrow(new MenuException(ErrorCode.PRODUCT_IS_NULL))
			.when(menuValidator)
			.validate(menuRequest);

		// then
		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(MenuException.class)
			.hasMessageContaining(ErrorCode.PRODUCT_IS_NULL.getMessage());
	}

	@DisplayName("메뉴의 가격이 메뉴 상품들의 가격을 모두 더한 값보다 클 경우 생성시 예외처리 테스트")
	@Test
	void createMenuPriceUnderSumPrice() {
		// given
		menuRequest = MenuRequest.of("불닭메", 80000, 1L,
			Collections.singletonList(MenuProductRequest.of(1L, 2L)));

		// when
		doThrow(new MenuException(ErrorCode.PRODUCT_PRICE_IS_UNDER_SUM_PRICE))
			.when(menuValidator)
			.validate(menuRequest);

		// when // then
		assertThatThrownBy(() -> {
			menuService.create(menuRequest);
		}).isInstanceOf(MenuException.class)
			.hasMessageContaining(ErrorCode.PRODUCT_PRICE_IS_UNDER_SUM_PRICE.getMessage());
	}

	@DisplayName("메뉴 목록 조회 테스트")
	@Test
	void getList() {
		// when
		when(menuRepository.findAll()).thenReturn(Collections.singletonList(menu));

		// then
		assertThat(menuService.list()).containsExactly(menu);
	}
}
