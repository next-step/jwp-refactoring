package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.repos.ProductRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    private Product product;

    private Product product2;

    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        product = Product.of(1L, "후라이드치킨", new BigDecimal(16000.00));
        product2 = Product.of(2L, "양념치킨", new BigDecimal(16000.00));
        productRequest = new ProductRequest("후라이드치킨", new BigDecimal(16000.00));
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void create() {
        // given
        when(productRepository.save(any())).thenReturn(product);

        // when
        Product expected = productService.create(productRequest);

        // then
        assertThat(product.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<Product> actual = Arrays.asList(product, product2);
        when(productRepository.findAll()).thenReturn(actual);

        // when
        List<Product> expected = productService.list();

        // then
        assertThat(actual.size()).isEqualTo(expected.size());
    }
}
