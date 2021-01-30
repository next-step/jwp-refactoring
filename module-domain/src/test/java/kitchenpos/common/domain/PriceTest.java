package kitchenpos.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PriceTest {
    @DisplayName("상품의 가격이 0 이하 이면 Exception")
    @Test
    void addProductPriceExceptionTest() {
        assertThrows(IllegalArgumentException.class,
                () ->  Price.of(-10));
    }
}