package menu.domain.mokito;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.assertj.core.util.*;
import org.junit.jupiter.api.*;

import fixture.*;
import menu.application.*;
import menu.dto.*;
import menu.repository.*;

@DisplayName("상품 관련(Mockito)")
class ProductMockitoTest {
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @DisplayName("상품 생성하기")
    @Test
    void createTest() {
        when(productRepository.save(any())).thenReturn(ProductFixture.상품_후라이드치킨);

        ProductRequest request = ProductRequest.of(ProductFixture.상품_후라이드치킨.getName(), ProductFixture.상품_후라이드치킨.getPrice());
        assertThat(productService.save(request)).isInstanceOf(ProductResponse.class);
    }

    @DisplayName("상품 조회하기")
    @Test
    void findAllTest() {
        when(productRepository.findAll()).thenReturn(Lists.newArrayList(ProductFixture.상품_후라이드치킨, ProductFixture.상품_양념치킨, ProductFixture.상품_반반치킨));
        assertThat(productService.findAll()).containsExactly(
            ProductResponse.from(ProductFixture.상품_후라이드치킨),
            ProductResponse.from(ProductFixture.상품_양념치킨),
            ProductResponse.from(ProductFixture.상품_반반치킨)
        );
    }
}
