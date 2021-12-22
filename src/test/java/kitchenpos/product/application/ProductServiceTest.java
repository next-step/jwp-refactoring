package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.product.fixture.ProductFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 상품_후라이드;
    private Product 상품_치킨무;
    private Product 상품_양념소스;

    @BeforeEach
    void setUp() {
        상품_후라이드 = ProductFixture.create(1L, "후라이드", BigDecimal.valueOf(15000L));
        상품_치킨무 = ProductFixture.create(2L, "치킨무", BigDecimal.valueOf(500L));
        상품_양념소스 = ProductFixture.create(3L, "양념소스", BigDecimal.valueOf(500L));
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        given(productRepository.save(상품_후라이드)).willReturn(상품_후라이드);

        Product savedProduct = productService.create(상품_후라이드);

        assertThat(savedProduct).isEqualTo(상품_후라이드);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        given(productRepository.findAll()).willReturn(Arrays.asList(상품_후라이드, 상품_치킨무, 상품_양념소스));

        List<Product> products = productService.list();

        assertThat(products).containsExactly(상품_후라이드, 상품_치킨무, 상품_양념소스);
    }
}