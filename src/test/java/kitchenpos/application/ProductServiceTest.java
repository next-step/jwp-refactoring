package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Product;
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

    private Product product;
    private final static long ANY_PRODUCT_ID = 1L;
    @BeforeEach
    void setUp() {
        product = Product.of("product", BigDecimal.valueOf(1000L));
        ReflectionTestUtils.setField(product, "id", ANY_PRODUCT_ID);
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

        assertThatThrownBy(() -> product.changePrice(UNDER_ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }
}