package kitchenpos.domain.product;

import kitchenpos.dto.product.ProductRequest;
import kitchenpos.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.of("product", Price.of(1000L));
    }

    @Test
    @DisplayName("상품을 등록할 수 있다")
    void create() {
        given(productRepository.save(product)).willReturn(product);

        ProductRequest productRequest = new ProductRequest("product", 1000L);
        productService.create(productRequest);

        verify(productRepository).save(product);
    }
}