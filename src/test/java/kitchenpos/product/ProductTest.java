package kitchenpos.product;

import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @DisplayName("product Equals 테스트")
    @Test
    void productEqualsTest() {
        //given
        Long id = 1L;
        String productName = "참치김밥";
        Price price = new Price(new BigDecimal(10000));
        final Product product1 = new Product(id, productName, price);
        final Product product2 = new Product(id, productName, price);

        //when
        //then
        assertThat(product1).isEqualTo(product2);
    }
}
