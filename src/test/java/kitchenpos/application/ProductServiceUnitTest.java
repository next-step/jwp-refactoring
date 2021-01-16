package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 가격 없이 생성한다")
    @Test
    public void testCreateProductWithoutPrice() {
        // given
        Product product = new Product();
        product.setName("후라이드");

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품의 가격을 0원 미만으로 생성한다")
    @Test
    public void testCreateProductWithNegativePrice() {
        // given
        Product product = new Product();
        product.setName("후라이드");
        product.setPrice(new BigDecimal(-1000));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }
}
