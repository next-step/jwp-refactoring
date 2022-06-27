package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.fixture.ProductFixture.상품_데이터_생성;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuProductTest {

    @DisplayName("메뉴상품을 생성한다.")
    @Test
    void create() {
        //given
        int quantity = 4;
        Product product = 상품_데이터_생성(1L, "product", BigDecimal.valueOf(1000));

        //when
        MenuProduct menuProduct = new MenuProduct(product, quantity);

        //then
        assertEquals(quantity, menuProduct.getQuantity());
        assertEquals(product.getId(), menuProduct.getProduct().getId());
    }
}