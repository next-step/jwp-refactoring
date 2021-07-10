package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductsTest {

    @DisplayName("Product ID와 수량으로 가격 합을 계산하는 테스트")
    @Test
    void calculatePrice() {
        // given
        final BigDecimal price = new BigDecimal(100);
        final Product product1 = new Product(1L, "name", price);
        final Product product2 = new Product(2L, "name", new BigDecimal(200));
        List<Product> productList = Arrays.asList(product1, product2);
        final Products products = new Products(productList);
        final int quantity = 2;

        // when
        final BigDecimal actual = products.calculatePrice(1L, quantity);

        // then
        assertThat(actual).isEqualTo(price.multiply(new BigDecimal(quantity)));
    }
}
