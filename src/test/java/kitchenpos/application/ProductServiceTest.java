package kitchenpos.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    private Product 치킨;
    private Product 치즈볼;

    @BeforeEach
    void setUp() {
        치킨 = new Product("치킨", BigDecimal.valueOf(3000L));
        치즈볼 = new Product("치즈볼", BigDecimal.valueOf(1500L));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품_생성() {
        // given
        when(productRepository.save(치킨)).thenReturn(치킨);

        // when
        Product saveProduct = productService.create(치킨);

        // then
        assertThat(saveProduct).isEqualTo(치킨);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void 상품_목록_조회() {
        // given
        List<Product> products = Arrays.asList(치킨, 치즈볼);
        when(productRepository.findAll()).thenReturn(products);

        // when
        List<Product> findProducts = productService.list();

        // then
        assertAll(
                () -> assertThat(findProducts).hasSize(products.size()),
                () -> assertThat(findProducts).containsExactly(치킨, 치즈볼)
        );
    }
}
