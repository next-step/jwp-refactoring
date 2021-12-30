package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;
    
    private Product 상품1;
    private Product 상품2;
    private List<Product> 상품_목록;
    private ProductRequest 상품_요청1;
    private ProductRequest 상품_요청2;
    
    @BeforeEach
    void setUp() {
        상품1 = Product.of(1L, "상품1", 10000);
        상품2 = Product.of(2L, "상품2", 20000);
        상품_목록 = Lists.newArrayList(상품1, 상품2);
        상품_요청1 = ProductRequest.of("상품1", 10000);
        상품_요청2 = ProductRequest.of("상품2", 20000);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void saveProduct() {
        given(productRepository.save(any())).willReturn(상품1);

        final ProductResponse 상품_응답 = productService.create(상품_요청1);

        assertAll(
                () -> assertThat(상품_응답).isNotNull(),
                () -> assertThat(상품_응답.getId()).isEqualTo(상품1.getId()),
                () -> assertThat(상품_응답.getPrice()).isEqualTo(상품1.getPrice().toLong()),
                () -> assertThat(상품_응답.getName()).isEqualTo(상품1.getName())
        );
    }

    @DisplayName("등록한 상품을 조회한다.")
    @Test
    void findProducts() {
        given(productRepository.findAll()).willReturn(상품_목록);

        final List<ProductResponse> 상품_목록_응답 = productService.list();

        assertThat(상품_목록_응답).hasSize(2);
    }
}
