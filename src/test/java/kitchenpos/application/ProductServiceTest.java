package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.menu.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private ProductRequest productRequest;
    private Product product;
    private final static long ANY_PRODUCT_ID = 1L;
    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest("product", BigDecimal.valueOf(1000L));
        product = Product.of("product", BigDecimal.valueOf(1000L));
    }

    @Test
    void create() {

        given(productDao.save(product))
                .willReturn(product);

        productService.create(productRequest);
        verify(productDao).save(product);
    }
}