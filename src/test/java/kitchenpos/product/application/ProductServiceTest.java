package kitchenpos.product.application;

import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 생성 성공")
    @Test
    void createProductSuccess() {
        // when
        productService.create("name", 1000L);

        // then
        verify(productRepository).save(any());
    }

    @DisplayName("상품 생성 실패 - 상품 가격이 음수")
    @NullSource
    @ValueSource(longs = { -1000, -5000 })
    @ParameterizedTest
    void createProductFail(Long price) {
        // when
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create("name", price));

        // then
        verify(productRepository, never()).save(any());
    }
}
