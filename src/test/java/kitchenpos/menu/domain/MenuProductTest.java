package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품의 가격 총합 확인")
    void calculateTotalPrice() {
        //given
        Product 강정치킨 = new Product(1L, "강정치킨", new BigDecimal(17_000));
        MenuProduct menuProduct = new MenuProduct(강정치킨, 5);

        //when
        BigDecimal totalPrice = menuProduct.calculateTotalPrice();

        //then
        assertThat(totalPrice).isEqualTo(new BigDecimal(85_000));
    }


}