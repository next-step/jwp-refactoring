package kitchenpos.menu.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    private MenuValidator menuValidator;

    private Product 후라이드;

    @BeforeEach
    void setUp() {
        menuValidator = new MenuValidator(menuGroupRepository, productRepository);
        후라이드 = Product.of("후라이드", new BigDecimal(16000));
    }

    @DisplayName("메뉴 그룹이 존재하고 메뉴 가격이 총 상품 가격보다 작아야 메뉴 등록이 가능하다.")
    @ParameterizedTest
    @ValueSource(ints = {16000, 32000})
    void validateCreate(int price) {
        // given
        long menuGroupId = 1L;
        Menu menu = Menu.of("반반치킨", new BigDecimal(price), menuGroupId,
            Collections.singletonList(
                MenuProduct.of(후라이드.getId(), 2)));

        given(menuGroupRepository.findById(menuGroupId)).willReturn(Optional.of(new MenuGroup("반반메뉴")));
        given(productRepository.findById(후라이드.getId())).willReturn(Optional.of(후라이드));

        // when && then
        assertDoesNotThrow(() -> menuValidator.validateCreate(menu));
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 메뉴를 등록할 수 없다.")
    @Test
    void validateCreateNotExistMenuGroup() {
        // given
        long menuGroupId = 1L;
        Menu menu = Menu.of("반반치킨", new BigDecimal(16000), menuGroupId,
            Collections.singletonList(
                MenuProduct.of(후라이드.getId(), 2)));

        given(menuGroupRepository.findById(menuGroupId)).willReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> menuValidator.validateCreate(menu))
            .isInstanceOf(NotFoundException.class)
            .hasMessage(NOT_FOUND_DATA.getMessage());
    }

    @DisplayName("메뉴의 가격이 상품의 총 가격보다 크면 메뉴 등록을 할 수 없다.")
    @Test
    void validateCreateIsGreaterThanSumPrice() {
        // given
        long menuGroupId = 1L;
        Menu menu = Menu.of("반반치킨", new BigDecimal(32001), menuGroupId,
            Collections.singletonList(
                MenuProduct.of(후라이드.getId(), 2)));

        given(menuGroupRepository.findById(menuGroupId)).willReturn(Optional.of(new MenuGroup("반반메뉴")));
        given(productRepository.findById(후라이드.getId())).willReturn(Optional.of(후라이드));

        // when && then
        assertThatThrownBy(() -> menuValidator.validateCreate(menu))
            .isInstanceOf(BadRequestException.class)
            .hasMessage(WRONG_VALUE.getMessage());
    }
}
