package product.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void create() {
        //given
        String name = "product";
        BigDecimal price = BigDecimal.valueOf(1000);

        //when
        Product product = new Product(name, price);

        //then
        assertAll(
                () -> assertEquals(name, product.getName()),
                () -> assertEquals(price, product.getPrice())
        );
    }
}