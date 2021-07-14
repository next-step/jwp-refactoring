package kitchenpos.menu.domain;

import static java.util.Collections.*;
import static kitchenpos.menu.domain.MenuGroupTest.*;
import static kitchenpos.menu.domain.MenuTest.*;
import static kitchenpos.product.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.generic.price.domain.Price;
import kitchenpos.generic.quantity.domain.Quantity;
import kitchenpos.menu.exception.ExceedingTotalPriceException;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.product.domain.ProductRepository;

@DisplayName("메뉴 밸리데이터 테스트")
@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @InjectMocks
    MenuValidator validator;

    @Mock
    ProductRepository productRepository;
    @Mock
    MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("실패 - 메뉴 그룹이 존재하지 않음")
    void create_fail1() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validator.validate(후라이드_메뉴, singletonList(후라이드)))
            .isInstanceOf(MenuGroupNotFoundException.class);
    }

    @Test
    @DisplayName("실패 - 단품 가격의 합을 초과")
    void create_fail2() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(한마리메뉴));
        Menu 신_메뉴 = new Menu("신-메뉴", Price.valueOf(50000),
            MenuProducts.of(new MenuProduct(후라이드.getId(), Quantity.valueOf(1))));

        assertThatThrownBy(() -> validator.validate(신_메뉴, singletonList(후라이드)))
            .isInstanceOf(ExceedingTotalPriceException.class);
    }

    @Test
    @DisplayName("실패 - 제품 정보 없음")
    void create_fail3() {
        when(menuGroupRepository.findById(any())).thenReturn(Optional.of(한마리메뉴));
        Menu 신_메뉴 = new Menu("신-메뉴", Price.valueOf(50000), MenuProducts.of(Collections.emptyList()));

        assertThatThrownBy(() -> validator.validate(신_메뉴, singletonList(후라이드)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
