package kitchenpos.application;

import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.menu.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequest productRequest;
    private Product product;
    @BeforeEach
    void setUp() {
        product = Product.of("product", BigDecimal.valueOf(1000L));
    }

    @Test
    void create() {
        given(productRepository.save(product)).willReturn(product);

        productRequest = new ProductRequest("product", BigDecimal.valueOf(1000L));
        productService.create(productRequest);

        verify(productRepository).save(product);
    }
}