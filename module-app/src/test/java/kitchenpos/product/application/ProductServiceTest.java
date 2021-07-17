package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@DisplayName("상품 서비스")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private List<Product> products;
    private Product createProduct;
    private ProductRequest createRequest;

    @BeforeEach
    void setUp() {
        createProduct = new Product("A", BigDecimal.valueOf(10_000.00));
        createRequest = new ProductRequest("A", BigDecimal.valueOf(10_000.00));
        products = Arrays.asList(new Product("B", BigDecimal.valueOf(5_000.00)),
                new Product("C", BigDecimal.valueOf(4_000.00)));
    }

    @Test
    @DisplayName("상품 전체 조회")
    void find_allProducts() {
        // mocking
        when(productRepository.findAll()).thenReturn(products);

        // when
        List<ProductResponse> resultProducts = productService.findAllProducts();

        // then
        assertThat(resultProducts.size()).isEqualTo(products.size());
    }

    @Test
    @DisplayName("상품 등록")
    void create_product() {
        // given
        given(productRepository.save(any(Product.class))).willReturn(createProduct);

        // when
        ProductResponse productResponse = productService.create(createRequest);

        // then
        assertThat(productResponse.getId()).isEqualTo(createProduct.getId());
    }
}
