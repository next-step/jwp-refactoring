package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.menugroup.domain.MenuGroupRepository;

@DisplayName("메뉴 Validator : 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

	@Mock
	MenuGroupRepository menuGroupRepository;

	@InjectMocks
	private MenuValidator menuValidator;

	@DisplayName("메뉴 그룹이 존재하지 않을 경우 예외처리 테스트")
	@Test
	void existedMenuGroupTest() {
		// given
		MenuRequest menuRequest = MenuRequest.of("메뉴", 100, 2L, Collections.emptyList());

		// when
		when(menuGroupRepository.existsById(anyLong())).thenReturn(false);

		// then
		assertThatThrownBy(() -> {
			menuValidator.validate(menuRequest);
		}).isInstanceOf(MenuException.class)
			.hasMessageContaining(ErrorCode.MENU_GROUP_IS_NOT_NULL.getMessage());
	}

	@DisplayName("가격이 null일 경우 예외처리 테스트")
	@Test
	void validateNullPriceTest() {
		// given
		given(menuGroupRepository.existsById(anyLong())).willReturn(true);

		// when
		MenuRequest menuRequest = MenuRequest.of("메뉴", null, 2L, Collections.emptyList());

		// then
		assertThatThrownBy(() -> {
			menuValidator.validate(menuRequest);
		}).isInstanceOf(MenuException.class)
			.hasMessageContaining(ErrorCode.PRICE_IS_NOT_NULL.getMessage());
	}

	@DisplayName("메뉴의 가격이 상품 가격보다 클 때 예외처리 테스트")
	@Test
	void validateMenuPriceTest() {
		// given
		given(menuGroupRepository.existsById(anyLong())).willReturn(true);
		MenuRequest menuRequest = MenuRequest.of("메뉴", 15000, 2L, Collections.emptyList());

		// when // then
		assertThatThrownBy(() -> {
			menuValidator.validate(menuRequest);
		}).isInstanceOf(MenuException.class)
			.hasMessageContaining(ErrorCode.PRODUCT_PRICE_IS_UNDER_SUM_PRICE.getMessage());
	}
}
