package kitchenpos.product.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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