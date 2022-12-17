package kitchenpos.menu.application;

import static kitchenpos.menu.domain.MenuProductFixture.*;
import static kitchenpos.product.domain.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.ProductRepository;

@DisplayName("메뉴 상품 금액 계산기 테스트")
@ExtendWith(MockitoExtension.class)
class MenuProductPriceCalculatorTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuProductPriceCalculator menuProductPriceCalculator;

    @DisplayName("존재하지 않는 상품 ID")
    @Test
    void calculateSum_product_not_exists() {
        // given
        List<MenuProductRequest> menuProducts = Collections.singletonList(
            menuProductRequest(1L, 2)
        );
        given(productRepository.findAllById(anyList())).willReturn(Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> menuProductPriceCalculator.calculateSum(menuProducts))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("금액 합계 계산 - 단일 상품")
    @Test
    void calculateSum_single_product() {
        // given
        List<MenuProductRequest> menuProducts = Collections.singletonList(menuProductRequest(1L, 2));
        given(productRepository.findAllById(anyList()))
            .willReturn(Collections.singletonList(savedProduct(1L, BigDecimal.valueOf(5000))));

        // when
        ProductPrice actual = menuProductPriceCalculator.calculateSum(menuProducts);

        // then
        assertThat(actual).isEqualTo(ProductPrice.from(10000));
    }

    @DisplayName("금액 합계 계산 - 여러 상품")
    @Test
    void calculateSum_multiple_product() {
        // given
        List<MenuProductRequest> menuProducts = Arrays.asList(
            menuProductRequest(1L, 2),
            menuProductRequest(2L, 3)
        );
        given(productRepository.findAllById(anyList())).willReturn(Arrays.asList(
            savedProduct(1L, BigDecimal.valueOf(5000)),
            savedProduct(2L, BigDecimal.valueOf(6000))
        ));

        // when
        ProductPrice actual = menuProductPriceCalculator.calculateSum(menuProducts);

        // then
        assertThat(actual).isEqualTo(ProductPrice.from(28000));
    }
}
