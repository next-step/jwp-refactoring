package kitchenpos.application;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 허니콤보;
    private Product 레드윙;

    @BeforeEach
    void setUp() {
        허니콤보 = Product.of(1L, "허니콤보", BigDecimal.valueOf(18000));
        레드윙 = Product.of(2L, "레드윙", BigDecimal.valueOf(19000));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        when(productRepository.save(any())).thenReturn(허니콤보);

        ProductResponse result = productService.create(허니콤보);

        assertThat(result).isEqualTo(ProductResponse.from(허니콤보));
    }


    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(허니콤보, 레드윙));

        List<ProductResponse> results = productService.list();

        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(results).containsExactly(ProductResponse.from(허니콤보), ProductResponse.from(레드윙))
        );
    }
}
