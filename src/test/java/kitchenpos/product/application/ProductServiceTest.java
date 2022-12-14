package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.application.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 버팔로윙;
    private Product 치킨텐더;

    @BeforeEach
    void setUp() {
        버팔로윙 = Product.of(1L, "버팔로윙", BigDecimal.valueOf(6_500));
        치킨텐더 = Product.of(2L, "치킨텐더", BigDecimal.valueOf(5_900));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        when(productRepository.save(any())).thenReturn(버팔로윙);

        ProductResponse result = productService.create(버팔로윙);

        assertThat(result).isEqualTo(ProductResponse.from(버팔로윙));
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(버팔로윙, 치킨텐더));

        List<ProductResponse> results = productService.list();

        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(results).containsExactly(ProductResponse.from(버팔로윙), ProductResponse.from(치킨텐더))
        );
    }
}
