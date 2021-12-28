package kitchenpos.menu.service;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.application.MenuProductService;
import kitchenpos.menu.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 검증 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {
    @InjectMocks
    private MenuValidator menuValidator;

    @Mock
    private MenuProductService menuProductService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹은 필수 입력 항목 저장소에 없음 예외")
    @Test
    void 메뉴_그룹_존재_검증() {
        Menu menu = Menu.of("양념치킨 세트", Price.of(BigDecimal.valueOf(5000)), 1L, new ArrayList<>());

        given(menuGroupRepository.findById(menu.getMenuGroupId())).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> menuValidator.validate(menu));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹은 필수 입력 항목입니다.");
    }

    @DisplayName("메뉴 상품은 필수 입력 항목 저장소에 없음 예외")
    @Test
    void 메뉴_상품_존재_검증() {
        Menu menu = Menu.of("양념치킨 세트", Price.of(BigDecimal.valueOf(5000)), 1L, new ArrayList<>());

        given(menuGroupRepository.findById(menu.getMenuGroupId())).willReturn(Optional.of(MenuGroup.of("튀김류")));

        Throwable thrown = catchThrowable(() -> menuValidator.validate(menu));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 상품 목록이 비어있습니다.");
    }

    @DisplayName("메뉴의 가격이 음수일 경우 예외")
    @Test
    void 메뉴_가격_음수_검증() {
        MenuProduct menuProduct = MenuProduct.of(1L, Quantity.of(1));
        Menu menu = Menu.of("양념치킨 세트", Price.of(BigDecimal.valueOf(-1)), 1L, Collections.singletonList(menuProduct));

        given(menuGroupRepository.findById(menu.getMenuGroupId())).willReturn(Optional.of(MenuGroup.of("튀김류")));

        Throwable thrown = catchThrowable(() -> menuValidator.validate(menu));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 음수가 될 수 없습니다.");
    }

    @DisplayName("메뉴의 가격이 상품 가격의 합계보다 클 수 없음 예외")
    @Test
    void 메뉴_가격_검증() {
        MenuProduct menuProduct = MenuProduct.of(1L, Quantity.of(1));
        Menu menu = Menu.of("양념치킨 세트", Price.of(BigDecimal.valueOf(5000)), 1L, Collections.singletonList(menuProduct));

        given(menuGroupRepository.findById(menu.getMenuGroupId())).willReturn(Optional.of(MenuGroup.of("튀김류")));
        given(menuProductService.calculateTotalPrice(menu.getMenuProducts())).willReturn(BigDecimal.valueOf(4900));

        Throwable thrown = catchThrowable(() -> menuValidator.validate(menu));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격이 상품 가격의 합계보다 클 수 없습니다.");
    }
}
