package kitchenpos.menu.application;

import static kitchenpos.menu.domain.MenuFixture.*;
import static kitchenpos.menu.domain.MenuProductFixture.*;
import static kitchenpos.product.domain.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@DisplayName("메뉴 유효성 검사")
@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuValidator menuValidator;

    @DisplayName("메뉴 유효성 검사 - 가격 없음")
    @Test
    void validate_price_null() {
        // given
        MenuProductRequest menuProduct = menuProductRequest(1L, 2L);
        MenuRequest menuRequest = menuRequest("후라이드+후라이드", null, 1L, Collections.singletonList(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 유효성 검사 - 가격 0원")
    @Test
    void validate_price_zero() {
        // given
        MenuProductRequest menuProduct = menuProductRequest(1L, 2L);
        MenuRequest menuRequest = menuRequest("후라이드+후라이드", BigDecimal.ZERO, 1L, Collections.singletonList(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 유효성 검사 - 메뉴 그룹 존재 하지 않음")
    @Test
    void validate_menu_group_not_exists() {
        // given
        MenuProductRequest menuProduct = menuProductRequest(1L, 2L);
        MenuRequest menuRequest = menuRequest("후라이드+후라이드", BigDecimal.valueOf(17000), 1L,
            Collections.singletonList(menuProduct));

        given(menuGroupRepository.existsById(any())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 유효성 검사 - 메뉴 상품 존재 하지 않음")
    @Test
    void validate_product_not_exists() {
        // given
        MenuProductRequest menuProduct = menuProductRequest(1L, 2L);
        MenuRequest menuRequest = menuRequest("후라이드+후라이드", BigDecimal.valueOf(17000), 1L,
            Collections.singletonList(menuProduct));

        given(menuGroupRepository.existsById(any())).willReturn(true);
        given(productRepository.findAllById(anyList())).willReturn(Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 유효성 검사 - 메뉴 상품 가격, 상품 가격 합계 초과")
    @Test
    void validate_product_price_invalid() {
        // given
        MenuProductRequest menuProduct = menuProductRequest(1L, 2L);
        MenuRequest menuRequest = menuRequest("후라이드+후라이드", BigDecimal.valueOf(22000), 1L,
            Collections.singletonList(menuProduct));

        given(menuGroupRepository.existsById(any())).willReturn(true);
        List<Product> savedProducts = Collections.singletonList(savedProduct(1L, new BigDecimal(10000)));
        given(productRepository.findAllById(anyList())).willReturn(savedProducts);

        // when, then
        assertThatThrownBy(() -> menuValidator.validate(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
