package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductMenuServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductMenuServiceImpl productMenuService;

    @Test
    @DisplayName("제품가격 합계 계산을 위한 상품조회 실패시 오류")
    void calculateProductsPrice_notFound_exception() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> productMenuService.calculateProductsPrice(1L, 2L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("조회된 제품이 없습니다.");
    }

    @Test
    @DisplayName("제품가격 합계 계산")
    void calculateProductsPrice() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.of(new Product("A", BigDecimal.valueOf(5_000.00))));

        // then
        assertThat(productMenuService.calculateProductsPrice(1L, 2L)).isEqualTo(BigDecimal.valueOf(10_000.00));
    }

}
