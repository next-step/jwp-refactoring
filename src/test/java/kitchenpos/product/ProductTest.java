package kitchenpos.product;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품")
class ProductTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @Test
    @DisplayName("상품의 이름이 비어있을 경우 예외가 발생한다.")
    void emptyProductName() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            productService.create(new Product(" ", BigDecimal.valueOf(1000)));
        });
    }

    @Test
    @DisplayName("상품의 가격이 0원 이상이 아닐 경우 예외가 발생한다.")
    void productPriceLessThanZero() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            productService.create(new Product("후라이드", BigDecimal.valueOf(-1000)));
        });
    }

}
