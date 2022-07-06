package menu.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @DisplayName("메뉴상품을 생성한다.")
    @Test
    void create() {
        //given
        int quantity = 4;
        Long productId = 1L;

        //when
        MenuProduct menuProduct = new MenuProduct(productId, quantity);

        //then
        assertEquals(quantity, menuProduct.getQuantity());
        assertEquals(productId, menuProduct.getProductId());
    }
}