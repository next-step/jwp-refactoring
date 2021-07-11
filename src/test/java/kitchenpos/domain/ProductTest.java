package kitchenpos.domain;

import kitchenpos.application.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 객체 테스트")
@ExtendWith(MockitoExtension.class)
class ProductTest {

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("짜장면");
    }

    @Test
    void 상품_가격_음수_등록시_등록_실패() {
        product.setPrice(new BigDecimal(-1000));
        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_가격_null값_등록시_등록_실패() {
        assertThatThrownBy(() -> productService.create(product)).isInstanceOf(IllegalArgumentException.class);
    }
}
