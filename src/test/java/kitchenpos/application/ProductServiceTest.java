package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private final static long ANY_PRODUCT_ID = 1L;
    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(ANY_PRODUCT_ID);
        product.setName("물건");
        product.setPrice(BigDecimal.valueOf(1000L));
    }

    @Test
    void create() {
        given(productDao.save(product))
                .willReturn(product);

        productService.create(product);
        verify(productDao).save(product);
    }

    @Test
    void exception_when_price_is_under_zero() {
        BigDecimal UNDER_ZERO = BigDecimal.valueOf(-1L);
        product.setPrice(UNDER_ZERO);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }
}